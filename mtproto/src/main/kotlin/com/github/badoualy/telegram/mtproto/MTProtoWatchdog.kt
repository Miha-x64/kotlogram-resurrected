package com.github.badoualy.telegram.mtproto

import com.github.badoualy.telegram.mtproto.log.Logger
import com.github.badoualy.telegram.mtproto.net.MTProtoSelectableConnection
import com.github.badoualy.telegram.mtproto.util.NamedThreadFactory
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.util.concurrent.Executors

/**
 * Permanently listen for messages on registered connections and wrap everything in an Observable,
 * every received messages will be send to the subscribers.
 * If no connections are registered, the thread will stop itself
 * (but still be usable, and restart itself when a new connection is registered).
 * */
internal object MTProtoWatchdog : Runnable {

    private val logger = Logger.Factory.create(MTProtoWatchdog::class)

    /** [Selector.select] operation timeout */
    private const val SELECT_TIMEOUT_DELAY = 10 * 1000L // 10 seconds
    /** If no connection are registered since this delay, the listening thread will stop itself */
    private const val IDLE_SHUTDOWN_DELAY = 60 * 1000L // 1 min

    private val selector = Selector.open()
    private val subject: Subject<SelectionKey> = PublishSubject.create()
    private val registerQueue = ArrayList<MTProtoSelectableConnection>()

    private val executor = Executors.newSingleThreadExecutor(NamedThreadFactory(javaClass.simpleName,
                                                                                singleThread = true,
                                                                                daemon = true))
    private val pool = Executors.newCachedThreadPool(NamedThreadFactory("${javaClass.simpleName}-exec",
                                                                        singleThread = false,
                                                                        daemon = true))

    @Volatile
    private var running = false
    private var lastBusyTime: Long = 0

    override fun run() {
        logger.trace("Starting watchdog")
        lastBusyTime = System.currentTimeMillis()
        while (running) {
            // Register new connections
            synchronized(this) {
                if (registerQueue.isNotEmpty()) {
                    registerQueue.forEach { it.register(selector, SelectionKey.OP_READ) }
                    registerQueue.clear()
                }
                logger.debug("has ${selector.keys().size} keys")
            }

            // Blocking select call
            if (selector.select(SELECT_TIMEOUT_DELAY) > 0) {
                logger.trace("select() returned with results")
                // We have key(s) ready to read
                synchronized(this) {
                    selector.selectedKeys().forEach { key ->
                        key.noOps()
                        subject.onNext(key)
                    }
                }
                selector.selectedKeys().clear()
            } else logger.trace("select() returned with nothing")

            // Check if should stop
            // Avoid synchronizing each loop
            if (selector.selectedKeys().isEmpty()) {
                if (System.currentTimeMillis() - lastBusyTime >= IDLE_SHUTDOWN_DELAY) {
                    synchronized(this) {
                        if (selector.selectedKeys().isEmpty()) {
                            running = false
                            logger.trace("Stopping watchdog")
                            return
                        }
                    }
                }
            } else {
                lastBusyTime = System.currentTimeMillis()
            }
        }
    }

    /**
     * Build an observable that'll emit each received message from the given connection.
     * Messages are read via the [MTProtoSelectableConnection.readMessage] method.
     * The connection will be registered on the [Selector] when a subscription is made to the [Observable]
     *  @return an observable that'll emit each received message from the given connection.
     */
    // TODO: ha
    fun getMessageObservable(connection: MTProtoSelectableConnection): Observable<ByteArray> = subject
            .filter { it.attachment() === connection }
            .observeOn(Schedulers.from(pool))
            .map { key -> connection.readMessage().also { listen(key) } }
            .doOnSubscribe {
                logger.info(connection.tag, "adding to registerQueue")
                synchronized(this) {
                    if (connection.channel.keyFor(selector) == null) {
                        registerQueue.add(connection)
                        runOrWakeup()
                    }
                }
            }
            .doOnError {
                logger.error(connection.tag, "onError: cancel selectionKey")
                cancelByTag(connection)
                synchronized(this) {
                    registerQueue.remove(connection)
                }
            }
            .doOnDispose {
                logger.debug(connection.tag, "onDispose: cancel selectionKey")
                cancelByTag(connection)
                synchronized(this) {
                    registerQueue.remove(connection)
                }
            }
            .observeOn(Schedulers.computation())!! // Ensure pool private usage

    /**
     * Start listening on the [executor] thread or wakeup [selector]
     * (main loop will check for new connections to register).
     */
    private fun runOrWakeup() {
        synchronized(this) {
            if (!running) {
                running = true
                executor.execute(this)
            } else {
                selector.wakeup()
            }
        }
    }

    /**
     * Update the selection key interested ops with the [SelectionKey.OP_READ] flag.
     * Also run or wake up listening thread.
     */
    private fun listen(key: SelectionKey) {
        synchronized(this) {
            key.readOp()
            runOrWakeup()
        }
    }

    /** Unregister a selection key after finding it by its attached connection */
    private fun cancelByTag(connection: MTProtoSelectableConnection) = selector.keys().firstOrNull {
        it.attachment() === connection
    }?.let { cancel(it) }

    // TODO: check if cancel will block thread like register, if so, use unregisterQueue
    /** Unregister given selection key */
    private fun cancel(key: SelectionKey) {
        synchronized(this) {
            key.attach(null)
            selector.wakeup()
            key.cancel()
            selector.wakeup()
        }
    }

    private fun SelectionKey.noOps() = interestOps(0)

    private fun SelectionKey.readOp() = interestOps(SelectionKey.OP_READ)

    private fun MTProtoSelectableConnection.register(selector: Selector, ops: Int): SelectionKey {
        channel.configureBlocking(false)
        return channel.register(selector, ops, this)!!
    }
}
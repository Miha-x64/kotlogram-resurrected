package com.github.badoualy.telegram.tl.api

import com.github.badoualy.telegram.tl.TLObjectUtils.SIZE_BOOLEAN
import com.github.badoualy.telegram.tl.TLObjectUtils.SIZE_CONSTRUCTOR_ID
import com.github.badoualy.telegram.tl.TLObjectUtils.SIZE_INT32
import com.github.badoualy.telegram.tl.TLObjectUtils.computeTLStringSerializedSize
import com.github.badoualy.telegram.tl.core.TLObject
import com.github.badoualy.telegram.tl.serialization.TLDeserializer
import com.github.badoualy.telegram.tl.serialization.TLSerializer
import java.io.IOException

/**
 * peerNotifySettings#af509d20
 *
 * @author Yannick Badoual yann.badoual@gmail.com
 * @see <a href="http://github.com/badoualy/kotlogram">http://github.com/badoualy/kotlogram</a>
 */
class TLPeerNotifySettings() : TLObject() {
    var showPreviews: Boolean? = null

    var silent: Boolean? = null

    var muteUntil: Int? = null

    var sound: String? = null

    private val _constructor: String = "peerNotifySettings#af509d20"

    override val constructorId: Int = CONSTRUCTOR_ID

    constructor(
            showPreviews: Boolean?,
            silent: Boolean?,
            muteUntil: Int?,
            sound: String?
    ) : this() {
        this.showPreviews = showPreviews
        this.silent = silent
        this.muteUntil = muteUntil
        this.sound = sound
    }

    override fun computeFlags() {
        _flags = 0
        updateFlags(showPreviews, 1)
        updateFlags(silent, 2)
        updateFlags(muteUntil, 4)
        updateFlags(sound, 8)
    }

    @Throws(IOException::class)
    override fun serializeBody(tlSerializer: TLSerializer) = with (tlSerializer)  {
        computeFlags()

        writeInt(_flags)
        doIfMask(showPreviews, 1) { writeBoolean(it) }
        doIfMask(silent, 2) { writeBoolean(it) }
        doIfMask(muteUntil, 4) { writeInt(it) }
        doIfMask(sound, 8) { writeString(it) }
    }

    @Throws(IOException::class)
    override fun deserializeBody(tlDeserializer: TLDeserializer) = with (tlDeserializer)  {
        _flags = readInt()
        showPreviews = readIfMask(1) { readBoolean() }
        silent = readIfMask(2) { readBoolean() }
        muteUntil = readIfMask(4) { readInt() }
        sound = readIfMask(8) { readString() }
    }

    override fun computeSerializedSize(): Int {
        computeFlags()

        var size = SIZE_CONSTRUCTOR_ID
        size += SIZE_INT32
        size += getIntIfMask(showPreviews, 1) { SIZE_BOOLEAN }
        size += getIntIfMask(silent, 2) { SIZE_BOOLEAN }
        size += getIntIfMask(muteUntil, 4) { SIZE_INT32 }
        size += getIntIfMask(sound, 8) { computeTLStringSerializedSize(it) }
        return size
    }

    override fun toString() = _constructor

    override fun equals(other: Any?): Boolean {
        if (other !is TLPeerNotifySettings) return false
        if (other === this) return true

        return _flags == other._flags
                && showPreviews == other.showPreviews
                && silent == other.silent
                && muteUntil == other.muteUntil
                && sound == other.sound
    }
    companion object  {
        const val CONSTRUCTOR_ID: Int = 0xaf509d20.toInt()
    }
}

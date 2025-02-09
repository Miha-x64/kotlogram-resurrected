package com.github.badoualy.telegram.mtproto.tl

import com.github.badoualy.telegram.tl.core.TLObject
import com.github.badoualy.telegram.tl.serialization.TLDeserializer
import com.github.badoualy.telegram.tl.serialization.TLSerializer
import java.io.IOException

class MTNewSessionCreated @JvmOverloads constructor(var firstMsgId: Long = 0,
                                                    var uniqId: Long = 0,
                                                    var serverSalt: Long = 0) : TLObject() {

    override val constructorId: Int = CONSTRUCTOR_ID

    @Throws(IOException::class)
    override fun serializeBody(tlSerializer: TLSerializer) = with(tlSerializer) {
        writeLong(firstMsgId)
        writeLong(uniqId)
        writeLong(serverSalt)
    }

    @Throws(IOException::class)
    override fun deserializeBody(tlDeserializer: TLDeserializer) = with(tlDeserializer) {
        firstMsgId = readLong()
        uniqId = readLong()
        serverSalt = readLong()
    }

    override fun toString() = "new_session_created#9ec20908"

    companion object {
        @JvmField
        val CONSTRUCTOR_ID = -1631450872
    }
}

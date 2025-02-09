package com.github.badoualy.telegram.tl.api.request

import com.github.badoualy.telegram.tl.TLObjectUtils.SIZE_CONSTRUCTOR_ID
import com.github.badoualy.telegram.tl.TLObjectUtils.computeTLStringSerializedSize
import com.github.badoualy.telegram.tl.core.TLBool
import com.github.badoualy.telegram.tl.core.TLMethod
import com.github.badoualy.telegram.tl.serialization.TLDeserializer
import com.github.badoualy.telegram.tl.serialization.TLSerializer
import java.io.IOException

/**
 * @author Yannick Badoual yann.badoual@gmail.com
 * @see <a href="http://github.com/badoualy/kotlogram">http://github.com/badoualy/kotlogram</a>
 */
class TLRequestAccountVerifyEmail() : TLMethod<TLBool>() {
    var email: String = ""

    var code: String = ""

    private val _constructor: String = "account.verifyEmail#ecba39db"

    override val constructorId: Int = CONSTRUCTOR_ID

    constructor(email: String, code: String) : this() {
        this.email = email
        this.code = code
    }

    @Throws(IOException::class)
    override fun serializeBody(tlSerializer: TLSerializer) = with (tlSerializer)  {
        writeString(email)
        writeString(code)
    }

    @Throws(IOException::class)
    override fun deserializeBody(tlDeserializer: TLDeserializer) = with (tlDeserializer)  {
        email = readString()
        code = readString()
    }

    override fun computeSerializedSize(): Int {
        var size = SIZE_CONSTRUCTOR_ID
        size += computeTLStringSerializedSize(email)
        size += computeTLStringSerializedSize(code)
        return size
    }

    override fun toString() = _constructor

    override fun equals(other: Any?): Boolean {
        if (other !is TLRequestAccountVerifyEmail) return false
        if (other === this) return true

        return email == other.email
                && code == other.code
    }
    companion object  {
        const val CONSTRUCTOR_ID: Int = 0xecba39db.toInt()
    }
}

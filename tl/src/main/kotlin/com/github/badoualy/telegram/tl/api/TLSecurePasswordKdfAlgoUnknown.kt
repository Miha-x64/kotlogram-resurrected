package com.github.badoualy.telegram.tl.api

/**
 * securePasswordKdfAlgoUnknown#4a8537
 *
 * @author Yannick Badoual yann.badoual@gmail.com
 * @see <a href="http://github.com/badoualy/kotlogram">http://github.com/badoualy/kotlogram</a>
 */
class TLSecurePasswordKdfAlgoUnknown : TLAbsSecurePasswordKdfAlgo() {
    private val _constructor: String = "securePasswordKdfAlgoUnknown#4a8537"

    override val constructorId: Int = CONSTRUCTOR_ID

    override fun toString() = _constructor

    override fun equals(other: Any?): Boolean {
        if (other !is TLSecurePasswordKdfAlgoUnknown) return false
        if (other === this) return true

        return true
    }
    companion object  {
        const val CONSTRUCTOR_ID: Int = 0x4a8537
    }
}

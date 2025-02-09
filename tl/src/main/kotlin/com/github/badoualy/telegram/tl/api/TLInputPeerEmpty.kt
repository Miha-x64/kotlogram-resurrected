package com.github.badoualy.telegram.tl.api

/**
 * inputPeerEmpty#7f3b18ea
 *
 * @author Yannick Badoual yann.badoual@gmail.com
 * @see <a href="http://github.com/badoualy/kotlogram">http://github.com/badoualy/kotlogram</a>
 */
class TLInputPeerEmpty : TLAbsInputPeer() {
    private val _constructor: String = "inputPeerEmpty#7f3b18ea"

    override val constructorId: Int = CONSTRUCTOR_ID

    override fun toString() = _constructor

    override fun equals(other: Any?): Boolean {
        if (other !is TLInputPeerEmpty) return false
        if (other === this) return true

        return true
    }
    companion object  {
        const val CONSTRUCTOR_ID: Int = 0x7f3b18ea
    }
}

package com.github.badoualy.telegram.tl.api

/**
 * updateStickerSets#43ae3dec
 *
 * @author Yannick Badoual yann.badoual@gmail.com
 * @see <a href="http://github.com/badoualy/kotlogram">http://github.com/badoualy/kotlogram</a>
 */
class TLUpdateStickerSets : TLAbsUpdate() {
    private val _constructor: String = "updateStickerSets#43ae3dec"

    override val constructorId: Int = CONSTRUCTOR_ID

    override fun toString() = _constructor

    override fun equals(other: Any?): Boolean {
        if (other !is TLUpdateStickerSets) return false
        if (other === this) return true

        return true
    }
    companion object  {
        const val CONSTRUCTOR_ID: Int = 0x43ae3dec
    }
}

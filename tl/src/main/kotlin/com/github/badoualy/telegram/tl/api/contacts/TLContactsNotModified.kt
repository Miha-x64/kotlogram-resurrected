package com.github.badoualy.telegram.tl.api.contacts

/**
 * contacts.contactsNotModified#b74ba9d2
 *
 * @author Yannick Badoual yann.badoual@gmail.com
 * @see <a href="http://github.com/badoualy/kotlogram">http://github.com/badoualy/kotlogram</a>
 */
class TLContactsNotModified : TLAbsContacts() {
    private val _constructor: String = "contacts.contactsNotModified#b74ba9d2"

    override val constructorId: Int = CONSTRUCTOR_ID

    override fun toString() = _constructor

    override fun equals(other: Any?): Boolean {
        if (other !is TLContactsNotModified) return false
        if (other === this) return true

        return true
    }
    companion object  {
        const val CONSTRUCTOR_ID: Int = 0xb74ba9d2.toInt()
    }
}

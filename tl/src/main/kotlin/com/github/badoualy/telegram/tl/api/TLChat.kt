package com.github.badoualy.telegram.tl.api

import com.github.badoualy.telegram.tl.TLObjectUtils.SIZE_CONSTRUCTOR_ID
import com.github.badoualy.telegram.tl.TLObjectUtils.SIZE_INT32
import com.github.badoualy.telegram.tl.TLObjectUtils.SIZE_INT64
import com.github.badoualy.telegram.tl.TLObjectUtils.computeTLStringSerializedSize
import com.github.badoualy.telegram.tl.serialization.TLDeserializer
import com.github.badoualy.telegram.tl.serialization.TLSerializer
import java.io.IOException

/**
 * chat#41cbf256
 *
 * @author Yannick Badoual yann.badoual@gmail.com
 * @see <a href="http://github.com/badoualy/kotlogram">http://github.com/badoualy/kotlogram</a>
 */
class TLChat() : TLAbsChat() {
    @Transient
    var creator: Boolean = false

    @Transient
    var kicked: Boolean = false

    @Transient
    var left: Boolean = false

    @Transient
    var deactivated: Boolean = false

    @Transient
    var callActive: Boolean = false

    @Transient
    var callNotEmpty: Boolean = false

    @Transient
    var noforwards: Boolean = false

    override var id: Long = 0L

    var title: String = ""

    var photo: TLAbsChatPhoto = TLChatPhotoEmpty()

    var participantsCount: Int = 0

    var date: Int = 0

    var version: Int = 0

    var migratedTo: TLAbsInputChannel? = null

    var adminRights: TLChatAdminRights? = null

    var defaultBannedRights: TLChatBannedRights? = null

    private val _constructor: String = "chat#41cbf256"

    override val constructorId: Int = CONSTRUCTOR_ID

    constructor(
            creator: Boolean,
            kicked: Boolean,
            left: Boolean,
            deactivated: Boolean,
            callActive: Boolean,
            callNotEmpty: Boolean,
            noforwards: Boolean,
            id: Long,
            title: String,
            photo: TLAbsChatPhoto,
            participantsCount: Int,
            date: Int,
            version: Int,
            migratedTo: TLAbsInputChannel?,
            adminRights: TLChatAdminRights?,
            defaultBannedRights: TLChatBannedRights?
    ) : this() {
        this.creator = creator
        this.kicked = kicked
        this.left = left
        this.deactivated = deactivated
        this.callActive = callActive
        this.callNotEmpty = callNotEmpty
        this.noforwards = noforwards
        this.id = id
        this.title = title
        this.photo = photo
        this.participantsCount = participantsCount
        this.date = date
        this.version = version
        this.migratedTo = migratedTo
        this.adminRights = adminRights
        this.defaultBannedRights = defaultBannedRights
    }

    override fun computeFlags() {
        _flags = 0
        updateFlags(creator, 1)
        updateFlags(kicked, 2)
        updateFlags(left, 4)
        updateFlags(deactivated, 32)
        updateFlags(callActive, 8388608)
        updateFlags(callNotEmpty, 16777216)
        updateFlags(noforwards, 33554432)
        updateFlags(migratedTo, 64)
        updateFlags(adminRights, 16384)
        updateFlags(defaultBannedRights, 262144)
    }

    @Throws(IOException::class)
    override fun serializeBody(tlSerializer: TLSerializer) = with (tlSerializer)  {
        computeFlags()

        writeInt(_flags)
        writeLong(id)
        writeString(title)
        writeTLObject(photo)
        writeInt(participantsCount)
        writeInt(date)
        writeInt(version)
        doIfMask(migratedTo, 64) { writeTLObject(it) }
        doIfMask(adminRights, 16384) { writeTLObject(it) }
        doIfMask(defaultBannedRights, 262144) { writeTLObject(it) }
    }

    @Throws(IOException::class)
    override fun deserializeBody(tlDeserializer: TLDeserializer) = with (tlDeserializer)  {
        _flags = readInt()
        creator = isMask(1)
        kicked = isMask(2)
        left = isMask(4)
        deactivated = isMask(32)
        callActive = isMask(8388608)
        callNotEmpty = isMask(16777216)
        noforwards = isMask(33554432)
        id = readLong()
        title = readString()
        photo = readTLObject<TLAbsChatPhoto>()
        participantsCount = readInt()
        date = readInt()
        version = readInt()
        migratedTo = readIfMask(64) { readTLObject<TLAbsInputChannel>() }
        adminRights = readIfMask(16384) { readTLObject<TLChatAdminRights>(TLChatAdminRights::class, TLChatAdminRights.CONSTRUCTOR_ID) }
        defaultBannedRights = readIfMask(262144) { readTLObject<TLChatBannedRights>(TLChatBannedRights::class, TLChatBannedRights.CONSTRUCTOR_ID) }
    }

    override fun computeSerializedSize(): Int {
        computeFlags()

        var size = SIZE_CONSTRUCTOR_ID
        size += SIZE_INT32
        size += SIZE_INT64
        size += computeTLStringSerializedSize(title)
        size += photo.computeSerializedSize()
        size += SIZE_INT32
        size += SIZE_INT32
        size += SIZE_INT32
        size += getIntIfMask(migratedTo, 64) { it.computeSerializedSize() }
        size += getIntIfMask(adminRights, 16384) { it.computeSerializedSize() }
        size += getIntIfMask(defaultBannedRights, 262144) { it.computeSerializedSize() }
        return size
    }

    override fun toString() = _constructor

    override fun equals(other: Any?): Boolean {
        if (other !is TLChat) return false
        if (other === this) return true

        return _flags == other._flags
                && creator == other.creator
                && kicked == other.kicked
                && left == other.left
                && deactivated == other.deactivated
                && callActive == other.callActive
                && callNotEmpty == other.callNotEmpty
                && noforwards == other.noforwards
                && id == other.id
                && title == other.title
                && photo == other.photo
                && participantsCount == other.participantsCount
                && date == other.date
                && version == other.version
                && migratedTo == other.migratedTo
                && adminRights == other.adminRights
                && defaultBannedRights == other.defaultBannedRights
    }
    companion object  {
        const val CONSTRUCTOR_ID: Int = 0x41cbf256
    }
}

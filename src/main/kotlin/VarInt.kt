class VarInt(val value: Long) {
    constructor(value: Int) : this(value.toLong())

    val size: Int
        get() = when {
            value < 0 -> 9
            value < 0xFD -> 1
            value <= 0xFFFFL -> 3
            value <= 0xFFFFFFFFL -> 5
            else -> 9
        }

    fun encode() =
            when (size) {
                1 -> arrayOf(value.toByte()).toByteArray()
                3 -> ByteArray(3).apply {
                    this[0] = 0xFD.toByte()
                    value.toInt().writeToAsLE(this, bytes = 2)
                }
                5 -> ByteArray(5).apply {
                    this[0] = 0xFE.toByte()
                    value.toInt().writeToAsLE(this, offset = 1)
                }
                else -> ByteArray(9).apply {
                    this[0] = 0xFF.toByte()
                    value.writeToAsLE(this, offset = 1)
                }
            }

    companion object {
        fun of(from: ByteArray, offset: Int = 0): VarInt {
            val first = 0xFF and from[offset].toInt()
            val value = when {
                first < 0xFD -> first
                first == 0xFD -> readInt(from, offset = offset + 1, bytes = 2)
                first == 0xFE -> readInt(from, offset = offset + 1)
                else -> readLong(from, offset + 1).toInt()
            }
            return VarInt(value)
        }
    }
}
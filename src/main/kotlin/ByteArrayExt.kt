import java.math.BigInteger

const val MAP_BASE58 = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
val FIFTY_EIGHT: BigInteger by lazy { BigInteger.valueOf(58L) }

fun ByteArray.toHex() = StringBuilder().apply {
    for (i in this@toHex) {
        append(String.format("%02X", i))
    }
}.toString()

fun ByteArray.toBase58Check(): String {
    val hashed = Hash.applySHA256(this, 2)
    val extended = ByteArray(this.size + 4)
    System.arraycopy(this, 0, extended, 0, this.size)
    System.arraycopy(hashed, 0, extended, this.size, 4)
    return extended.toBase58()
}

fun ByteArray.toBase58(): String {
    var leadingZeroBytes = 0
    while (leadingZeroBytes < this.size && this[leadingZeroBytes] == 0.toByte()) {
        leadingZeroBytes++
    }

    var input = BigInteger(this)
    val output = StringBuilder()
    while (input > BigInteger.ZERO) {
        val results = input.divideAndRemainder(FIFTY_EIGHT)
        output.append(MAP_BASE58[results[1].toInt()])
        input = results[0]
    }
    for (i in leadingZeroBytes - 1 downTo 0) {
        output.append(MAP_BASE58[0])
    }
    return output.reverse().toString()
}
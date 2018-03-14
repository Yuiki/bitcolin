import java.math.BigInteger

const val MAP_BASE58 = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
val FIFTY_EIGHT: BigInteger = BigInteger.valueOf(58L)

fun ByteArray.toBase58Check(): String {
    val hashed = Hash.applySHA256(this, 2)
    val extended = ByteArray(this.size + 4)
    System.arraycopy(this, 0, extended, 0, this.size)
    System.arraycopy(hashed, 0, extended, this.size, 4)
    return extended.toBase58()
}

fun ByteArray.toBase58(): String =
        StringBuilder().apply {
            var input = BigInteger(this@toBase58)
            while (input > BigInteger.ZERO) {
                input.divideAndRemainder(FIFTY_EIGHT).also {
                    this.append(MAP_BASE58[it[1].toInt()])
                    input = it[0]
                }
            }

            // check leading zero
            (0 until this@toBase58.size)
                    .takeWhile { this@toBase58[it] == 0.toByte() }
                    .forEach { this.append(MAP_BASE58[0]) }
        }.reverse().toString()
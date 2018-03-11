import java.util.*

class ExtendedKey(seed: ByteArray) {
    val chainCode: ByteArray

    companion object {
        val key = "Bitcoin seed".toByteArray()
    }

    init {
        val hashedSeed = applyHmacSHA512(seed, key)
        val hashedLeft = Arrays.copyOfRange(hashedSeed, 0, 32)
        val hashedRight = Arrays.copyOfRange(hashedSeed, 32, 64)
        chainCode = hashedRight
    }
}
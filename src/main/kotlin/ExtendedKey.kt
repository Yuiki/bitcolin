import Hash.Companion.applyHmacSHA512
import java.io.ByteArrayOutputStream
import java.util.*

class ExtendedKey(testnet: Boolean, seed: ByteArray) {
    val chainCode: ByteArray
    private val depth: Int
    private val parentFingerprint: Int
    private val childNumber: Int
    private val keyPriv: ByteArray

    private val verPub = if (testnet) TPUB else XPUB
    private val verPriv = if (testnet) TPRIV else XPRIV

    companion object {
        val KEY_HASH = "Bitcoin seed".toByteArray()

        val XPUB = byteArrayOf(0x04, 0x88.toByte(), 0xB2.toByte(), 0x1E)
        val XPRIV = byteArrayOf(0x04, 0x88.toByte(), 0xAD.toByte(), 0xE4.toByte())
        val TPUB = byteArrayOf(0x04, 0x35, 0x87.toByte(), 0xCF.toByte())
        val TPRIV = byteArrayOf(0x04, 0x35, 0x83.toByte(), 0x94.toByte())
    }

    init {
        val hashedSeed = applyHmacSHA512(seed, KEY_HASH)
        val hashedLeft = Arrays.copyOfRange(hashedSeed, 0, 32)
        val hashedRight = Arrays.copyOfRange(hashedSeed, 32, 64)
        chainCode = hashedRight

        depth = 0
        parentFingerprint = 0
        childNumber = 0
        keyPriv = hashedLeft
    }

    val privKey: String by lazy { serialize(true, keyPriv) }

    private fun serialize(isPriv: Boolean, key: ByteArray): String =
            ByteArrayOutputStream().apply {
                write(if (isPriv) verPriv else verPub)
                write(depth and 0xFF)
                write(parentFingerprint ushr 24 and 0xFF)
                write(parentFingerprint ushr 16 and 0xFF)
                write(parentFingerprint ushr 8 and 0xFF)
                write(parentFingerprint and 0xFF)
                write(childNumber ushr 24 and 0xFF)
                write(childNumber ushr 16 and 0xFF)
                write(childNumber ushr 8 and 0xFF)
                write(childNumber and 0xFF)
                write(chainCode)
                if (isPriv) write(0x00)
                write(key)
            }.toByteArray().toBase58Check()
}
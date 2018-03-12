import java.io.ByteArrayOutputStream
import java.util.*

class ExtendedKey(private val isTestNet: Boolean,
                  hashedKey: ByteArray,
                  private val childNumber: Int = 0,
                  private val depth: Int = 0,
                  parentECKey: ECKey? = null,
                  private val parentFingerprint: Int = 0) {
    val chainCode: ByteArray
    private val ecKey: ECKey

    private val verPub = if (isTestNet) TPUB else XPUB
    private val verPriv = if (isTestNet) TPRIV else XPRIV

    companion object {
        val XPUB = byteArrayOf(0x04, 0x88.toByte(), 0xB2.toByte(), 0x1E)
        val XPRIV = byteArrayOf(0x04, 0x88.toByte(), 0xAD.toByte(), 0xE4.toByte())
        val TPUB = byteArrayOf(0x04, 0x35, 0x87.toByte(), 0xCF.toByte())
        val TPRIV = byteArrayOf(0x04, 0x35, 0x83.toByte(), 0x94.toByte())

        fun fromPath(root: ExtendedKey, path: String): ExtendedKey {
            path.split("/").let {
                if (it.size != 4) throw IllegalArgumentException("Invalid derivation path")
                val account = it[1].toInt()
                val change = it[2].toInt()
                val index = it[3].toInt()

                return root.derive(account).derive(change).derive(index)
            }
        }
    }

    init {
        val hashedKeyLeft = Arrays.copyOfRange(hashedKey, 0, 32)
        val hashedKeyRight = Arrays.copyOfRange(hashedKey, 32, 64)
        chainCode = hashedKeyRight
        ecKey = if (parentECKey != null) ECKey(hashedKeyLeft, parentECKey) else ECKey(hashedKeyLeft)
    }

    val privKey: String = serialize(true, ecKey.privKey)
    val pubKey: String = serialize(false, ecKey.pubKey)

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

    private fun derive(index: Int): ExtendedKey {
        val parentPubKey = ecKey.pubKey
        val childKey = ByteArray(parentPubKey.size + 4)
        System.arraycopy(parentPubKey, 0, childKey, 0, parentPubKey.size)
        childKey[parentPubKey.size] = (index ushr 24 and 0xFF).toByte()
        childKey[parentPubKey.size + 1] = (index ushr 16 and 0xFF).toByte()
        childKey[parentPubKey.size + 2] = (index ushr 8 and 0xFF).toByte()
        childKey[parentPubKey.size + 3] = (index and 0xFF).toByte()
        val hashedChildKey = Hash.applyHmacSHA512(childKey, chainCode)
        return ExtendedKey(isTestNet, hashedChildKey, index, depth + 1, ecKey, ecKey.fingerprint)
    }
}
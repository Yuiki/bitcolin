import Hash.Companion.applyHmacSHA512
import java.io.ByteArrayOutputStream
import java.util.*

class ExtendedKey(hashedKey: ByteArray,
                  private val isTestNet: Boolean = false,
                  private val childNumber: Int = 0,
                  private val depth: Int = 0,
                  parentECKey: ECKey? = null) {
    private val chainCode: ByteArray
    private val ecKey: ECKey
    private val parentFingerprint: Int

    private val verPub = if (isTestNet) TPUB else XPUB
    private val verPriv = if (isTestNet) TPRIV else XPRIV

    companion object {
        private val XPUB = byteArrayOf(0x04, 0x88.toByte(), 0xB2.toByte(), 0x1E)
        private val XPRIV = byteArrayOf(0x04, 0x88.toByte(), 0xAD.toByte(), 0xE4.toByte())
        private val TPUB = byteArrayOf(0x04, 0x35, 0x87.toByte(), 0xCF.toByte())
        private val TPRIV = byteArrayOf(0x04, 0x35, 0x83.toByte(), 0x94.toByte())

        private val KEY_HASH = "Bitcoin seed".toByteArray()

        fun fromSeed(seed: ByteArray) = ExtendedKey(hashedKey = applyHmacSHA512(seed, KEY_HASH))

        fun fromRootByPath(root: ExtendedKey, path: String): ExtendedKey {
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
        if (parentECKey != null) {
            ecKey = ECKey(hashedKeyLeft, parentECKey)
            parentFingerprint = parentECKey.fingerprint
        } else {
            ecKey = ECKey(hashedKeyLeft)
            parentFingerprint = 0
        }
    }

    val privKey: String = serialize(true)
    val pubKey: String = serialize(false)

    private fun serialize(isPriv: Boolean): String =
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
                write(if (isPriv) ecKey.privKey else ecKey.pubKey)
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
        return ExtendedKey(hashedChildKey, isTestNet, index, depth + 1, ecKey)
    }
}
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.MessageDigest
import java.security.Security
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class Hash {
    companion object {
        private const val HMAC_SHA512 = "HmacSHA512"
        private const val SHA256 = "SHA-256"
        private const val PROVIDER = "BC"

        init {
            Security.addProvider(BouncyCastleProvider())
        }

        fun applyHmacSHA512(data: ByteArray, key: ByteArray): ByteArray {
            val keySpec = SecretKeySpec(key, HMAC_SHA512)
            val mac = Mac.getInstance(HMAC_SHA512, PROVIDER).apply { init(keySpec) }
            return mac.doFinal(data)
        }

        fun applySHA256(data: ByteArray, round: Int): ByteArray {
            val md = MessageDigest.getInstance(SHA256)
            var digested = data
            for (i in 0 until round) {
                md.update(digested)
                digested = md.digest()
            }
            return digested
        }
    }
}
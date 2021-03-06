import org.bouncycastle.crypto.digests.RIPEMD160Digest
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

        fun applyHmacSHA512(data: ByteArray, key: ByteArray): ByteArray =
                Mac.getInstance(HMAC_SHA512, PROVIDER)
                        .apply { init(SecretKeySpec(key, HMAC_SHA512)) }
                        .doFinal(data)

        fun applySHA256(data: ByteArray, round: Int = 1): ByteArray {
            val md = MessageDigest.getInstance(SHA256)
            var digested = data
            for (i in 0 until round) {
                md.update(digested)
                digested = md.digest()
            }
            return digested
        }

        fun applyHash160(data: ByteArray): ByteArray {
            val result = ByteArray(20)
            val applied = applySHA256(data)
            RIPEMD160Digest().apply {
                update(applied, 0, applied.size)
                doFinal(result, 0)
            }
            return result
        }
    }
}
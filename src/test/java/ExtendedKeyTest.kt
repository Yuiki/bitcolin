import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.security.Security

class ExtendedKeyTest {
    companion object {
        @JvmStatic
        @BeforeAll
        fun init() {
            Security.addProvider(BouncyCastleProvider())
        }
    }

    @Test
    fun testExtendedKey() {
        val passPhrase = "check this one *&##X87 check this 2 this passphrase 593 is tough to crack&@!!"
        val round = 50000
        val hashedPassPhrase = applySHA256(passPhrase.toByteArray(), round)
        val chainCode = ExtendedKey(hashedPassPhrase).chainCode
        Assertions.assertTrue(chainCode.toHex().toLowerCase() == "a24a2cae513d6c70c4076d685c56945b565810ee70bdb96198a2b60b83e1e7ba".toLowerCase())
    }
}
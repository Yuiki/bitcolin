import Hash.Companion.applyHmacSHA512
import Hash.Companion.applySHA256
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ExtendedKeyTest {
    @Test
    fun testExtendedKey() {
        val passPhrase = "check this one *&##X87 check this 2 this passphrase 593 is tough to crack&@!!"
        val round = 50000
        val hashedPassPhrase = applySHA256(passPhrase.toByteArray(), round)
        val hashKey = "Bitcoin seed".toByteArray()
        val hashedKey = applyHmacSHA512(hashedPassPhrase, hashKey)
        val key = ExtendedKey(false, hashedKey)
        val chainCode = key.chainCode
        Assertions.assertTrue(chainCode.toHex().toLowerCase() == "a24a2cae513d6c70c4076d685c56945b565810ee70bdb96198a2b60b83e1e7ba".toLowerCase())
        val privKey = key.privKey
        Assertions.assertTrue(privKey == "xprv9s21ZrQH143K3g56LSYgKiKx4LD4GJh27DUXRqShDpn2cAD4EAPbVn5T9BqqcGswHzcrztzJEJMHpS7wstai53cS6esPPkRGjp4voMhXrTP")
        val pubKey = key.pubKey
        Assertions.assertTrue(pubKey == "HarqLWPftSVzTwufETzTJ6jCT7jXuUxVDzzNg2t9qiUHME3687vYBVTyTLAFjH4exdNYCbFtGnzvgWHnCJSFA2jkCa7zj8hCnJSZv9yukLsmHGzFFXtYUYoXZGHS5SA8DDq6QS4D3gzi3UoDYMi7J4E2q9h")

        val path = "m/0/0/3"
        val childKey = ExtendedKey.fromPath(key, path)
        val childPrivKey = childKey.privKey
        Assertions.assertTrue(childPrivKey == "xprv9yRjeYDqF5X9XjLDomcEfK9QPZ3JGLcdaR6jaAeFnJ4a9Nem3aH4EzuZhh54pfg2XXGTYL6oCoHLHtnZa6Quqe5D1TLnhgYgbeoY6EfgeWN")
    }
}
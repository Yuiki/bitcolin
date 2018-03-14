import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ExtendedKeyTest {
    @Test
    fun testExtendedKey() {
        val seed = byteArrayOf(0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f)
        val key = ExtendedKey.fromSeed(seed)

        val actualPrivKey = key.privKey
        val expectedPrivKey = "xprv9s21ZrQH143K3QTDL4LXw2F7HEK3wJUD2nW2nRk4stbPy6cq3jPPqjiChkVvvNKmPGJxWUtg6LnF5kejMRNNU3TGtRBeJgk33yuGBxrMPHi"
        assertTrue(actualPrivKey == expectedPrivKey)

        val actualPubKey = key.pubKey
        val expectedPubKey = "xpub661MyMwAqRbcFtXgS5sYJABqqG9YLmC4Q1Rdap9gSE8NqtwybGhePY2gZ29ESFjqJoCu1Rupje8YtGqsefD265TMg7usUDFdp6W1EGMcet8"
        assertTrue(actualPubKey == expectedPubKey)

        val childKey = ExtendedKey.fromRootByPath(key, path = "m/2147483648")

        val actualChildPrivKey = childKey.privKey
        val expectedChildPrivKey = "xprv9uHRZZhk6KAJC1avXpDAp4MDc3sQKNxDiPvvkX8Br5ngLNv1TxvUxt4cV1rGL5hj6KCesnDYUhd7oWgT11eZG7XnxHrnYeSvkzY7d2bhkJ7"
        assertTrue(actualChildPrivKey == expectedChildPrivKey)

        val actualChildPubKey = childKey.pubKey
        val expectedChildPubKey = "xpub68Gmy5EdvgibQVfPdqkBBCHxA5htiqg55crXYuXoQRKfDBFA1WEjWgP6LHhwBZeNK1VTsfTFUHCdrfp1bgwQ9xv5ski8PX9rL2dZXvgGDnw"
        assertTrue(actualChildPubKey == expectedChildPubKey)
    }
}
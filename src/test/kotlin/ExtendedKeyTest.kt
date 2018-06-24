import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import javax.xml.bind.DatatypeConverter

class ExtendedKeyTest {
    @Test
    fun testExtendedKey1() {
        val seed = DatatypeConverter.parseHexBinary("000102030405060708090a0b0c0d0e0f")
        val key = ExtendedKey.fromSeed(seed)
        val childKey = ExtendedKey.fromRootByPath(key, path = "m/0'/1/2'/2/1000000000")

        val actualChildPrivKey = childKey.privKey
        val expectedChildPrivKey = "xprvA41z7zogVVwxVSgdKUHDy1SKmdb533PjDz7J6N6mV6uS3ze1ai8FHa8kmHScGpWmj4WggLyQjgPie1rFSruoUihUZREPSL39UNdE3BBDu76"
        assertTrue(actualChildPrivKey == expectedChildPrivKey)

        val actualChildPubKey = childKey.pubKey
        val expectedChildPubKey = "xpub6H1LXWLaKsWFhvm6RVpEL9P4KfRZSW7abD2ttkWP3SSQvnyA8FSVqNTEcYFgJS2UaFcxupHiYkro49S8yGasTvXEYBVPamhGW6cFJodrTHy"
        assertTrue(actualChildPubKey == expectedChildPubKey)
    }

    @Test
    fun testExtendedKey2() {
        val seed = DatatypeConverter.parseHexBinary("fffcf9f6f3f0edeae7e4e1dedbd8d5d2cfccc9c6c3c0bdbab7b4b1aeaba8a5a29f9c999693908d8a8784817e7b7875726f6c696663605d5a5754514e4b484542")
        val key = ExtendedKey.fromSeed(seed)
        val childKey = ExtendedKey.fromRootByPath(key, path = "m/0/2147483647'/1/2147483646'/2")

        val actualChildPrivKey = childKey.privKey
        val expectedChildPrivKey = "xprvA2nrNbFZABcdryreWet9Ea4LvTJcGsqrMzxHx98MMrotbir7yrKCEXw7nadnHM8Dq38EGfSh6dqA9QWTyefMLEcBYJUuekgW4BYPJcr9E7j"
        assertTrue(actualChildPrivKey == expectedChildPrivKey)

        val actualChildPubKey = childKey.pubKey
        val expectedChildPubKey = "xpub6FnCn6nSzZAw5Tw7cgR9bi15UV96gLZhjDstkXXxvCLsUXBGXPdSnLFbdpq8p9HmGsApME5hQTZ3emM2rnY5agb9rXpVGyy3bdW6EEgAtqt"
        assertTrue(actualChildPubKey == expectedChildPubKey)
    }

    @Test
    fun testExtendedKey3() {
        val seed = DatatypeConverter.parseHexBinary("4b381541583be4423346c643850da4b320e46a87ae3d2a4e6da11eba819cd4acba45d239319ac14f863b8d5ab5a0d0c64d2e8a1e7d1457df2e5a3c51c73235be")
        val key = ExtendedKey.fromSeed(seed)
        val childKey = ExtendedKey.fromRootByPath(key, path = "m/0'")

        val actualChildPrivKey = childKey.privKey
        val expectedChildPrivKey = "xprv9uPDJpEQgRQfDcW7BkF7eTya6RPxXeJCqCJGHuCJ4GiRVLzkTXBAJMu2qaMWPrS7AANYqdq6vcBcBUdJCVVFceUvJFjaPdGZ2y9WACViL4L"
        assertTrue(actualChildPrivKey == expectedChildPrivKey)

        val actualChildPubKey = childKey.pubKey
        val expectedChildPubKey = "xpub68NZiKmJWnxxS6aaHmn81bvJeTESw724CRDs6HbuccFQN9Ku14VQrADWgqbhhTHBaohPX4CjNLf9fq9MYo6oDaPPLPxSb7gwQN3ih19Zm4Y"
        assertTrue(actualChildPubKey == expectedChildPubKey)
    }
}
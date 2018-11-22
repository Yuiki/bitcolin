import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AddressTest {
    @Test
    fun convertHashedPubKeyToAddress() {
        val hashedPubKey = "f54a5851e9372b87810a8e60cdd2e7cfd80b6e31".toByteArrayFromHex()
        val addr = Address(hashedPubKey = hashedPubKey, isTestNet = false)

        val actual = addr.toString()
        val expected = "1PMycacnJaSqwwJqjawXBErnLsZ7RkXUAs"

        Assertions.assertEquals(expected, actual)
    }
}
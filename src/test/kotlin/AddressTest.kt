import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.xml.bind.DatatypeConverter

class AddressTest {
    @Test
    fun convertHashedPubKeyToAddress() {
        val hashedPubKey = DatatypeConverter.parseHexBinary("f54a5851e9372b87810a8e60cdd2e7cfd80b6e31")
        val addr = Address(hashedPubKey = hashedPubKey, isTestNet = false)

        val actual = addr.toString()
        val expected = "1PMycacnJaSqwwJqjawXBErnLsZ7RkXUAs"

        Assertions.assertEquals(expected, actual)
    }
}
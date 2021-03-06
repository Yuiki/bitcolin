import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ByteArrayExtTest {
    @Test
    fun testByteArrayToBase58() {
        val input = "Bitcoin".toByteArray()
        val expected = "3WyEDWjcVB"
        val actual = input.toBase58()
        assertTrue(actual == expected)
    }

    @Test
    fun testByteArrayToBase58Check() {
        val input = "Bitcoin".toByteArray()
        val expected = "HUB1bxjWuH3izZw"
        val actual = input.toBase58Check()
        assertTrue(actual == expected)
    }
}
import java.io.ByteArrayOutputStream
import java.io.OutputStream

abstract class Message(
        val command: String,
        var length: Int = -1
) {
    fun serialize(): ByteArray {
        val raw = ByteArrayOutputStream().apply {
            this@Message.writeTo(this)
        }.toByteArray()
        return ByteArray(raw.size).apply {
            System.arraycopy(raw, 0, this, 0, raw.size)
        }
    }

    abstract fun writeTo(target: OutputStream)
}
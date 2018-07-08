import java.io.OutputStream
import java.nio.BufferUnderflowException

class PingMessage(
        val nonce: Long?
) : Message(command = "ping") {
    override fun writeTo(target: OutputStream) {}

    companion object {
        fun parse(payload: ByteArray): PingMessage {
            val nonce = try {
                readLong(payload)
            } catch (e: BufferUnderflowException) {
                null
            }
            return PingMessage(nonce)
        }
    }
}
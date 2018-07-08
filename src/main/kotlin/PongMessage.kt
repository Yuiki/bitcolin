import java.io.OutputStream

class PongMessage(val nonce: Long?) : Message("pong") {
    override fun writeTo(target: OutputStream) {
        nonce?.writeToAsLE(target)
    }
}
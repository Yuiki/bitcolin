import java.io.OutputStream

class VerAckMessage: Message(command = "verack", length = 0) {
    override fun writeTo(target: OutputStream) {}
}
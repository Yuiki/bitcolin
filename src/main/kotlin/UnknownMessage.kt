import java.io.OutputStream

object UnknownMessage : Message(
        command = "unknown"
) {
    override fun writeTo(target: OutputStream) {}
}
import java.io.OutputStream
import java.net.InetAddress

class VersionMessage(
        val startHeight: Int
) : Message(command = "version") {
    val version = 70001
    val services = 0L
    val timestamp = System.currentTimeMillis() / 1000
    val addrRecv = NetworkAddress(
            ip = InetAddress.getLocalHost(),
            port = 8333,
            services = 0L
    )
    val addrFrom = addrRecv
    val nonce = 0L
    val userAgent = "/bitcolin:0.1.0/"
    val relay = false

    init {
        length = 85 + VarInt(userAgent.length).size + userAgent.length
    }

    override fun writeTo(target: OutputStream) {
        version.writeToAsLE(target)
        services.writeToAsLE(target)
        timestamp.writeToAsLE(target)
        addrRecv.writeTo(target)
        addrFrom.writeTo(target)
        nonce.writeToAsLE(target)
        val userAgentBytes = userAgent.toByteArray()
        target.write(VarInt(userAgentBytes.size).encode())
        target.write(userAgentBytes)
        startHeight.writeToAsLE(target)
        target.write(if (relay) 1 else 0)
    }
}
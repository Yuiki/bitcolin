import java.io.OutputStream
import java.net.InetAddress

class VersionMessage(
        val version: Int,
        val services: Long,
        val timestamp: Long,
        val addrRecv: NetworkAddress,
        val addrFrom: NetworkAddress,
        val nonce: Long,
        val userAgent: String,
        val startHeight: Int,
        val relay: Boolean
) : Message(command = "version") {
    constructor(startHeight: Int) : this(
            version = 70001,
            services = 0,
            timestamp = System.currentTimeMillis() / 1000,
            addrRecv = NetworkAddress(
                    ip = InetAddress.getLocalHost(),
                    port = 8333,
                    services = 0
            ),
            addrFrom = NetworkAddress(
                    ip = InetAddress.getLocalHost(),
                    port = 8333,
                    services = 0
            ),
            nonce = 0,
            userAgent = "/bitcolin:0.1.0/",
            startHeight = startHeight,
            relay = false
    )

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

    companion object {
        fun parse(payload: ByteArray): VersionMessage {
            val version = readInt(payload)
            val services = readLong(payload, offset = 4)
            val timestamp = readLong(payload, offset = 12)
            val addrFrom = NetworkAddress.parse(payload, offset = 20)
            val addrRecv = NetworkAddress.parse(payload, offset = 46)
            val nonce = readLong(payload, offset = 72)
            val (uaOffset, userAgent) = readString(payload, offset = 80)
            val startHeight = readInt(payload, offset = 80 + uaOffset)
            val relay = readBytes(payload, offset = 84 + uaOffset, length = 1)[0].toInt() != 0

            return VersionMessage(
                    version = version,
                    services = services,
                    timestamp = timestamp,
                    addrRecv = addrRecv,
                    addrFrom = addrFrom,
                    nonce = nonce,
                    userAgent = userAgent,
                    startHeight = startHeight,
                    relay = relay)
        }
    }
}
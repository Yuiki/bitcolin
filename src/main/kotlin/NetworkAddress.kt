import java.io.OutputStream
import java.net.InetAddress

class NetworkAddress(
        val services: Long,
        val ip: InetAddress,
        val port: Int
) : ChildMessage(length = 30) {
    override fun writeTo(target: OutputStream) {
        services.writeToAsLE(target)

        // convert address to IPv6 if it is ipv4
        val ipAddr = ip.address.let {
            if (it.size == 4) {
                return@let ByteArray(16).apply {
                    System.arraycopy(it, 0, this, 12, 4)
                    this[10] = 0xFF.toByte()
                    this[11] = 0xFF.toByte()
                }
            }
            it
        }
        target.write(ipAddr)

        port.writeToAsLE(target, bytes = 2)
    }
}
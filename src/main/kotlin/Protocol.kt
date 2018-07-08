import java.io.ByteArrayOutputStream
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

class Protocol {
    val magic = 0x0B110907 /* TODO: MainNet */

    fun connect() {
        val version = VersionMessage(startHeight = 0)
        val target = InetAddress.getByName("testnet-seed.bitcoin.jonasschnelli.ch")
        val channel = SocketChannel.open().apply {
            connect(InetSocketAddress(target, 18333))
        }

        sendMessage(message = version, channel = channel)

        val readBuff = ByteBuffer.allocateDirect(65536)
        val read = channel.read(readBuff)
        println(read)
    }

    private fun sendMessage(message: Message, channel: SocketChannel) {
        val out = ByteArrayOutputStream()
        val header = ByteArray(24)
        magic.writeToAsBE(header)
        for (i in 0 until message.command.length) {
            header[4 + i] = (message.command.codePointAt(i) and 0xFF).toByte()
        }
        message.length.writeToAsLE(header, offset = 4 + 12)
        val payload = message.serialize()
        val hash = Hash.applySHA256(payload, round = 2)
        System.arraycopy(hash, 0, header, 4 + 12 + 4, 4)
        out.write(header)
        out.write(payload)
        channel.write(ByteBuffer.wrap(out.toByteArray()))
    }
}
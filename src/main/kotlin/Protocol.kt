import java.io.ByteArrayOutputStream
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.nio.charset.Charset

class Protocol {
    val magic = 0x0B110907 /* TODO: MainNet */

    fun connect() {
        val target = InetAddress.getByName("testnet-seed.bitcoin.jonasschnelli.ch")
        val channel = SocketChannel.open().apply {
            connect(InetSocketAddress(target, 18333))
        }
        handshake(channel)
    }

    private fun handshake(channel: SocketChannel) {
        // send version
        val version = VersionMessage(startHeight = 0)
        sendMessage(message = version, channel = channel)
        println("sent version")

        // recv version
        receiveMessage(channel).also {
            println("recv " + it.command)
        }

        // send verack
        sendMessage(message = VerAckMessage(), channel = channel)
        println("sent verack")

        while (true) {
            val message = receiveMessage(channel).also {
                println(it.command)
            }
            // recv verack
            if (message.command == "verack") return
        }
    }

    private fun receiveMessage(channel: SocketChannel): Message {
        val readBuff = ByteBuffer.allocateDirect(131072)
        channel.read(readBuff)
        readBuff.flip()

        // seek magic bytes
        var magicCursor = 3
        while (magicCursor > -1) {
            val expectedByte = (0xFF and (magic shr (magicCursor * 8))).toByte()
            val actualByte = readBuff.get()
            if (actualByte == expectedByte) {
                magicCursor--
            } else {
                magicCursor = 3
            }
        }

        val header = ByteArray(20).apply {
            readBuff.get(this, 0, this.size)
        }

        var commandLen = 0
        for (i in 0..11) {
            if (header[i] == 0.toByte()) {
                commandLen = i
                break
            }
        }
        val command = ByteArray(commandLen).let {
            System.arraycopy(header, 0, it, 0, commandLen)
            String(it, Charset.forName("US-ASCII"))
        }

        val length = readInt(header, 12)

        val checksum = ByteArray(4).apply {
            System.arraycopy(header, 16, this, 0, 4)
        }

        println(length)
        println(readBuff.remaining())

        val payload = ByteArray(length).apply {
            readBuff.get(this, 0, length)
        }

        // verify checksum
        val hash = Hash.applySHA256(payload, round = 2).take(4).toByteArray()
        if (!checksum.contentEquals(hash)) {
            throw RuntimeException("Checksum invalid")
        }

        return when (command) {
            "version" -> VersionMessage.parse(payload)
            "verack" -> VerAckMessage()
            else -> UnknownMessage
        }
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
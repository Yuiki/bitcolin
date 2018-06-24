import java.io.ByteArrayOutputStream

class Address(
        private val hashedPubKey: ByteArray,
        isTestNet: Boolean
) {
    private val versionPrefix = if (isTestNet) 0x6F else 0x0

    override fun toString() =
            ByteArrayOutputStream().apply {
                write(versionPrefix)
                write(hashedPubKey)
            }.toByteArray().toBase58Check()
}
fun String.toByteArrayFromHex(): ByteArray {
    if (length % 2 != 0) {
        throw IllegalArgumentException("$this must be even-length.")
    }

    return ByteArray(length / 2).apply {
        for (i in 0 until length step 2) {
            val h = this@toByteArrayFromHex[i].toBinFromHex()
            val l = this@toByteArrayFromHex[i + 1].toBinFromHex()
            if (h == -1 || l == -1) {
                throw IllegalArgumentException("$this contains illegal char.")
            }

            this[i / 2] = (h * 16 + l).toByte()
        }
    }
}
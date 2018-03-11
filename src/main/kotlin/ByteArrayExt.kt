fun ByteArray.toHex() = StringBuilder().apply {
    for (i in this@toHex) {
        append(String.format("%02X", i))
    }
}.toString()
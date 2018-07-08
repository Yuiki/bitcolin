import java.io.OutputStream

fun Int.writeToAsLE(target: OutputStream, bytes: Int = 4) {
    val out = ByteArray(size = bytes)
    writeToAsLE(out = out, bytes = bytes)
    target.write(out)
}

fun Int.writeToAsLE(out: ByteArray, bytes: Int = 4, offset: Int = 0): ByteArray {
    return writeTo(isLE = true, out = out, bytes = bytes, offset = offset)
}

fun Int.writeToAsBE(out: ByteArray, offset: Int = 0): ByteArray {
    return writeTo(isLE = false, out = out, offset = offset)
}

fun Int.writeTo(isLE: Boolean, out: ByteArray, bytes: Int = 4, offset: Int = 0): ByteArray {
    val range = (if (isLE) 0..(bytes - 1) * 8 else (bytes - 1) * 8 downTo 0) step 8
    for ((idx, value) in range.withIndex()) {
        out[offset + idx] = (0xFF and (this shr value)).toByte()
    }
    return out
}

fun Long.writeToAsLE(target: OutputStream) {
    val out = ByteArray(size = 8)
    writeToAsLE(out = out)
    target.write(out)
}

fun Long.writeToAsLE(out: ByteArray, offset: Int = 0): ByteArray {
    for ((idx, value) in (0..56 step 8).withIndex()) {
        out[offset + idx] = (0xFFL and (this shr value)).toByte()
    }
    return out
}
import java.io.OutputStream
import java.math.BigInteger
import java.nio.charset.Charset

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

fun readInt(from: ByteArray, offset: Int = 0, bytes: Int = 4) =
        (0..(bytes - 1) * 8 step 8).foldIndexed(0) { idx: Int, acc: Int, i: Int ->
            acc or (from[offset + idx].toInt() shl i)
        }

fun readLong(from: ByteArray, offset: Int = 0) =
        BigInteger(reverse(readBytes(from, offset = offset, length = 8))).longValueExact()

fun readBytes(from: ByteArray, offset: Int = 0, length: Int) =
        ByteArray(length).apply {
            System.arraycopy(from, offset, this, 0, length)
        }

fun readVarInt(from: ByteArray, offset: Int = 0): VarInt {
    return VarInt.of(from, offset = offset)
}

fun readString(from: ByteArray, offset: Int = 0): Pair<Int, String> {
    val varInt = readVarInt(from, offset = offset)
    val len = varInt.value.toInt()
    return Pair(varInt.size + len, String(readBytes(from, offset = offset + varInt.size, length = len), Charset.forName("UTF-8")))
}

fun reverse(source: ByteArray) =
        ByteArray(source.size).apply {
            for (i in 0 until source.size) {
                this[i] = source[source.size - 1 - i]
            }
        }
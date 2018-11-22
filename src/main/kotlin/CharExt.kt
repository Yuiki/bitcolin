fun Char.toBinFromHex(): Int {
    return when {
        this in '0'..'9' -> this - '0'
        this in 'A'..'F' -> this - 'A' + 10
        this in 'a'..'f' -> this - 'a' + 10
        else -> -1
    }
}
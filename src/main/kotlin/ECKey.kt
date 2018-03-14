import org.bouncycastle.asn1.sec.SECNamedCurves
import org.bouncycastle.asn1.x9.X9ECParameters
import java.math.BigInteger

class ECKey {
    var privKey: ByteArray
        private set
    lateinit var pubKey: ByteArray
        private set
    lateinit var hashedPubKey: ByteArray
        private set

    val fingerprint by lazy {
        var fingerprint = 0
        val hashedPubKey = hashedPubKey
        for (i in 0..3) {
            fingerprint = fingerprint shl 8
            fingerprint = fingerprint or (hashedPubKey[i].toInt() and 0xff)
        }
        fingerprint
    }

    constructor(key: ByteArray) {
        privKey = key
        initPubKey()
    }

    constructor(key: ByteArray, ecKey: ECKey) {
        privKey = BigInteger(1, key).add(BigInteger(1, ecKey.privKey)).mod(secp256K1.n).toByteArray()
        initPubKey()
    }

    private fun initPubKey() {
        pubKey = secp256K1.g.multiply(BigInteger(1, privKey)).getEncoded(true)
        hashedPubKey = Hash.applyHash160(pubKey)
    }

    companion object {
        val secp256K1: X9ECParameters = SECNamedCurves.getByName("secp256k1")
    }
}
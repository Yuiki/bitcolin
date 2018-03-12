import org.bouncycastle.asn1.sec.SECNamedCurves
import org.bouncycastle.asn1.x9.X9ECParameters
import java.math.BigInteger

class ECKey(val privKey: ByteArray) {
    val pubKey: ByteArray = secp256K1.g.multiply(BigInteger(1, privKey)).getEncoded(false)

    companion object {
        val secp256K1: X9ECParameters = SECNamedCurves.getByName("secp256k1")
    }
}
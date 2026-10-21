package cl.corpaliv.albalab.util

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

// Solo para el MVP local: nunca guardamos la contraseña en texto plano.
object Seguridad {
    private const val ITERACIONES = 120000
    fun generarSal(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    fun hash(clave: String, salBase64: String): String {
        val sal = Base64.decode(salBase64, Base64.NO_WRAP)
        val spec = PBEKeySpec(clave.toCharArray(), sal, ITERACIONES, 256)
        return try {
            val resultado = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(spec).encoded
            Base64.encodeToString(resultado, Base64.NO_WRAP)
        } finally {
            spec.clearPassword()
        }
    }

    fun verificar(clave: String, sal: String, hashGuardado: String): Boolean =
        MessageDigest.isEqual(
            Base64.decode(hash(clave, sal), Base64.NO_WRAP),
            Base64.decode(hashGuardado, Base64.NO_WRAP)
        )
}

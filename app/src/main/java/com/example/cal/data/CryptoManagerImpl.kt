package com.example.cal.data

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import com.example.cal.domain.CryptoManager
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptoManagerImpl @Inject constructor() : CryptoManager {

    companion object {
        private const val KEY_ALIAS = "my_encryption_key"
    }

    override fun encryptMessage(message: String): String? {
        try {
            val secretKey = getOrCreateSecretKey()
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)

            val iv = cipher.iv
            val encryptedBytes = cipher.doFinal(message.toByteArray())

            val ivAndCiphertext = iv + encryptedBytes
            return Base64.encodeToString(ivAndCiphertext, Base64.DEFAULT)

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    // Decrypt the message
    override fun decryptMessage(encryptedMessage: String): String? {
        try {
            val ivAndCiphertext = Base64.decode(encryptedMessage, Base64.DEFAULT)

            val iv = ivAndCiphertext.copyOfRange(0, 12)
            val ciphertext = ivAndCiphertext.copyOfRange(12, ivAndCiphertext.size)

            val secretKey = getOrCreateSecretKey()
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val gcmSpec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec)

            val decryptedBytes = cipher.doFinal(ciphertext)
            return String(decryptedBytes)

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    // Generate or retrieve the AES key from the Android Keystore
    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            )
            keyGenerator.generateKey()
        }

        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }
}

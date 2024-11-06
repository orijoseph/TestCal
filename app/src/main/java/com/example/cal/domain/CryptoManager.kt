package com.example.cal.domain

interface CryptoManager {
    fun encryptMessage(message: String): String?
    fun decryptMessage(encryptedMessage: String): String?
}
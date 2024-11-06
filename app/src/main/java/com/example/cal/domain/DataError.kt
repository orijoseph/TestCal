package com.example.cal.domain

sealed interface DataError: Error {

    enum class Network: DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERIALIZATION,
        UNKNOWN
    }
    enum class Crypto: DataError {
        ENCRYPTION,
        DECRYPTION
    }
}

package com.example.jaimequeraltgarrigos.spotify_artist.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class ErrorHandlerHelperKtTest {
    @Test
    fun testErrorHandlerHelper() {
        val iothrowable = IOException()
        assertEquals(asNetworkException(iothrowable), "No Internet Connection")
        val throwable = Throwable()
        assertEquals(asNetworkException(throwable), "Something went wrong...")
    }
}
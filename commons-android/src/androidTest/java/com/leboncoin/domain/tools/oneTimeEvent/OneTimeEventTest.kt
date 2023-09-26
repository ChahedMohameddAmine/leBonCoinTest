package com.leboncoin.domain.tools.oneTimeEvent

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import org.junit.Before
import org.junit.Test

class OneTimeEventTest{

    private lateinit var oneTimeEvent: OneTimeEvent<String>

    @Before
    fun setUp() {
        oneTimeEvent = OneTimeEvent("TestPayload")
    }

    @Test
    fun getValue_returnsPayloadIfNotConsumed() {
        val result = oneTimeEvent.payload
        assertEquals("TestPayload", result)
        assertFalse(oneTimeEvent.isConsumed())
    }

    @Test
    fun getValue_ReturnsNullIfConsumed() {
        oneTimeEvent.resetEvent()
        var result :String? = null
        oneTimeEvent.consumeOnce {}
        oneTimeEvent.consumeOnce {
             result = oneTimeEvent.payload
        } // Consume it once

        assertNull(result)
        assertTrue(oneTimeEvent.isConsumed())
    }

    @Test
    fun consumeOnce_invokesTheBlockWithPayloadIfNotConsumed() {
        var result: String? = null
        oneTimeEvent.consumeOnce {
            result = it
        }
        assertEquals("TestPayload", result)
        assertTrue(oneTimeEvent.isConsumed())
    }

    @Test
    fun consumeOnce_DoesNotInvokeTheBlockIfConsumed() {
        oneTimeEvent.resetEvent()
        oneTimeEvent.consumeOnce {}
        oneTimeEvent.consumeOnce {
            fail("Block should not be invoked")
        }
        assertTrue(oneTimeEvent.isConsumed())
    }

    @Test
    fun consumeOnce_InvokeTheBlockIfReset() {
        oneTimeEvent.consumeOnce {}
        oneTimeEvent.resetEvent()
        var result :String?= null
        oneTimeEvent.consumeOnce {
            result = it
        }
        assertNotNull(result)
        assertTrue(oneTimeEvent.isConsumed())
    }

    // Add more tests for coConsumeOnce if needed
}


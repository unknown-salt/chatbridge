package com.chatbridge.utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ChatFormatterSmokeTest {
    @Test
    fun guildPatternMatchesAndKeepsGroupIndexes() {
        val match = ChatFormatter.GUILD_PATTERN.matcher("Guild > [MVP+] Gygi4 §3[OFFICER]: hello world")
        assertTrue(match.matches())
        assertEquals("[MVP+] ", match.group(1))
        assertEquals("Gygi4", match.group(2))
        assertEquals("OFFICER", match.group(3))
        assertEquals("hello world", match.group(4))
    }

    @Test
    fun officerPatternMatchesAndKeepsGroupIndexes() {
        val match = ChatFormatter.OFFICER_PATTERN.matcher("Officer > [MVP++] Gygi4 §3[LEADER]: announce")
        assertTrue(match.matches())
        assertEquals("[MVP++] ", match.group(1))
        assertEquals("Gygi4", match.group(2))
        assertEquals("LEADER", match.group(3))
        assertEquals("announce", match.group(4))
    }

    @Test
    fun partyPatternMatchesAndKeepsGroupIndexes() {
        val match = ChatFormatter.PARTY_PATTERN.matcher("Party > [VIP] Gygi4: Gusic better music bot")
        assertTrue(match.matches())
        assertEquals("[VIP] ", match.group(1))
        assertEquals("Gygi4", match.group(2))
        assertEquals("Gusic better music bot", match.group(3))
    }

    @Test
    fun privatePatternMatchesAndKeepsGroupIndexes() {
        val match = ChatFormatter.PRIVATE_PATTERN.matcher("From [MVP++] Gygi4: hi")
        assertTrue(match.matches())
        assertEquals("From", match.group(1))
        assertEquals("MVP", match.group(2))
        assertEquals("++", match.group(3))
        assertEquals("Gygi4", match.group(4))
        assertEquals("hi", match.group(5))
    }

    @Test
    fun bridgePatternSplitsNameAndMessage() {
        val match = ChatFormatter.BRIDGE_PATTERN.matcher("User: hello from bridge")
        assertTrue(match.find())
        assertEquals("User:", match.group(1))
        assertEquals("hello from bridge", match.group(2))
    }

    @Test
    fun guildNotificationPatternMatches() {
        val match = ChatFormatter.GUILD_NOTIFICATION_PATTERN.matcher("Guild > Gygi4 joined.")
        assertTrue(match.matches())
        assertEquals("Gygi4", match.group(1))
        assertEquals("joined.", match.group(2))
    }

    @Test
    fun invalidInputDoesNotMatchGuildPattern() {
        val match = ChatFormatter.GUILD_PATTERN.matcher("Random line without chat format")
        assertFalse(match.matches())
    }
}


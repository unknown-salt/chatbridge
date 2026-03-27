package com.chatbridge

import com.chatbridge.config.ChatBridgeConfig
import com.chatbridge.utils.ChatFormatter
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.network.chat.Component
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.regex.Pattern.compile
import com.chatbridge.config.ChatBridgeConfig.config
import com.chatbridge.utils.Extras


object ChatBridge : ModInitializer {
    val logger: Logger = LoggerFactory.getLogger("chatbridge")

    override fun onInitialize() {
        ChatBridgeConfig.load()
        ClientReceiveMessageEvents.MODIFY_GAME.register(::onModify)
    }

    private fun onModify(message: Component, actionBar: Boolean): Component {
        if (actionBar) return message

        val unformatted = compile("§\\w").matcher(message.string).replaceAll("")

        val channel = when (unformatted.split(" ")[0]) {
            "Guild" -> ChatChannel.GUILD
            "Officer" -> ChatChannel.OFFICER
            "Party" -> ChatChannel.PARTY
            "From" -> ChatChannel.PRIVATE
            "To" -> ChatChannel.PRIVATE
            "G" -> ChatChannel.GUILD
            else -> ChatChannel.UNKNOWN
        }

        var formatted = message

        if (channel != ChatChannel.UNKNOWN) formatted = ChatFormatter().format(formatted, channel)

        if (config.extras.discordWarnings && message.string.split("\n").size > 1) formatted =
            Extras().removeDiscordWarning(formatted) ?: formatted

        return formatted
    }

    fun hasCloth(): Boolean {
        try {
            Class.forName("me.shedaniel.clothconfig2.api.ConfigBuilder")
            return true
        } catch (_: Exception) {
            return false
        }
    }

    fun lastColorCode(text: String?): String {
        if (text.isNullOrEmpty()) return "§7"
        val matches = Regex("§[0-9a-f]").findAll(text.lowercase()).toList()
        return (matches.lastOrNull()?.value ?: "§7")
    }

    fun findColor(component: Component, text: String): Int? {
        if (component.string.trim() == text) return component.style.color?.value
        return component.siblings.firstNotNullOfOrNull { findColor(it, text) }
    }


    enum class ChatChannel {
        PARTY, GUILD, OFFICER, PRIVATE, UNKNOWN
    }

}
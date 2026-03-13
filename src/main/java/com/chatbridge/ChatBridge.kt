package com.chatbridge

import com.chatbridge.config.ChatBridgeConfig
import com.chatbridge.config.ChatBridgeConfig.config
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.network.chat.Component
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.regex.Pattern.compile


object ChatBridge : ModInitializer {
    val logger: Logger = LoggerFactory.getLogger("chatbridge")
    const val GUILD_PATTERN = ("^(?:G|Guild) > (?:\\[(?:\\S+?)\\] )?(\\w+)(?: \\[(?:\\S+?)\\])?: ?(.+)$")
    const val BRIDGE_PATTERN =
        ("^ *((?:.+?)(?: attached an? \\w+(?::|$)| replied to .+ with an? \\w+(?::|$)| replied to .+?(?::|$)|:))(?:(?: (.*)?$)|$)")

    override fun onInitialize() {
        ChatBridgeConfig.load()
        ClientReceiveMessageEvents.MODIFY_GAME.register(::onModify)
    }


    private fun onModify(message: Component, actionBar: Boolean): Component {
        if (actionBar) return message
        if (!config.bridgeEnabled) return message
        if (config.botNames.isEmpty()) return message

        val unformatted = compile("§\\w").matcher(message.string).replaceAll("")

        val channel = when(unformatted.split(" ")[0]) {
            "From" -> ChatChannel.PRIVATE
            "Party" -> ChatChannel.PARTY
            "Guild" -> ChatChannel.GUILD
            "G" -> ChatChannel.GUILD
            else -> ChatChannel.UNKNOWN
        }


        if (channel == ChatChannel.GUILD) {
            val match = compile(GUILD_PATTERN).matcher(unformatted)
            if (!match.matches()) return message

            val username = match.group(1)
            val text = match.group(2)

            if (!config.botNames.contains(username.lowercase())) return message

            val bridgeMatcher = compile(BRIDGE_PATTERN).matcher(text)

            val (name, msg) =
                (if (bridgeMatcher.find()) bridgeMatcher.group(1) to (bridgeMatcher.group(2)
                    ?: "") else if (config.hideBotName) text to "" else username to text)

            val formatted = Component.literal("${config.prefix} ")
                .withColor(config.prefixColor.toColor())
                .append(
                    Component.literal(if (msg.isEmpty()) text else name.replaceFirst(Regex(":$"), ""))
                        .withColor(config.nameColor.toColor())
                )
                .append(
                    Component.literal(if (msg.isEmpty()) "" else ": ${msg.replaceFirst(Regex("^: "), "")}")
                        .withColor(config.messageColor.toColor())
                )

            return formatted
        }
        return message
    }

    fun hasCloth(): Boolean {
        try {
            Class.forName("me.shedaniel.clothconfig2.api.ConfigBuilder")
            return true
        } catch (_: Exception) {
            return false
        }
    }

    private fun String.toColor(): Int {
        return Integer.decode(this)
    }

    private enum class ChatChannel {
        PARTY, GUILD, PRIVATE, UNKNOWN
    }

}
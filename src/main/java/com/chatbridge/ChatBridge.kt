package com.chatbridge

import com.chatbridge.config.ChatBridgeConfig
import com.chatbridge.config.ChatBridgeConfig.config
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.network.chat.Component
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.regex.Pattern.compile
import net.minecraft.ChatFormatting


object ChatBridge : ModInitializer {
    val logger: Logger = LoggerFactory.getLogger("chatbridge")
    const val GUILD_PATTERN =
        ("^(?:§\\w)?(?:G|Guild) > (?:§\\w)?(.+\\[(?:\\S+?)\\] )?(\\w+)(?: §3\\[(\\S+?)\\])?(?:§\\w)?: ?(.+)$")
    const val OFFICER_PATTERN =
        ("^(?:§\\w)?(?:Officer) > (?:§\\w)?(.+\\[(?:\\S+?)\\] )?(\\w+)(?: §3\\[(\\S+?)\\])?(?:§\\w)?: ?(.+)$")
    const val BRIDGE_PATTERN =
        ("^ *((?:.+?)(?: attached an? \\w+(?::|$)| replied to .+ with an? \\w+(?::|$)| replied to .+?(?::|$)|:))(?:(?: (.*)?$)|$)")

    override fun onInitialize() {
        ChatBridgeConfig.load()
        ClientReceiveMessageEvents.MODIFY_GAME.register(::onModify)
    }

    private fun onModify(message: Component, actionBar: Boolean): Component {
        if (actionBar) return message

        val unformatted = compile("§\\w").matcher(message.string).replaceAll("")

        val channel = when (unformatted.split(" ")[0]) {
            "From" -> ChatChannel.PRIVATE
            "Party" -> ChatChannel.PARTY
            "Guild" -> ChatChannel.GUILD
            "Officer" -> ChatChannel.OFFICER
            "G" -> ChatChannel.GUILD
            else -> ChatChannel.UNKNOWN
        }

        if (channel == ChatChannel.GUILD) {
            val notifMatch = compile("^(?:G|Guild) > (\\w+) (joined.|left.)").matcher(message.string)
            if (config.guildChat != ChatBridgeConfig.originalGuild && notifMatch.matches()) {
                return Component.literal("${config.guildChat.prefix} ")
                    .withColor(config.guildChat.prefixColor.toColor())
                    .append(
                        Component.literal("${notifMatch.group(1)} ")
                            .withColor(
                                config.guildChat.usernameColor?.toColor() ?: findColor(message, notifMatch.group(1))
                                ?: 0xAAAAAA
                            )
                    )
                    .append(
                        Component.literal(notifMatch.group(2))
                            .withColor(config.guildChat.guildNotificationColor.toColor())
                    )

            }
            val match = compile(GUILD_PATTERN).matcher(message.string)
            if (!match.matches()) return message

            val rank = match.group(1)
            val username = match.group(2)
            val guildRank = match.group(3)
            val text = match.group(4)

            if (config.botNames.isEmpty() || !config.bridgeEnabled || !config.botNames.contains(username.lowercase())) {
                if (config.guildChat == ChatBridgeConfig.originalGuild) return message

                val usernameColor: Int = config.guildChat.usernameColor?.toColor()
                    ?: if (rank.isNullOrEmpty()) 0xAAAAAA
                    else ChatFormatting.getByCode(lastColorCode(rank)[1])?.color ?: 0xAAAAAA

                return Component.literal("${config.guildChat.prefix} ")
                    .withColor(config.guildChat.prefixColor.toColor())
                    .append(Component.literal(if (config.guildChat.hidePlayerRank || rank.isNullOrEmpty()) "" else rank))
                    .append(Component.literal(username).withColor(usernameColor))
                    .append(
                        Component.literal(if (config.guildChat.hideGuildRank || guildRank.isEmpty()) "" else " [$guildRank]")
                            .withColor(config.guildChat.guildRankColor.toColor())
                    )
                    .append(Component.literal(": $text").withColor(config.guildChat.messageColor.toColor()))
            }

            val bridgeMatcher = compile(BRIDGE_PATTERN).matcher(text)

            val (name, msg) =
                (if (bridgeMatcher.find()) bridgeMatcher.group(1) to (bridgeMatcher.group(2)
                    ?: "") else if (config.hideBotName) text to "" else username to text)

            return Component.literal("${config.prefix} ")
                .withColor(config.prefixColor.toColor())
                .append(
                    Component.literal(if (msg.isEmpty()) text else name.replaceFirst(Regex(":$"), ""))
                        .withColor(config.nameColor.toColor())
                )
                .append(
                    Component.literal(if (msg.isEmpty()) "" else ": ${msg.replaceFirst(Regex("^: "), "")}")
                        .withColor(config.messageColor.toColor())
                )
        }

        if (channel == ChatChannel.OFFICER && config.officerChat != ChatBridgeConfig.originalOfficer) {
            val match = compile(OFFICER_PATTERN).matcher(message.string)
            if (!match.matches()) return message

            val rank = match.group(1)
            val username = match.group(2)
            val guildRank = match.group(3)
            val text = match.group(4)

            val usernameColor: Int = config.officerChat.usernameColor?.toColor()
                ?: if (rank.isNullOrEmpty()) 0xAAAAAA
                else ChatFormatting.getByCode(lastColorCode(rank)[1])?.color ?: 0xAAAAAA

            return Component.literal("${config.officerChat.prefix} ")
                .withColor(config.officerChat.prefixColor.toColor())
                .append(Component.literal(if (config.officerChat.hidePlayerRank || rank.isNullOrEmpty()) "" else rank))
                .append(Component.literal(username).withColor(usernameColor))
                .append(
                    Component.literal(if (config.officerChat.hideGuildRank || guildRank.isEmpty()) "" else " [$guildRank]")
                        .withColor(config.officerChat.guildRankColor.toColor())
                )
                .append(Component.literal(": $text").withColor(config.officerChat.messageColor.toColor()))
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

    fun lastColorCode(text: String?): String {
        if (text.isNullOrEmpty()) return "§7"
        val matches = Regex("§[0-9a-f]").findAll(text.lowercase()).toList()
        return (matches.lastOrNull()?.value ?: "§7")
    }

    fun findColor(component: Component, text: String): Int? {
        if (component.string.trim().equals(text, ignoreCase = true)) {
            return component.style.color?.value
        }

        return component.siblings.firstNotNullOfOrNull { findColor(it, text) }
    }

    private fun String.toColor(): Int {
        return Integer.decode(this)
    }

    private enum class ChatChannel {
        PARTY, GUILD, OFFICER, PRIVATE, UNKNOWN
    }

}
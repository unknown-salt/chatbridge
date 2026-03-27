package com.chatbridge.formatter

import com.chatbridge.ChatBridge.ChatChannel
import com.chatbridge.ChatBridge.findColor
import com.chatbridge.ChatBridge.lastColorCode
import com.chatbridge.config.ChatBridgeConfig
import com.chatbridge.config.ChatBridgeConfig.config
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import java.util.regex.Pattern.compile

class ChatFormatter {
    val GUILD_PATTERN =
        ("^(?:§\\w)?(?:G|Guild) > ((?:§\\w)?\\[(?:\\S+?)\\] )?(?:§\\w)?(\\w+)(?: §3\\[(\\S+?)\\])?(?:§\\w)?: ?(.+)$")
    val OFFICER_PATTERN =
        ("^(?:§\\w)?(?:Officer) > ((?:§\\w)?\\[(?:\\S+?)\\] )?(?:§\\w)?(\\w+)(?: §3\\[(\\S+?)\\])?(?:§\\w)?: ?(.+)$")
    val BRIDGE_PATTERN =
        ("^ *((?:.+?)(?: attached an? \\w+(?::|$)| replied to .+ with an? \\w+(?::|$)| replied to .+?(?::|$)|:))(?:(?: (.*)?$)|$)")
    val PARTY_PATTERN =
        ("^(?:§\\w)?(?:Party) (?:§\\w)?> ((?:§\\w)?\\[(?:\\S+?)\\] )?(?:§\\w)?(\\w+)(?:§\\w)?: ?(.+)$")
    val PRIVATE_PATTERN =
        ("^(From|To)(?: \\[([^+\\s]+?)(\\+{1,4})?\\])? (\\w+): (.+)$")

    fun format(message: Component, channel: ChatChannel): Component {
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
                        Component.literal(if (config.guildChat.hideGuildRank || guildRank.isNullOrEmpty()) "" else " [$guildRank]")
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
                    Component.literal(if (config.officerChat.hideGuildRank || guildRank.isNullOrEmpty()) "" else " [$guildRank]")
                        .withColor(config.officerChat.guildRankColor.toColor())
                )
                .append(Component.literal(": $text").withColor(config.officerChat.messageColor.toColor()))
        }

        if (channel == ChatChannel.PARTY && config.partyChat != ChatBridgeConfig.originalParty) {
            val match = compile(PARTY_PATTERN).matcher(message.string)
            if (!match.matches()) return message

            val rank = match.group(1)
            val username = match.group(2)
            val text = match.group(3)

            val prefix = if (config.partyChat.prefix == ChatBridgeConfig.originalParty.prefix)
                Component.literal("Party ").withColor(config.partyChat.prefixColor.toColor())
                    .append(Component.literal("> ").withColor(0x555555))
            else Component.literal("${config.partyChat.prefix} ").withColor(config.partyChat.prefixColor.toColor())

            val usernameColor: Int = config.partyChat.usernameColor?.toColor()
                ?: if (rank.isNullOrEmpty()) 0xAAAAAA
                else ChatFormatting.getByCode(lastColorCode(rank)[1])?.color ?: 0xAAAAAA

            return prefix
                .append(Component.literal(if (config.partyChat.hidePlayerRank || rank.isNullOrEmpty()) "" else rank))
                .append(Component.literal(username).withColor(usernameColor))
                .append(Component.literal(": $text").withColor(config.partyChat.messageColor.toColor()))
        }

        if (channel == ChatChannel.PRIVATE && config.privateChat != ChatBridgeConfig.originalPrivate) {
            val match = compile(PRIVATE_PATTERN).matcher(message.string)
            if (!match.matches()) return message

            val isFrom = match.group(1) == "From"
            val rankName = match.group(2)
            val plus = match.group(3)
            val username = match.group(4)
            val text = match.group(5)

            val bracketsColor =
                if (!rankName.isNullOrEmpty()) findColor(message, "] $username") ?: findColor(message, "[")
                ?: 0xAAAAAA else 0xAAAAAA

            val rank = when {
                config.privateChat.hidePlayerRank || rankName.isNullOrEmpty() -> Component.literal("")
                else ->
                    Component.literal("[").withColor(bracketsColor)
                        .append(Component.literal(rankName).withColor(findColor(message, rankName) ?: bracketsColor))
                        .append(
                            Component.literal(plus ?: "")
                                .withColor(if (plus.isNullOrEmpty()) 0xFF5555 else findColor(message, plus) ?: 0xFF5555)
                        )
                        .append(Component.literal("] ").withColor(bracketsColor))
            }


            val usernameColor: Int =
                config.privateChat.usernameColor?.toColor() ?: findColor(
                    message,
                    if (rankName.isNullOrEmpty()) username else "] $username"
                ) ?: 0xAAAAAA

            return Component.literal("${if (isFrom) config.privateChat.receivePrefix else config.privateChat.sendPrefix} ")
                .withColor(config.privateChat.prefixColor.toColor())
                .append(rank)
                .append(Component.literal(username).withColor(usernameColor))
                .append(Component.literal(": $text").withColor(config.privateChat.messageColor.toColor()))
        }

        return message
    }

    private fun String.toColor(): Int {
        return Integer.decode(this)
    }
}
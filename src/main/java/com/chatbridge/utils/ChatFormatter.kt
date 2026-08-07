package com.chatbridge.utils

import com.chatbridge.ChatBridge.ChatChannel
import com.chatbridge.ChatBridge.findColor
import com.chatbridge.ChatBridge.lastColorCode
import com.chatbridge.config.ChatBridgeConfig
import com.chatbridge.config.ChatBridgeConfig.config
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.TextColor
import java.util.regex.Pattern

class ChatFormatter {
    companion object {
        internal val GUILD_PATTERN: Pattern = Pattern.compile(
            """^(?:§\w)?(?:G|Guild) > ((?:§\w)?\[(?:\S+?)\] )?(?:§\w)?(\w+)(?: §\w\[(\S+?)\])?(?:§\w)?: ?(.+)$"""
        )
        internal val OFFICER_PATTERN: Pattern = Pattern.compile(
            """^(?:§\w)?(?:Officer) > ((?:§\w)?\[(?:\S+?)\] )?(?:§\w)?(\w+)(?: §\w\[(\S+?)\])?(?:§\w)?: ?(.+)$"""
        )
        internal val BRIDGE_PATTERN: Pattern = Pattern.compile(
            """^ *((?:.+?)(?: attached an? \w+(?::|$)| replied to .+ with an? \w+(?::|$)| replied to .+?(?::|$)|:))(?:(?: (.*)?$)|$)"""
        )
        internal val PARTY_PATTERN: Pattern = Pattern.compile(
            """^(?:§\w)?(?:Party) (?:§\w)?> ((?:§\w)?\[(?:\S+?)\] )?(?:§\w)?(\w+)(?:§\w)?: ?(.+)$"""
        )
        internal val PRIVATE_PATTERN: Pattern = Pattern.compile(
            """^(From|To)(?: \[([^+\s]+?)(\+{1,4})?\])? (\w+): (.+)$"""
        )
        internal val GUILD_NOTIFICATION_PATTERN: Pattern = Pattern.compile("""^(?:G|Guild) > (\w+) (joined.|left.)""")
    }

    private data class ParsedMessage(
        val rank: String?,
        val username: String,
        val guildRank: String?,
        val text: String,
    )

    fun format(message: Component, channel: ChatChannel): Component {
        val messageText = message.string.substringBefore('\n')

        if (channel == ChatChannel.GUILD) {
            val notifMatch = GUILD_NOTIFICATION_PATTERN.matcher(messageText)
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
            val parsed = parsePattern(messageText, GUILD_PATTERN, hasGuildRank = true) ?: return message
            val isBridgeMessage =
                config.bridgeEnabled && config.botNames.isNotEmpty() && config.botNames.contains(parsed.username.lowercase())

            if (!isBridgeMessage) {
                if (config.guildChat == ChatBridgeConfig.originalGuild) return message

                val usernameColor = resolveUsernameColor(config.guildChat.usernameColor, parsed.rank)

                return buildStandardMessage(
                    prefix = coloredPrefix(config.guildChat.prefix, config.guildChat.prefixColor),
                    hidePlayerRank = config.guildChat.hidePlayerRank,
                    rank = parsed.rank,
                    username = parsed.username,
                    usernameColor = usernameColor,
                    text = parsed.text,
                    textColor = config.guildChat.messageColor.toColor(),
                    guildRank = parsed.guildRank,
                    hideGuildRank = config.guildChat.hideGuildRank,
                    guildRankColor = config.guildChat.guildRankColor.toColor()
                )
            }

            val bridgeMatcher = BRIDGE_PATTERN.matcher(parsed.text)

            val (name, msg) =
                (if (bridgeMatcher.find()) bridgeMatcher.group(1) to (bridgeMatcher.group(2)
                    ?: "") else if (config.hideBotName) parsed.text to "" else parsed.username to parsed.text)

            return coloredPrefix(config.prefix, config.prefixColor)
                .append(
                    Component.literal(if (msg.isEmpty()) parsed.text else name.removeSuffix(":"))
                        .withColor(config.nameColor.toColor())
                )
                .append(
                    Component.literal(if (msg.isEmpty()) "" else ": ${msg.removePrefix(": ")}")
                        .withColor(config.messageColor.toColor())
                )
        }

        if (channel == ChatChannel.OFFICER && config.officerChat != ChatBridgeConfig.originalOfficer) {
            val parsed = parsePattern(messageText, OFFICER_PATTERN, hasGuildRank = true) ?: return message

            val usernameColor = resolveUsernameColor(config.officerChat.usernameColor, parsed.rank)

            return buildStandardMessage(
                prefix = coloredPrefix(config.officerChat.prefix, config.officerChat.prefixColor),
                hidePlayerRank = config.officerChat.hidePlayerRank,
                rank = parsed.rank,
                username = parsed.username,
                usernameColor = usernameColor,
                text = parsed.text,
                textColor = config.officerChat.messageColor.toColor(),
                guildRank = parsed.guildRank,
                hideGuildRank = config.officerChat.hideGuildRank,
                guildRankColor = config.officerChat.guildRankColor.toColor()
            )
        }

        if (channel == ChatChannel.PARTY && config.partyChat != ChatBridgeConfig.originalParty) {
            val parsed = parsePattern(messageText, PARTY_PATTERN, hasGuildRank = false) ?: return message

            val prefix = buildPartyPrefix()

            val usernameColor = resolveUsernameColor(config.partyChat.usernameColor, parsed.rank)

            return buildStandardMessage(
                prefix = prefix,
                hidePlayerRank = config.partyChat.hidePlayerRank,
                rank = parsed.rank,
                username = parsed.username,
                usernameColor = usernameColor,
                text = parsed.text,
                textColor = config.partyChat.messageColor.toColor()
            )
        }

        if (channel == ChatChannel.PRIVATE && config.privateChat != ChatBridgeConfig.originalPrivate) {
            val match = PRIVATE_PATTERN.matcher(messageText)
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

            return coloredPrefix(if (isFrom) config.privateChat.receivePrefix else config.privateChat.sendPrefix, config.privateChat.prefixColor)
                .append(rank)
                .append(Component.literal(username).withColor(usernameColor))
                .append(Component.literal(": $text").withColor(config.privateChat.messageColor.toColor()))
        }

        return message
    }
    
    private fun resolveUsernameColor(configColor: String?, rank: String?): Int {
        return configColor?.toColor()
            ?: if (rank.isNullOrEmpty()) 0xAAAAAA
            else ChatFormatting.getByCode(lastColorCode(rank)[1])
                ?.let { TextColor.fromLegacyFormat(it)?.getValue() }
                ?: 0xAAAAAA
    }

    private fun parsePattern(messageText: String, pattern: Pattern, hasGuildRank: Boolean): ParsedMessage? {
        val match = pattern.matcher(messageText)
        if (!match.matches()) return null

        return if (hasGuildRank) {
            ParsedMessage(
                rank = match.group(1),
                username = match.group(2),
                guildRank = match.group(3),
                text = match.group(4)
            )
        } else {
            ParsedMessage(
                rank = match.group(1),
                username = match.group(2),
                guildRank = null,
                text = match.group(3)
            )
        }
    }

    private fun coloredPrefix(prefix: String, color: String): MutableComponent {
        return Component.literal("$prefix ").withColor(color.toColor())
    }

    private fun buildPartyPrefix(): MutableComponent {
        return if (config.partyChat.prefix == ChatBridgeConfig.originalParty.prefix) {
            Component.literal("Party ").withColor(config.partyChat.prefixColor.toColor())
                .append(Component.literal("> ").withColor(0x555555))
        } else {
            coloredPrefix(config.partyChat.prefix, config.partyChat.prefixColor)
        }
    }

    private fun buildStandardMessage(
        prefix: Component,
        hidePlayerRank: Boolean,
        rank: String?,
        username: String,
        usernameColor: Int,
        text: String,
        textColor: Int,
        guildRank: String? = null,
        hideGuildRank: Boolean = false,
        guildRankColor: Int = 0,
    ): Component {
        val result = prefix.copy()
            .append(Component.literal(if (hidePlayerRank || rank.isNullOrEmpty()) "" else rank))
            .append(Component.literal(username).withColor(usernameColor))

        if (guildRank != null) {
            result.append(
                Component.literal(if (hideGuildRank || guildRank.isEmpty()) "" else " [$guildRank]")
                    .withColor(guildRankColor)
            )
        }

        return result.append(Component.literal(": $text").withColor(textColor))
    }
}

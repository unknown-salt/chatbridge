package com.chatbridge.config.categories

import com.chatbridge.config.ChatBridgeConfig.config
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.minecraft.network.chat.Component.translatable


class OtherChatsCategory {
    fun build(
        category: ConfigCategory,
        entryBuilder: ConfigEntryBuilder,
    ) {
        val guild =
            entryBuilder.startSubCategory(translatable("entry.chatbridge.guildChat")).setExpanded(false)

        val officer =
            entryBuilder.startSubCategory(translatable("entry.chatbridge.officerChat")).setExpanded(false)

        val party =
            entryBuilder.startSubCategory(translatable("entry.chatbridge.partyChat")).setExpanded(false)

        val private =
            entryBuilder.startSubCategory(translatable("entry.chatbridge.private")).setExpanded(false)

        guild.add(
            entryBuilder.startTextField(translatable("entry.chatbridge.prefix"), config.guildChat.prefix)
                .setDefaultValue("Guild >")
                .setSaveConsumer { value -> config.guildChat.prefix = value.trim() }
                .build()
        )

        guild.add(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.prefixColor"),
                Integer.decode(config.guildChat.prefixColor)
            )
                .setDefaultValue(0x00AA00)
                .setSaveConsumer { value -> config.guildChat.prefixColor = String.format("#%06X", value) }
                .build()
        )

        guild.add(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.messageColor"),
                Integer.decode(config.guildChat.messageColor)
            )
                .setDefaultValue(0xFFFFFF)
                .setSaveConsumer { value -> config.guildChat.messageColor = String.format("#%06X", value) }
                .build()
        )

        guild.add(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.guildNotificationColor"),
                Integer.decode(config.guildChat.guildNotificationColor)
            )
                .setDefaultValue(0xFFFF55)
                .setSaveConsumer { value -> config.guildChat.guildNotificationColor = String.format("#%06X", value) }
                .build()
        )

        guild.add(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.guildRankColor"),
                Integer.decode(config.guildChat.guildRankColor)
            )
                .setDefaultValue(0x00AAAA)
                .setSaveConsumer { value -> config.guildChat.guildRankColor = String.format("#%06X", value) }
                .build()
        )

        guild.add(
            entryBuilder.startBooleanToggle(
                translatable("entry.chatbridge.hideGuildRank"),
                config.guildChat.hideGuildRank
            )
                .setDefaultValue(false)
                .setSaveConsumer { value -> config.guildChat.hideGuildRank = value }
                .build()
        )

        guild.add(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.badUsernameColor"),
                Integer.decode(config.guildChat.usernameColor ?: "#000000")
            )
                .setDefaultValue(0x000000)
                .setSaveConsumer { value ->
                    config.guildChat.usernameColor = if (value != 0x000000) String.format("#%06X", value) else null
                }
                .build()
        )

        guild.add(
            entryBuilder.startBooleanToggle(
                translatable("entry.chatbridge.hidePlayerRank"),
                config.guildChat.hidePlayerRank
            )
                .setDefaultValue(false)
                .setSaveConsumer { value -> config.guildChat.hidePlayerRank = value }
                .build()
        )

        officer.add(
            entryBuilder.startTextField(translatable("entry.chatbridge.prefix"), config.officerChat.prefix)
                .setDefaultValue("Officer >")
                .setSaveConsumer { value -> config.officerChat.prefix = value.trim() }
                .build()
        )

        officer.add(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.prefixColor"),
                Integer.decode(config.officerChat.prefixColor)
            )
                .setDefaultValue(0x00AAAA)
                .setSaveConsumer { value -> config.officerChat.prefixColor = String.format("#%06X", value) }
                .build()
        )

        officer.add(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.messageColor"),
                Integer.decode(config.officerChat.messageColor)
            )
                .setDefaultValue(0xFFFFFF)
                .setSaveConsumer { value -> config.officerChat.messageColor = String.format("#%06X", value) }
                .build()
        )

        officer.add(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.guildRankColor"),
                Integer.decode(config.officerChat.guildRankColor)
            )
                .setDefaultValue(0x00AAAA)
                .setSaveConsumer { value -> config.officerChat.guildRankColor = String.format("#%06X", value) }
                .build()
        )

        officer.add(
            entryBuilder.startBooleanToggle(
                translatable("entry.chatbridge.hideGuildRank"),
                config.officerChat.hideGuildRank
            )
                .setDefaultValue(false)
                .setSaveConsumer { value -> config.officerChat.hideGuildRank = value }
                .build()
        )

        officer.add(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.badUsernameColor"),
                Integer.decode(config.officerChat.usernameColor ?: "#000000")
            )
                .setDefaultValue(0x000000)
                .setSaveConsumer { value ->
                    config.officerChat.usernameColor = if (value != 0x000000) String.format("#%06X", value) else null
                }
                .build()
        )

        officer.add(
            entryBuilder.startBooleanToggle(
                translatable("entry.chatbridge.hidePlayerRank"),
                config.officerChat.hidePlayerRank
            )
                .setDefaultValue(false)
                .setSaveConsumer { value -> config.officerChat.hidePlayerRank = value }
                .build()
        )

        category.addEntry(guild.build())
        category.addEntry(officer.build())
    }

}
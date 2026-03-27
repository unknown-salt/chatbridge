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
            entryBuilder.startSubCategory(translatable("entry.chatbridge.privateChat")).setExpanded(false)

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

        party.add(
            entryBuilder.startTextField(translatable("entry.chatbridge.prefix"), config.partyChat.prefix)
                .setDefaultValue("Party >")
                .setSaveConsumer { value -> config.partyChat.prefix = value.trim() }
                .build()
        )

        party.add(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.prefixColor"),
                Integer.decode(config.partyChat.prefixColor)
            )
                .setDefaultValue(0x5555FF)
                .setSaveConsumer { value -> config.partyChat.prefixColor = String.format("#%06X", value) }
                .build()
        )

        party.add(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.messageColor"),
                Integer.decode(config.partyChat.messageColor)
            )
                .setDefaultValue(0xFFFFFF)
                .setSaveConsumer { value -> config.partyChat.messageColor = String.format("#%06X", value) }
                .build()
        )

        party.add(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.badUsernameColor"),
                Integer.decode(config.partyChat.usernameColor ?: "#000000")
            )
                .setDefaultValue(0x000000)
                .setSaveConsumer { value ->
                    config.partyChat.usernameColor = if (value != 0x000000) String.format("#%06X", value) else null
                }
                .build()
        )

        party.add(
            entryBuilder.startBooleanToggle(
                translatable("entry.chatbridge.hidePlayerRank"),
                config.partyChat.hidePlayerRank
            )
                .setDefaultValue(false)
                .setSaveConsumer { value -> config.partyChat.hidePlayerRank = value }
                .build()
        )

        private.add(
            entryBuilder.startTextField(
                translatable("entry.chatbridge.receivePrefix"),
                config.privateChat.receivePrefix
            )
                .setDefaultValue("From >")
                .setSaveConsumer { value -> config.privateChat.receivePrefix = value.trim() }
                .build()
        )

        private.add(
            entryBuilder.startTextField(
                translatable("entry.chatbridge.sendPrefix"),
                config.privateChat.sendPrefix
            )
                .setDefaultValue("To >")
                .setSaveConsumer { value -> config.privateChat.sendPrefix = value.trim() }
                .build()
        )

        private.add(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.prefixColor"),
                Integer.decode(config.privateChat.prefixColor)
            )
                .setDefaultValue(0xFF55FF)
                .setSaveConsumer { value -> config.privateChat.prefixColor = String.format("#%06X", value) }
                .build()
        )

        private.add(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.messageColor"),
                Integer.decode(config.privateChat.messageColor)
            )
                .setDefaultValue(0xAAAAAA)
                .setSaveConsumer { value -> config.privateChat.messageColor = String.format("#%06X", value) }
                .build()
        )

        private.add(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.badUsernameColor"),
                Integer.decode(config.privateChat.usernameColor ?: "#000000")
            )
                .setDefaultValue(0x000000)
                .setSaveConsumer { value ->
                    config.privateChat.usernameColor = if (value != 0x000000) String.format("#%06X", value) else null
                }
                .build()
        )

        private.add(
            entryBuilder.startBooleanToggle(
                translatable("entry.chatbridge.hidePlayerRank"),
                config.privateChat.hidePlayerRank
            )
                .setDefaultValue(false)
                .setSaveConsumer { value -> config.privateChat.hidePlayerRank = value }
                .build()
        )

        category.addEntry(guild.build())
        category.addEntry(officer.build())
        category.addEntry(party.build())
        category.addEntry(private.build())
    }

}
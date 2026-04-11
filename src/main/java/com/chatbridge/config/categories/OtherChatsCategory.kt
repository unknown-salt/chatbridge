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
        val guild = entryBuilder.startSubCategory(translatable("entry.chatbridge.guildChat")).setExpanded(false)
        val officer = entryBuilder.startSubCategory(translatable("entry.chatbridge.officerChat")).setExpanded(false)
        val party = entryBuilder.startSubCategory(translatable("entry.chatbridge.partyChat")).setExpanded(false)
        val privateChat = entryBuilder.startSubCategory(translatable("entry.chatbridge.privateChat")).setExpanded(false)

        guild.add(entryBuilder.trimmedTextEntry("entry.chatbridge.prefix", config.guildChat.prefix, "Guild >") { config.guildChat.prefix = it })
        guild.add(entryBuilder.colorEntry("entry.chatbridge.prefixColor", config.guildChat.prefixColor, 0x00AA00) { config.guildChat.prefixColor = it })
        guild.add(entryBuilder.colorEntry("entry.chatbridge.messageColor", config.guildChat.messageColor, 0xFFFFFF) { config.guildChat.messageColor = it })
        guild.add(entryBuilder.colorEntry("entry.chatbridge.guildNotificationColor", config.guildChat.guildNotificationColor, 0xFFFF55) { config.guildChat.guildNotificationColor = it })
        guild.add(entryBuilder.colorEntry("entry.chatbridge.guildRankColor", config.guildChat.guildRankColor, 0x00AAAA) { config.guildChat.guildRankColor = it })
        guild.add(entryBuilder.booleanEntry("entry.chatbridge.hideGuildRank", config.guildChat.hideGuildRank) { config.guildChat.hideGuildRank = it })
        guild.add(entryBuilder.optionalUsernameColorEntry(config.guildChat.usernameColor) { config.guildChat.usernameColor = it })
        guild.add(entryBuilder.booleanEntry("entry.chatbridge.hidePlayerRank", config.guildChat.hidePlayerRank) { config.guildChat.hidePlayerRank = it })

        officer.add(entryBuilder.trimmedTextEntry("entry.chatbridge.prefix", config.officerChat.prefix, "Officer >") { config.officerChat.prefix = it })
        officer.add(entryBuilder.colorEntry("entry.chatbridge.prefixColor", config.officerChat.prefixColor, 0x00AAAA) { config.officerChat.prefixColor = it })
        officer.add(entryBuilder.colorEntry("entry.chatbridge.messageColor", config.officerChat.messageColor, 0xFFFFFF) { config.officerChat.messageColor = it })
        officer.add(entryBuilder.colorEntry("entry.chatbridge.guildRankColor", config.officerChat.guildRankColor, 0x00AAAA) { config.officerChat.guildRankColor = it })
        officer.add(entryBuilder.booleanEntry("entry.chatbridge.hideGuildRank", config.officerChat.hideGuildRank) { config.officerChat.hideGuildRank = it })
        officer.add(entryBuilder.optionalUsernameColorEntry(config.officerChat.usernameColor) { config.officerChat.usernameColor = it })
        officer.add(entryBuilder.booleanEntry("entry.chatbridge.hidePlayerRank", config.officerChat.hidePlayerRank) { config.officerChat.hidePlayerRank = it })

        party.add(entryBuilder.trimmedTextEntry("entry.chatbridge.prefix", config.partyChat.prefix, "Party >") { config.partyChat.prefix = it })
        party.add(entryBuilder.colorEntry("entry.chatbridge.prefixColor", config.partyChat.prefixColor, 0x5555FF) { config.partyChat.prefixColor = it })
        party.add(entryBuilder.colorEntry("entry.chatbridge.messageColor", config.partyChat.messageColor, 0xFFFFFF) { config.partyChat.messageColor = it })
        party.add(entryBuilder.optionalUsernameColorEntry(config.partyChat.usernameColor) { config.partyChat.usernameColor = it })
        party.add(entryBuilder.booleanEntry("entry.chatbridge.hidePlayerRank", config.partyChat.hidePlayerRank) { config.partyChat.hidePlayerRank = it })

        privateChat.add(entryBuilder.trimmedTextEntry("entry.chatbridge.receivePrefix", config.privateChat.receivePrefix, "From >") { config.privateChat.receivePrefix = it })
        privateChat.add(entryBuilder.trimmedTextEntry("entry.chatbridge.sendPrefix", config.privateChat.sendPrefix, "To >") { config.privateChat.sendPrefix = it })
        privateChat.add(entryBuilder.colorEntry("entry.chatbridge.prefixColor", config.privateChat.prefixColor, 0xFF55FF) { config.privateChat.prefixColor = it })
        privateChat.add(entryBuilder.colorEntry("entry.chatbridge.messageColor", config.privateChat.messageColor, 0xAAAAAA) { config.privateChat.messageColor = it })
        privateChat.add(entryBuilder.optionalUsernameColorEntry(config.privateChat.usernameColor) { config.privateChat.usernameColor = it })
        privateChat.add(entryBuilder.booleanEntry("entry.chatbridge.hidePlayerRank", config.privateChat.hidePlayerRank) { config.privateChat.hidePlayerRank = it })

        category.addEntry(guild.build())
        category.addEntry(officer.build())
        category.addEntry(party.build())
        category.addEntry(privateChat.build())
    }
}

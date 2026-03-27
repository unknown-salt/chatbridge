package com.chatbridge.config.categories

import com.chatbridge.config.ChatBridgeConfig.config
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.minecraft.network.chat.Component.translatable

class ExtrasCategory {
    fun build(
        category: ConfigCategory,
        entryBuilder: ConfigEntryBuilder,
    ) {
        category.addEntry(
            entryBuilder.startBooleanToggle(
                translatable("entry.chatbridge.extras.discordWarnings"),
                config.extras.discordWarnings
            )
                .setDefaultValue(true)
                .setSaveConsumer { value -> config.extras.discordWarnings = value }
                .build()
        )
    }
}
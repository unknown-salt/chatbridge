package com.chatbridge.config.categories

import com.chatbridge.config.ChatBridgeConfig.config
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.minecraft.network.chat.Component.translatable

class GeneralCategory {
    fun build(
        category: ConfigCategory,
        entryBuilder: ConfigEntryBuilder,
    ) {
        category.addEntry(
            entryBuilder.startBooleanToggle(translatable("entry.chatbridge.enabled"), config.bridgeEnabled)
                .setDefaultValue(true)
                .setTooltip(translatable("tooltip.chatbridge.bridgeEnabled"))
                .setSaveConsumer { value -> config.bridgeEnabled = value }
                .build()
        )

        category.addEntry(
            entryBuilder.startTextField(translatable("entry.chatbridge.botNames"), config.botNames.joinToString(" "))
                .setTooltip(translatable("tooltip.chatbridge.botNames"))
                .setSaveConsumer { value ->
                    config.botNames = value.lowercase().trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
                }
                .build()
        )

        category.addEntry(entryBuilder.trimmedTextEntry("entry.chatbridge.prefix", config.prefix, "Bridge >") { config.prefix = it })
        category.addEntry(entryBuilder.colorEntry("entry.chatbridge.prefixColor", config.prefixColor, 0x616AC7) { config.prefixColor = it })
        category.addEntry(entryBuilder.colorEntry("entry.chatbridge.nameColor", config.nameColor, 0x8F99FF) { config.nameColor = it })
        category.addEntry(entryBuilder.colorEntry("entry.chatbridge.messageColor", config.messageColor, 0xC1C3C7) { config.messageColor = it })

        category.addEntry(
            entryBuilder.startBooleanToggle(translatable("entry.chatbridge.hideBotName"), config.hideBotName)
                .setDefaultValue(false)
                .setTooltip(translatable("tooltip.chatbridge.hideBotName"))
                .setSaveConsumer { value -> config.hideBotName = value }
                .build()
        )
    }
}

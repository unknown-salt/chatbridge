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
            entryBuilder.startBooleanToggle(
                translatable("entry.chatbridge.enabled"),
                config.bridgeEnabled
            )
                .setDefaultValue(true)
                .setTooltip(translatable("tooltip.chatbridge.bridgeEnabled"))
                .setSaveConsumer { value -> config.bridgeEnabled = value }
                .build()
        )

        category.addEntry(
            entryBuilder.startTextField(
                translatable("entry.chatbridge.botNames"),
                config.botNames.joinToString(" ")
            )
                .setTooltip(translatable("tooltip.chatbridge.botNames"))
                .setSaveConsumer { value -> config.botNames = value.lowercase().split(" ") }
                .build()
        )

        category.addEntry(
            entryBuilder.startTextField(translatable("entry.chatbridge.prefix"), config.prefix)
                .setDefaultValue("Bridge >")
                .setSaveConsumer { value -> config.prefix = value.trim() }
                .build()
        )

        category.addEntry(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.prefixColor"),
                Integer.decode(config.prefixColor)
            )
                .setDefaultValue(0x616AC7)
                .setSaveConsumer { value -> config.prefixColor = String.format("#%06X", value) }
                .build()
        )

        category.addEntry(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.nameColor"),
                Integer.decode(config.nameColor)
            )
                .setDefaultValue(0x8F99FF)
                .setSaveConsumer { value -> config.nameColor = String.format("#%06X", value) }
                .build()
        )

        category.addEntry(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.messageColor"),
                Integer.decode(config.messageColor)
            )
                .setDefaultValue(0xC1C3C7)
                .setSaveConsumer { value -> config.messageColor = String.format("#%06X", value) }
                .build()
        )


        category.addEntry(
            entryBuilder.startBooleanToggle(
                translatable("entry.chatbridge.hideBotName"),
                config.hideBotName
            )
                .setDefaultValue(false)
                .setTooltip(translatable("tooltip.chatbridge.hideBotName"))
                .setSaveConsumer { value -> config.hideBotName = value }
                .build()
        )
    }
}
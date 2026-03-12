package com.chatbridge.config

import com.chatbridge.config.ChatBridgeConfig.config
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component.translatable

object ChatBridgeConfigManager {
    fun build(parent: Screen?): Screen {
        val builder = ConfigBuilder.create()
        builder.parentScreen = parent
        builder.title = translatable("title.chatbridge.config")

        val general = builder.getOrCreateCategory(translatable("category.chatbridge.general"))
        val entryBuilder = builder.entryBuilder()

        general.addEntry(
            entryBuilder.startBooleanToggle(translatable("entry.chatbridge.bridgeEnabled"), config.bridgeEnabled)
                .setDefaultValue(true)
                .setTooltip(translatable("tooltip.chatbridge.bridgeEnabled"))
                .setSaveConsumer { value -> config.bridgeEnabled = value }
                .build()
        )

        general.addEntry(
            entryBuilder.startTextField(translatable("entry.chatbridge.botNames"), config.botNames.joinToString(" "))
                .setTooltip(translatable("tooltip.chatbridge.botNames"))
                .setSaveConsumer { value -> config.botNames = value.lowercase().split(" ") }
                .build()
        )

        general.addEntry(
            entryBuilder.startTextField(translatable("entry.chatbridge.prefix"), config.prefix)
                .setDefaultValue("Bridge >")
                .setSaveConsumer { value -> config.prefix = value.trim() }
                .build()
        )

        general.addEntry(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.prefixColor"),
                Integer.decode(config.prefixColor)
            )
                .setDefaultValue(0x616AC7)
                .setSaveConsumer { value -> config.prefixColor = String.format("#%06X", value) }
                .build()
        )

        general.addEntry(
            entryBuilder.startColorField(translatable("entry.chatbridge.nameColor"), Integer.decode(config.nameColor))
                .setDefaultValue(0x8F99FF)
                .setSaveConsumer { value -> config.nameColor = String.format("#%06X", value) }
                .build()
        )

        general.addEntry(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.messageColor"),
                Integer.decode(config.messageColor)
            )
                .setDefaultValue(0xC1C3C7)
                .setSaveConsumer { value -> config.messageColor = String.format("#%06X", value) }
                .build()
        )


        general.addEntry(
            entryBuilder.startBooleanToggle(translatable("entry.chatbridge.hideBotName"), config.hideBotName)
                .setDefaultValue(false)
                .setTooltip(translatable("tooltip.chatbridge.hideBotName"))
                .setSaveConsumer { value -> config.hideBotName = value }
                .build()
        )

        builder.setSavingRunnable {
            ChatBridgeConfig.save()
        }

        return builder.build()
    }


}
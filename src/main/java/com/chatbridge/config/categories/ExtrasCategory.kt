package com.chatbridge.config.categories

import com.chatbridge.config.ChatBridgeConfig.config
import me.shedaniel.clothconfig2.api.ConfigCategory
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Component.translatable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Optional

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

        val timestamp =
            entryBuilder.startSubCategory(translatable("entry.chatbridge.timestamp")).setExpanded(false)

        timestamp.add(
            entryBuilder.startBooleanToggle(
                translatable("entry.chatbridge.enabled"),
                config.extras.timestamp.enabled
            )
                .setDefaultValue(false)
                .setSaveConsumer { value -> config.extras.timestamp.enabled = value }
                .build()
        )

        timestamp.add(
            entryBuilder.startTextField(
                translatable("entry.chatbridge.timestamp.format"),
                config.extras.timestamp.format
            )
                .setDefaultValue("[HH:mm:ss]")
                .setSaveConsumer { value -> config.extras.timestamp.format = value }
                .setErrorSupplier { value ->
                    if (isTimestampValid(value)) Optional.empty()
                    else Optional.of(Component.literal("Invalid format: $value. Check DateTimeFormatter patterns."))
                }
                .build()
        )

        timestamp.add(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.timestamp.color"),
                Integer.decode(config.extras.timestamp.color)
            )
                .setDefaultValue(0xFF55FF)
                .setSaveConsumer { value -> config.extras.timestamp.color = String.format("#%06X", value) }
                .build()
        )

        timestamp.add(
            entryBuilder.startColorField(
                translatable("entry.chatbridge.timestamp.numbersColor"),
                Integer.decode(config.extras.timestamp.numbersColor)
            )
                .setDefaultValue(0xFF33FF)
                .setSaveConsumer { value -> config.extras.timestamp.numbersColor = String.format("#%06X", value) }
                .build()
        )

        timestamp.add(
            entryBuilder.startBooleanToggle(
                translatable("entry.chatbridge.timestamp.ignoreEmpty"),
                config.extras.timestamp.ignoreEmpty
            )
                .setDefaultValue(false)
                .setSaveConsumer { value -> config.extras.timestamp.ignoreEmpty = value }
                .build()
        )

        timestamp.add(entryBuilder.startTextDescription(translatable("description.chatbridge.timestamp")).build())

        category.addEntry(timestamp.build())

    }

    fun isTimestampValid(pattern: String): Boolean {
        return try {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            formatter.format(LocalDateTime.now())
            true
        } catch (e: Exception) {
            false
        }
    }

}
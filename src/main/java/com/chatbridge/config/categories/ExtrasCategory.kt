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
            entryBuilder.startBooleanToggle(translatable("entry.chatbridge.extras.discordWarnings"), config.extras.discordWarnings)
                .setDefaultValue(true)
                .setSaveConsumer { value -> config.extras.discordWarnings = value }
                .build()
        )

        val timestamp =
            entryBuilder.startSubCategory(translatable("entry.chatbridge.timestamp")).setExpanded(false)

        timestamp.add(entryBuilder.booleanEntry("entry.chatbridge.enabled", config.extras.timestamp.enabled) { config.extras.timestamp.enabled = it })

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

        timestamp.add(entryBuilder.colorEntry("entry.chatbridge.timestamp.color", config.extras.timestamp.color, 0xFF55FF) { config.extras.timestamp.color = it })
        timestamp.add(entryBuilder.colorEntry("entry.chatbridge.timestamp.numbersColor", config.extras.timestamp.numbersColor, 0xFF33FF) { config.extras.timestamp.numbersColor = it })
        timestamp.add(entryBuilder.booleanEntry("entry.chatbridge.timestamp.ignoreEmpty", config.extras.timestamp.ignoreEmpty) { config.extras.timestamp.ignoreEmpty = it })

        timestamp.add(entryBuilder.startTextDescription(translatable("description.chatbridge.timestamp")).build())

        category.addEntry(timestamp.build())

    }

    fun isTimestampValid(pattern: String): Boolean {
        return try {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            formatter.format(LocalDateTime.now())
            true
        } catch (_: Exception) {
            false
        }
    }
}

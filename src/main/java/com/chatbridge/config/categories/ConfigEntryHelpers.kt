package com.chatbridge.config.categories

import com.chatbridge.utils.toHexColor
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder
import net.minecraft.network.chat.Component.translatable

internal fun ConfigEntryBuilder.trimmedTextEntry(
    key: String,
    currentValue: String,
    defaultValue: String,
    onSave: (String) -> Unit,
) = startTextField(translatable(key), currentValue)
    .setDefaultValue(defaultValue)
    .setSaveConsumer { value -> onSave(value.trim()) }
    .build()

internal fun ConfigEntryBuilder.colorEntry(
    key: String,
    currentColor: String,
    defaultColor: Int,
    onSave: (String) -> Unit,
) = startColorField(translatable(key), Integer.decode(currentColor))
    .setDefaultValue(defaultColor)
    .setSaveConsumer { value -> onSave(value.toHexColor()) }
    .build()

internal fun ConfigEntryBuilder.booleanEntry(
    key: String,
    currentValue: Boolean,
    onSave: (Boolean) -> Unit,
) = startBooleanToggle(translatable(key), currentValue)
    .setDefaultValue(false)
    .setSaveConsumer(onSave)
    .build()

internal fun ConfigEntryBuilder.optionalUsernameColorEntry(
    currentColor: String?,
    onSave: (String?) -> Unit,
) = startColorField(translatable("entry.chatbridge.badUsernameColor"), Integer.decode(currentColor ?: "#000000"))
    .setDefaultValue(0x000000)
    .setSaveConsumer { value -> onSave(if (value == 0x000000) null else value.toHexColor()) }
    .build()


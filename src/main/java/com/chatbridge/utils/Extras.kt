package com.chatbridge.utils

import com.chatbridge.config.ChatBridgeConfig.config
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.contents.PlainTextContents
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Extras {
    fun removeDiscordWarning(component: Component): MutableComponent? {
        val text = (component.contents as? PlainTextContents.LiteralContents)?.text
        if (text == "Please be mindful of Discord links in chat as they may pose a security risk") return null

        return component.copy().apply {
            siblings.clear()

            component.siblings.fold(mutableListOf<MutableComponent>()) { acc, child ->
                val contain = (child.contents as? PlainTextContents.LiteralContents)?.text
                val processed = removeDiscordWarning(child)

                when {
                    processed == null -> {
                        if (acc.lastOrNull()?.string == "\n") acc.removeLast()
                    }

                    contain == "\n" -> acc.add(Component.literal("\n"))
                    else -> processed.let { acc.add(it) }
                }
                acc
            }.forEach { append(it) }
        }
    }

    fun timestampComponent(): Component {
        val timestamp = config.extras.timestamp
        val formatted = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern(timestamp.format.replace("[", "'['").replace("]", "']'")))
        val result = Component.empty()
        var buffer = StringBuilder()
        var isDigitBuffer: Boolean? = null

        fun flushBuffer() {
            if (buffer.isNotEmpty()) {
                val color = if (isDigitBuffer == true) timestamp.numbersColor else timestamp.color
                result.append(
                    Component.literal(buffer.toString())
                        .withStyle { it.withColor(color.toColor()) }
                )
                buffer = StringBuilder()
            }
        }

        formatted.forEach { ch ->
            val isDigit = ch.isDigit()
            if (isDigitBuffer == null) isDigitBuffer = isDigit
            if (isDigit == isDigitBuffer) buffer.append(ch)
            else {
                flushBuffer()
                buffer.append(ch)
                isDigitBuffer = isDigit
            }
        }

        flushBuffer()

        return result
    }


    private fun String.toColor(): Int {
        return Integer.decode(this)
    }
}
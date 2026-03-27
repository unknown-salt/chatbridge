package com.chatbridge.utils

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.contents.PlainTextContents

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
}
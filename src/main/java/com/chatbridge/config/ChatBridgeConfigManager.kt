package com.chatbridge.config

import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component.translatable
import com.chatbridge.config.categories.GeneralCategory
import com.chatbridge.config.categories.OtherChatsCategory

object ChatBridgeConfigManager {
    fun build(parent: Screen?): Screen {
        val builder = ConfigBuilder.create()
        builder.parentScreen = parent
        builder.title = translatable("title.chatbridge.config")

        val general = builder.getOrCreateCategory(translatable("category.chatbridge.general"))
        val otherChats = builder.getOrCreateCategory(translatable("category.chatbridge.otherChats"))
        val entryBuilder = builder.entryBuilder()

        GeneralCategory().build(general, entryBuilder)
        OtherChatsCategory().build(otherChats, entryBuilder)

        builder.setSavingRunnable {
            ChatBridgeConfig.save()
        }

        return builder.build()
    }


}
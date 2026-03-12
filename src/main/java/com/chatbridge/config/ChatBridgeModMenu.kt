package com.chatbridge.config

import com.chatbridge.ChatBridge
import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi

class ChatBridgeModMenu : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent -> if (ChatBridge.hasCloth()) ChatBridgeConfigManager.build(parent) else null }
    }
}
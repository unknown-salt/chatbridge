package com.chatbridge.utils

import com.chatbridge.config.ChatBridgeConfigManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.client.Minecraft

class ChatBridgeCommands {

    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                literal("chatbridge").executes {
                    val client = Minecraft.getInstance()
                    val screen = ChatBridgeConfigManager.build(null)
                    client.schedule {
                        client.setScreen(screen)
                    }
                    1
                }
            )
        }
    }
}
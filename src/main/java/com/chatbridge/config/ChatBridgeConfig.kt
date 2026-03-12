package com.chatbridge.config

import com.chatbridge.ChatBridge.logger
import java.io.File
import com.google.gson.GsonBuilder

data class Config(
    var bridgeEnabled: Boolean = true,
    var botNames: List<String> = listOf(""),
    var hideBotName: Boolean = false,
    var prefix: String = "Bridge >",
    var prefixColor: String = "#616AC7",
    var nameColor: String = "#8F99FF",
    var messageColor: String = "#C1C3C7"
)

object ChatBridgeConfig {
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()
    private val file = File("config/chatbridge.json")

    var config = Config()

    fun save() {

        try {
            val json = gson.toJson(config)
            file.writeText(json)
        } catch (e: Exception) {
            logger.error("Encountered error whilst trying to save config JSON.", e)
        }
    }

    fun load() {
        if (!file.exists()) {
            logger.info("Config file not found, new created.")
            save()
            return
        }

        val json = file.readText()
        config = gson.fromJson(json, Config::class.java)
    }
}
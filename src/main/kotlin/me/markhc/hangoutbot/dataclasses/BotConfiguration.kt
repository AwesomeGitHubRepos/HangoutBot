package me.markhc.hangoutbot.dataclasses

import com.google.gson.Gson
import me.jakejmattson.discordkt.api.dsl.Data
import java.io.File

data class BotConfiguration(val token: String = "",
                            var prefix: String = "++",
                            val ownerId: String = "") : Data("config/config.json")

data class Properties(val version: String = "",
                      val discordkt: String = "",
                      val repository: String = "")

suspend fun loadConfig(onFinishedLoading: suspend (BotConfiguration?) -> Unit) {
    val configFile = File("config/config.json")

    if (!configFile.exists()) {
        return onFinishedLoading(null)
    }

    return onFinishedLoading(Gson().fromJson(configFile.readText(), BotConfiguration::class.java))
}
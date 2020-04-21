package me.markhc.tphbot;

import me.aberrantfox.kjdautils.api.dsl.PrefixDeleteMode
import me.aberrantfox.kjdautils.api.startBot;
import me.markhc.tphbot.services.*
import mu.KotlinLogging
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val logger = KotlinLogging.logger {}
    try {
        loadConfig {
            val configuration = it ?: throw Exception("Failed to parse configuration");

            startBot(configuration.token) {
                createDatabaseSchema(configuration)
                registerInjectionObject(configuration, logger)

                configure {
                    prefix = "++"
                    deleteMode = PrefixDeleteMode.None
                }
            }
        }
    } catch (e: Exception) {
        println(e.message)
        exitProcess(-1)
    }
}
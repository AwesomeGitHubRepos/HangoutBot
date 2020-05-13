package me.markhc.hangoutbot.arguments

import me.aberrantfox.kjdautils.api.dsl.command.CommandEvent
import me.aberrantfox.kjdautils.extensions.stdlib.trimToID
import me.aberrantfox.kjdautils.internal.command.*
import net.dv8tion.jda.api.entities.TextChannel

open class GuildTextChannelArg(override val name: String = "TextChannel") : ArgumentType<TextChannel>() {
    companion object : GuildTextChannelArg()

    override val consumptionType = ConsumptionType.Single

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<TextChannel> {
        val channel = tryRetrieveSnowflake(event.discord.jda) {
            it.getTextChannelById(arg.trimToID())
        } as TextChannel? ?: return ArgumentResult.Error("Couldn't retrieve text channel: $arg")

        if(channel.guild != event.guild)
            return ArgumentResult.Error("Couldn't retrieve text channel: $arg")

        return ArgumentResult.Success(channel)
    }

    override fun generateExamples(event: CommandEvent<*>) = listOf(event.channel.id)
}

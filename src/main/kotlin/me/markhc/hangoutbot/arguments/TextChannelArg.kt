package me.markhc.hangoutbot.arguments

import me.aberrantfox.kjdautils.api.dsl.command.CommandEvent
import me.aberrantfox.kjdautils.extensions.stdlib.trimToID
import me.aberrantfox.kjdautils.internal.command.*
import net.dv8tion.jda.api.entities.TextChannel

// Workaround for KUtils bug.
open class TextChannelArg(override val name : String = "TextChannel"): ArgumentType<TextChannel>() {
    companion object : TextChannelArg()

    override val consumptionType = ConsumptionType.Single
    override val examples: ArrayList<String>
        get() = arrayListOf("#general")

    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<TextChannel> {
        val channel = tryRetrieveSnowflake(event.discord.jda) {
            it.getTextChannelById(arg.trimToID())
        } as TextChannel? ?: return ArgumentResult.Error("Couldn't retrieve text channel: $arg")

        return ArgumentResult.Success(channel)
    }
}
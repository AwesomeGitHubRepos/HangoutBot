package me.markhc.hangoutbot.arguments

import me.aberrantfox.kjdautils.api.dsl.command.CommandEvent
import me.aberrantfox.kjdautils.extensions.stdlib.trimToID
import me.aberrantfox.kjdautils.internal.command.*
import net.dv8tion.jda.api.entities.Role

open class RoleArg(override val name : String = "Role", private val guildId: String = ""): ArgumentType<Role>() {
    companion object : RoleArg()

    override val consumptionType = ConsumptionType.Multiple
    override val examples: ArrayList<String>
        get() = arrayListOf("@Staff", "210017247028105985", "Community Member")
    override fun convert(arg: String, args: List<String>, event: CommandEvent<*>): ArgumentResult<Role> {

        val roleById = tryRetrieveSnowflake(event.discord.jda) {
            it.getRoleById(arg.trimToID())
        } as Role?

        if (roleById != null)
            return ArgumentResult.Success(roleById)

        val guild = if (guildId.isNotEmpty()) event.discord.jda.getGuildById(guildId) else event.guild
        guild ?: return ArgumentResult.Error("Failed to resolve guild! Pass a valid guild id to RoleArg.")

        var roles = guild.roles
        val roleBuilder = StringBuilder()
        fun String.startsWithIgnoreCase(string: String) = this.toLowerCase().startsWith(string.toLowerCase())

        //Consume arguments until only one role matches the filter
        args.takeWhile {
            val padding = if (roleBuilder.isNotEmpty()) " " else ""
            roleBuilder.append("$padding$it")
            roles = roles.filter { it.name.startsWithIgnoreCase(roleBuilder.toString()) }

            roles.size > 1
        }

        //Get the single role that survived filtering
        val resolvedRole = roles.firstOrNull() ?: return ArgumentResult.Error("Couldn't retrieve role :: $roleBuilder")
        val resolvedName = resolvedRole.name

        //Determine how many args this role would consume
        val lengthOfRole = resolvedName.split(" ").size

        //Check if the role that survived filtering matches the args given
        val argList = args.take(lengthOfRole)
        val isValid = resolvedName.toLowerCase() == argList.joinToString(" ").toLowerCase()

        return if (isValid) ArgumentResult.Success(resolvedRole, argList) else ArgumentResult.Error("Couldn't retrieve role :: $roleBuilder")
    }
}
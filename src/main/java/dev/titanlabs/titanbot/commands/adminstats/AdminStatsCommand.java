package dev.titanlabs.titanbot.commands.adminstats;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.commands.adminstats.subs.AdminStatsUserCommand;
import net.dv8tion.jda.api.entities.Member;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SimpleCommand;

public class AdminStatsCommand extends SimpleCommand {

    public AdminStatsCommand(TitanBot bot) {
        super(bot, "adminstats", bot.getGuild().getRoleById("686658320425025537"), false);

        this.setAliases("astats", "ainfo", "admininfo");
        this.setSubCommands(
                new AdminStatsUserCommand(bot)
        );
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        container.getChannel().sendMessage("-ainfo @User#1111").queue();
    }
}

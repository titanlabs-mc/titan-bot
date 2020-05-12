package dev.titanlabs.titanbot.commands;

import net.dv8tion.jda.api.entities.Member;
import pink.zak.simplediscord.bot.SimpleBot;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SimpleCommand;

public class HelpCommand extends SimpleCommand {

    public HelpCommand(SimpleBot bot) {
        super(bot, "help");
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        container.getChannel().sendMessage("I'm dumb. I cannot give help.").queue();
    }
}

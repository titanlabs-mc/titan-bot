package dev.titanlabs.titanbot.commands.buy;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.commands.buy.subs.BuyItemSub;
import net.dv8tion.jda.api.entities.Member;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SimpleCommand;

public class BuyCommand extends SimpleCommand {

    public BuyCommand(TitanBot bot) {
        super(bot, "buy", false);

        this.setSubCommands(
                new BuyItemSub(bot)
        );
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        container.getChannel().sendMessage("Usage: -buy <item>").queue();
    }
}

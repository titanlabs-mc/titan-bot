package dev.titanlabs.titanbot.commands.balance;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.cache.UserCache;
import dev.titanlabs.titanbot.objects.TitanUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SimpleCommand;

import java.awt.*;

public class BalanceCommand extends SimpleCommand {
    private final UserCache userCache;

    public BalanceCommand(TitanBot bot) {
        super(bot, "balance", false);
        this.userCache = bot.getUserCache();

        this.setAliases("bal", "research", "intel", "holiness"); // He he, just run holiness instead if you read this ;)
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        TitanUser user = this.userCache.getUser(sender.getId());
        container.getChannel().sendMessage(new EmbedBuilder()
                .setTitle(sender.getEffectiveName().concat("'s research"))
                .setThumbnail(sender.getUser().getAvatarUrl())
                .setColor(Color.CYAN)
                .setDescription(String.valueOf(user.getResearch()))
                .build()).queue();
    }
}

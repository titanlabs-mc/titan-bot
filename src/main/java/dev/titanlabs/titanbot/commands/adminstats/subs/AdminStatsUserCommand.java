package dev.titanlabs.titanbot.commands.adminstats.subs;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.cache.UserCache;
import dev.titanlabs.titanbot.objects.TitanUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SubCommand;

import java.awt.*;
import java.util.Optional;

public class AdminStatsUserCommand extends SubCommand {
    private final UserCache userCache;

    public AdminStatsUserCommand(TitanBot bot) {
        super(bot, false);
        this.userCache = bot.getUserCache();

        this.inheritPermission();
        this.addArgument(Member.class, "member");
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        Optional<Member> optionalTarget = this.parseArgument(args, container.getGuild(), 0);
        if (optionalTarget.isPresent()) {
            Member target = optionalTarget.get();
            TitanUser user = this.userCache.getUser(target.getId());
            container.getChannel().sendMessage(new EmbedBuilder()
                    .setTitle("Information on ".concat(target.getUser().getName()))
                    .setColor(Color.RED)
                    .addField("Ticket", user.getTicketChannelId().isPresent() ? "<#".concat(user.getTicketChannelId().get()).concat(">") : "N/A", true)
                    .addField("Tickets Opened", String.valueOf(user.getTicketsOpened()), true)
                    .addField("Research", String.valueOf(user.getResearch()), true)
                    .addField("Messages Sent", String.valueOf(user.getMessageAmount()), true)
                    .build()).queue();
            return;
        }
        container.getChannel().sendMessage("Could not get the user ".concat(args[0])).queue();
    }
}

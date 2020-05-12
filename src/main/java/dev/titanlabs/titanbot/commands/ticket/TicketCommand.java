package dev.titanlabs.titanbot.commands.ticket;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.cache.UserCache;
import dev.titanlabs.titanbot.commands.ticket.subs.TicketAddSub;
import dev.titanlabs.titanbot.commands.ticket.subs.TicketCloseSub;
import dev.titanlabs.titanbot.objects.TitanUser;
import dev.titanlabs.titanbot.service.TicketUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SimpleCommand;

import java.awt.*;
import java.util.Set;

public class TicketCommand extends SimpleCommand {
    private final UserCache userCache;
    private final TicketUtils ticketUtils;
    private final Set<Permission> ticketPermissions;


    public TicketCommand(TitanBot bot) {
        super(bot, "ticket", false);
        this.userCache = bot.getUserCache();
        this.ticketUtils = bot.getTicketUtils();
        this.ticketPermissions = ticketUtils.getTicketPermissions();

        this.setSubCommands(
                new TicketAddSub(bot),
                new TicketCloseSub(bot)
        );
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        TitanUser titanUser = this.userCache.getUser(sender.getId());
        User user = sender.getUser();
        if (titanUser.getTicketChannelId().isPresent()) {
            TextChannel ticketChannel = container.getGuild().getTextChannelById(titanUser.getTicketChannelId().get());
            container.getChannel().sendMessage(new EmbedBuilder()
                    .setTitle(":x: Ticket Already Open")
                    .setColor(Color.RED)
                    .setDescription("You already have a ticket open: ".concat(ticketChannel.getAsMention()).concat("\nPlease close the existing ticket or use the channel."))
                    .build()).queue();
        } else {
            String targetChannelName = "ticket-".concat(user.getName()).concat("-").concat(user.getDiscriminator());
            this.ticketUtils.getPendingCategory().createTextChannel(targetChannelName).addMemberPermissionOverride(sender.getIdLong(), this.ticketPermissions, null).queue(channel -> {
                container.getChannel().sendMessage(new EmbedBuilder()
                        .setTitle(":white_check_mark: Ticket Created")
                        .setColor(Color.GREEN)
                        .setDescription("Your ticket has been created: ".concat(channel.getAsMention()))
                        .build()).queue();
                titanUser.setTicketChannelId(channel.getId());
                channel.sendMessage("Thank you for creating a ticket ".concat(user.getAsMention()).concat(". We will be with you shortly.")).queue();
            });
            titanUser.getTicketsOpened().getAndIncrement();
        }
    }
}

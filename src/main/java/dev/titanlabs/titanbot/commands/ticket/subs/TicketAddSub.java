package dev.titanlabs.titanbot.commands.ticket.subs;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.cache.UserCache;
import dev.titanlabs.titanbot.objects.TitanUser;
import dev.titanlabs.titanbot.service.TicketUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SubCommand;

import java.util.Optional;
import java.util.Set;

public class TicketAddSub extends SubCommand {
    private final Guild guild;
    private final UserCache userCache;
    private final TicketUtils ticketUtils;
    private final Set<Permission> ticketPermissions;

    public TicketAddSub(TitanBot bot) {
        super(bot, false);
        this.guild = bot.getGuild();
        this.userCache = bot.getUserCache();
        this.ticketUtils = bot.getTicketUtils();
        this.ticketPermissions = this.ticketUtils.getTicketPermissions();

        this.addFlat("add");
        this.addArgument(Member.class, "user");
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        MessageChannel messageChannel = container.getChannel();
        TextChannel textChannel = this.guild.getTextChannelById(messageChannel.getId());
        TitanUser titanUser = this.userCache.getUser(sender.getId());
        if (!messageChannel.getName().contains("ticket-")) {
            messageChannel.sendMessage(":x: Please use this message in a ticket channel.").queue();
            return;
        }
        if (sender.getRoles().contains(ticketUtils.getTicketRole()) || (titanUser.hasOpenTicket() && messageChannel.getId().equals(titanUser.getTicketChannelId()))) {
            Optional<Member> optionalTarget = this.parseArgument(args, container.getGuild(), 1);
            if (!optionalTarget.isPresent()) {
                messageChannel.sendMessage(":x: There was an issue adding that user").queue();
                return;
            }
            Member target = optionalTarget.get();
            textChannel.createPermissionOverride(target).grant(this.ticketPermissions).queue(permissionOverride -> {
                messageChannel.sendMessage(":white_check_mark: ".concat(target.getAsMention()).concat(" has been added to the ticket.")).queue();
            });
            return;
        }
        messageChannel.sendMessage(":x: You must be the owner of the ticket to add another user.").queue();
    }
}

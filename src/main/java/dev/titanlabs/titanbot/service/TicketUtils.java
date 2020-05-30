package dev.titanlabs.titanbot.service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.cache.UserCache;
import dev.titanlabs.titanbot.objects.TitanUser;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.config.Config;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class TicketUtils {
    private final UserCache userCache;
    private final Map<String, Long> deleteConfirmation = Maps.newHashMap();
    private final Set<Permission> ticketPermissions = Sets.newHashSet();
    private final Role ticketRole;
    private final Category pendingCategory;
    private final Category answeredCategory;

    public TicketUtils(TitanBot bot) {
        Config settings = bot.getConfig("settings");
        Guild guild = bot.getGuild();
        this.userCache = bot.getUserCache();
        this.ticketRole = guild.getRoleById(settings.string("ticket-role-id"));
        this.pendingCategory = guild.getCategoryById(settings.string("pending-ticket-category"));
        this.answeredCategory = guild.getCategoryById(settings.string("answered-ticket-category"));

        for (String value : settings.stringList("ticket-permissions")) {
            this.ticketPermissions.add(Permission.valueOf(value));
        }
    }

    public void handleCloseRequest(Member sender, CommandContainer container) {
        TextChannel channel = (TextChannel) container.getChannel();
        TitanUser titanUser = this.userCache.getUser(sender.getId());
        if (channel.getName().contains("ticket-")) {
            if (titanUser.getTicketChannelId().isPresent() && channel.getId().equals(titanUser.getTicketChannelId().get())) {
                this.initiateOrConfirm(channel, channel, titanUser);
                return;
            }
            if (sender.getRoles().contains(this.ticketRole)) {
                AtomicReference<String> ticketOwnerId = new AtomicReference<>();
                this.userCache.modifyAllUsers(user -> {
                    if (user.getTicketChannelId().isPresent() && user.getTicketChannelId().get().equals(channel.getId())) {
                        ticketOwnerId.set(user.getId());
                        System.out.println("Match: ".concat(ticketOwnerId.get()));
                    }
                    return user;
                });
                this.initiateOrConfirm(channel, channel, this.userCache.getUser(ticketOwnerId.get()));
                return;
            }
            channel.sendMessage(":x: You cannot close this ticket.").queue();
        } else {
            if (titanUser.getTicketChannelId().isPresent()) {
                container.getGuild().getTextChannelById(titanUser.getTicketChannelId().get()).delete().queue();
                titanUser.setTicketChannelId("N/A");
                return;
            }
            channel.sendMessage(":x: You do not have a ticket open.").queue();
        }
    }

    private void initiateOrConfirm(TextChannel ticketChannel, TextChannel sendChannel, TitanUser channelOwner) {
        String ticketName = ticketChannel.getName();
        if (this.deleteConfirmation.containsKey(ticketName) && System.currentTimeMillis() - this.deleteConfirmation.get(ticketName) < 60000) {
            channelOwner.setTicketChannelId("N/A");
            ticketChannel.delete().queue();
            this.deleteConfirmation.remove(ticketName);
            return;
        }
        sendChannel.sendMessage(":clock1: Type `-close` within 60 seconds to confirm your action.")
                .queue(message -> this.deleteConfirmation.put(ticketChannel.getName(), System.currentTimeMillis()));
    }

    public Set<Permission> getTicketPermissions() {
        return this.ticketPermissions;
    }

    public Role getTicketRole() {
        return this.ticketRole;
    }

    public Category getPendingCategory() {
        return this.pendingCategory;
    }

    public Category getAnsweredCategory() {
        return this.answeredCategory;
    }
}

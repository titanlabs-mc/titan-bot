package dev.titanlabs.titanbot.service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.cache.UserCache;
import dev.titanlabs.titanbot.objects.TitanUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.config.Config;

import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class TicketUtils {
    private final UserCache userCache;
    private final TitanBot bot;
    private final Map<String, Long> deleteConfirmation = Maps.newHashMap();
    private final Set<Permission> ticketPermissions = Sets.newHashSet();
    private final Role ticketRole;
    private final Category pendingCategory;
    private final Category answeredCategory;

    public TicketUtils(TitanBot bot) {
        Config settings = bot.getConfig("settings");
        Guild guild = bot.getGuild();
        this.userCache = bot.getUserCache();
        this.bot = bot;
        this.ticketRole = guild.getRoleById(settings.string("ticket-role-id"));
        this.pendingCategory = guild.getCategoryById(settings.string("pending-ticket-category"));
        this.answeredCategory = guild.getCategoryById(settings.string("answered-ticket-category"));

        for (String value : settings.stringList("ticket-permissions")) {
            this.ticketPermissions.add(Permission.valueOf(value));
        }
    }

    public void handleOpenRequest(Member sender, CommandContainer container) {
        TitanUser titanUser = this.userCache.getUser(sender.getId());
        User user = sender.getUser();
        if (titanUser.hasOpenTicket()) {
            TextChannel ticketChannel = container.getGuild().getTextChannelById(titanUser.getTicketChannelId());
            if (ticketChannel == null) {
                titanUser.setTicketChannelId("N/A");
                this.handleOpenRequest(sender, container);
                return;
            }
            container.getChannel().sendMessage(new EmbedBuilder()
                    .setTitle(":x: Ticket Already Open")
                    .setColor(Color.RED)
                    .setDescription("You already have a ticket open: ".concat(ticketChannel.getAsMention()).concat("\nPlease close the existing ticket or use the channel."))
                    .build()).queue();
        } else {
            String targetChannelName = "ticket-".concat(user.getName()).concat("-").concat(user.getDiscriminator());
            this.getPendingCategory().createTextChannel(targetChannelName).addMemberPermissionOverride(sender.getIdLong(), this.ticketPermissions, null).queue(channel -> {
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

    public void handleCloseRequest(Member sender, CommandContainer container) {
        TextChannel channel = (TextChannel) container.getChannel();
        TextChannel targetChannel = channel;
        TitanUser titanUser = this.userCache.getUser(sender.getId());
        if (sender.getRoles().contains(this.ticketRole) && (!titanUser.hasOpenTicket() || !channel.getId().equals(titanUser.getTicketChannelId()))) {
            AtomicReference<String> ticketOwnerId = new AtomicReference<>();
            this.userCache.modifyAllUsers(user -> {
                if (user.hasOpenTicket() && user.getTicketChannelId().equals(channel.getId())) {
                    ticketOwnerId.set(user.getId());
                }
                return user;
            });
            if (ticketOwnerId.get() == null) {
                TitanBot.getLogger().error("Couldn't find ticket owner channel ".concat(channel.getId()));
                channel.sendMessage(":x: was unable to find the ticket owner.").queue();
                return;
            }
            this.closeTicketOrConfirm(channel, channel, this.userCache.getUser(ticketOwnerId.get()));
            return;
        }
        if (!titanUser.hasOpenTicket()) {
            channel.sendMessage(":x: You do not have a ticket open.").queue();
            return;
        }
        if (!channel.getId().equals(titanUser.getTicketChannelId())) {
            targetChannel = this.bot.getGuild().getTextChannelById(titanUser.getTicketChannelId());
        }
        if (targetChannel == null) {
            channel.sendMessage(":x: You cannot close this ticket.").queue();
            return;
        }
        this.closeTicketOrConfirm(targetChannel, channel, titanUser);
    }

    private void closeTicketOrConfirm(TextChannel ticketChannel, TextChannel sendChannel, TitanUser channelOwner) {
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

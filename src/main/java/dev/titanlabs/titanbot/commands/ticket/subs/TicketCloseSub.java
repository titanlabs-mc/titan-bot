package dev.titanlabs.titanbot.commands.ticket.subs;

import com.google.common.collect.Maps;
import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.cache.UserCache;
import dev.titanlabs.titanbot.objects.TitanUser;
import dev.titanlabs.titanbot.service.TicketUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SubCommand;

import java.util.Map;

public class TicketCloseSub extends SubCommand {
    private final UserCache userCache;
    private final TicketUtils ticketUtils;
    private Map<String, Long> pendingConfirmation = Maps.newHashMap();

    public TicketCloseSub(TitanBot bot) {
        super(bot, false);
        this.userCache = bot.getUserCache();
        this.ticketUtils = bot.getTicketUtils();

        this.addFlat("close");
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        TextChannel channel = (TextChannel) container.getChannel();
        TitanUser titanUser = this.userCache.getUser(sender.getId());
        if (channel.getName().contains("ticket-")) {
            if (sender.getRoles().contains(this.ticketUtils.getTicketRole())) {
                if (this.initiateOrConfirm(channel, channel)) {
                    channel.getHistoryFromBeginning(1).queue(messageHistory -> {
                        String id = messageHistory.getRetrievedHistory().get(0).getContentRaw().split("<")[1].substring(1, 19);
                        this.userCache.getUser(id).setTicketChannelId("N/A");
                    });
                }
                return;
            }
            if (titanUser.getTicketChannelId().isPresent() && channel.getId().equals(titanUser.getTicketChannelId().get())) {
                this.initiateOrConfirm(channel, channel);
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

    private boolean initiateOrConfirm(TextChannel ticketChannel, TextChannel sendChannel) {
        String ticketName = ticketChannel.getName();
        if (this.pendingConfirmation.containsKey(ticketName) && System.currentTimeMillis() - this.pendingConfirmation.get(ticketName) < 60000) {
            ticketChannel.delete().queue();
            this.pendingConfirmation.remove(ticketName);
            return true;
        }
        sendChannel.sendMessage(":clock1: Type -ticket close again within 60 seconds to confirm.")
                .queue(message -> this.pendingConfirmation.put(ticketChannel.getName(), System.currentTimeMillis()));
        return false;
    }
}

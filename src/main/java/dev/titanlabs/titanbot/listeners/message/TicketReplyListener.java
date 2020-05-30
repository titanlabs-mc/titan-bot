package dev.titanlabs.titanbot.listeners.message;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.service.TicketUtils;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class TicketReplyListener extends ListenerAdapter {
    private final TicketUtils ticketUtils;
    private final Role ticketRole;

    public TicketReplyListener(TitanBot bot) {
        this.ticketUtils = bot.getTicketUtils();
        this.ticketRole = this.ticketUtils.getTicketRole();
    }

    @Override
    @SubscribeEvent
    public void onMessageReceived(MessageReceivedEvent event) {
        GuildChannel channel = event.getTextChannel();
        if (channel.getName().startsWith("ticket-") && !event.getAuthor().isBot()
                && !event.getMessage().getContentRaw().startsWith("-ticket")
                && !event.getMessage().getContentRaw().startsWith("-t")
                && !event.getMessage().getContentRaw().startsWith("-close")) {
            for (Role role : event.getMember().getRoles()) {
                if (role.getId().equals(this.ticketRole.getId())) {
                    channel.getManager().setParent(this.ticketUtils.getAnsweredCategory()).queue();
                    return;
                }
            }
            channel.getManager().setParent(this.ticketUtils.getPendingCategory()).queue();
        }
    }
}

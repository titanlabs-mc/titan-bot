package dev.titanlabs.titanbot.commands.ticket.subs;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.service.TicketUtils;
import net.dv8tion.jda.api.entities.Member;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SubCommand;

public class TicketCloseSub extends SubCommand {
    private final TicketUtils ticketUtils;

    public TicketCloseSub(TitanBot bot) {
        super(bot, false);
        this.ticketUtils = bot.getTicketUtils();

        this.addFlat("close");
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        this.ticketUtils.handleCloseRequest(sender, container);
    }
}

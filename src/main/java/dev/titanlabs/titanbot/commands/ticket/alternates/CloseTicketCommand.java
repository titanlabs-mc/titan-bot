package dev.titanlabs.titanbot.commands.ticket.alternates;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.service.TicketUtils;
import net.dv8tion.jda.api.entities.Member;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SimpleCommand;

public class CloseTicketCommand extends SimpleCommand {
    private final TicketUtils ticketUtils;

    public CloseTicketCommand(TitanBot bot) {
        super(bot, "close", false);
        this.ticketUtils = bot.getTicketUtils();
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        this.ticketUtils.handleCloseRequest(sender, container);
    }
}

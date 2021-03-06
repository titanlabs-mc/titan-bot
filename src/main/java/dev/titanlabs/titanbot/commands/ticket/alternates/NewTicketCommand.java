package dev.titanlabs.titanbot.commands.ticket.alternates;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.service.TicketUtils;
import net.dv8tion.jda.api.entities.Member;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SimpleCommand;

public class NewTicketCommand extends SimpleCommand {
    private final TicketUtils ticketUtils;

    public NewTicketCommand(TitanBot bot) {
        super(bot, "new");
        this.ticketUtils = bot.getTicketUtils();
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        this.ticketUtils.handleOpenRequest(sender, container);
    }
}

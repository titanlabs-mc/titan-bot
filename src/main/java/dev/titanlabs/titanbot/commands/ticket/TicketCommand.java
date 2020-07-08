package dev.titanlabs.titanbot.commands.ticket;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.commands.ticket.subs.ForceSub;
import dev.titanlabs.titanbot.commands.ticket.subs.TicketAddSub;
import dev.titanlabs.titanbot.commands.ticket.subs.TicketCloseSub;
import dev.titanlabs.titanbot.service.TicketUtils;
import net.dv8tion.jda.api.entities.Member;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SimpleCommand;

public class TicketCommand extends SimpleCommand {
    private final TicketUtils ticketUtils;

    public TicketCommand(TitanBot bot) {
        super(bot, "ticket", false);
        this.ticketUtils = bot.getTicketUtils();

        this.setAliases("t");

        this.setSubCommands(
                new TicketAddSub(bot),
                new TicketCloseSub(bot),
                new ForceSub(bot)
        );
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        this.ticketUtils.handleOpenRequest(sender, container);
    }
}

package dev.titanlabs.titanbot.commands.ticket.subs;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.cache.UserCache;
import dev.titanlabs.titanbot.objects.TitanUser;
import dev.titanlabs.titanbot.service.TicketUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SubCommand;

import java.util.Optional;

public class ForceSub extends SubCommand {
    private final TicketUtils ticketUtils;
    private final UserCache userCache;

    public ForceSub(TitanBot bot) {
        super(bot, bot.getTicketUtils().getTicketRole(), false);
        this.ticketUtils = bot.getTicketUtils();
        this.userCache = bot.getUserCache();

        this.addFlat("force");
        this.addArgument(Member.class, "member");
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        Optional<Member> optionalMember = this.parseArgument(args, container.getGuild(), 1);
        optionalMember.ifPresent(member -> {
            User user = member.getUser();
            TitanUser titanUser = this.userCache.getUser(user.getId());
            if (titanUser.hasOpenTicket()) {
                container.getChannel().sendMessage("Failed. User already has ticket: ".concat("<#").concat(titanUser.getTicketChannelId()).concat(">")).queue();
                return;
            }
            String targetChannelName = "ticket-".concat(user.getName()).concat("-").concat(user.getDiscriminator());
            this.ticketUtils.getPendingCategory().createTextChannel(targetChannelName).addMemberPermissionOverride(user.getIdLong(), this.ticketUtils.getTicketPermissions(), null).queue(channel -> {
                titanUser.setTicketChannelId(channel.getId());
                channel.sendMessage("Ticket created ".concat(user.getAsMention()).concat(". We will be with you shortly.")).queue();
            });
        });
    }
}

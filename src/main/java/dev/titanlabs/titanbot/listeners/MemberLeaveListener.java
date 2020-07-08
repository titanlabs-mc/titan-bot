package dev.titanlabs.titanbot.listeners;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.cache.UserCache;
import dev.titanlabs.titanbot.objects.TitanUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

public class MemberLeaveListener extends ListenerAdapter {
    private final UserCache userCache;

    public MemberLeaveListener(TitanBot bot) {
        this.userCache = bot.getUserCache();
    }


    @Override
    @SubscribeEvent
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        User user = event.getUser();
        if (user.isBot()) {
            return;
        }
        TitanUser titanUser = this.userCache.getUser(event.getUser().getId());
        if (titanUser.hasOpenTicket()) {
            event.getGuild().getTextChannelById(titanUser.getTicketChannelId()).sendMessage(":warning: The ticket owner has left the Discord!").queue();
        }
        this.userCache.save(titanUser, true);
    }
}

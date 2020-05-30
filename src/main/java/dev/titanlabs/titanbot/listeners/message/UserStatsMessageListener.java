package dev.titanlabs.titanbot.listeners.message;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.cache.UserCache;
import dev.titanlabs.titanbot.objects.TitanUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class UserStatsMessageListener extends ListenerAdapter {
    private final UserCache userCache;

    public UserStatsMessageListener(TitanBot bot) {
        this.userCache = bot.getUserCache();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        if (author.isBot()) {
            return;
        }
        TitanUser user = this.userCache.getUser(author.getId());
        user.getMessageAmount().getAndIncrement();
    }
}

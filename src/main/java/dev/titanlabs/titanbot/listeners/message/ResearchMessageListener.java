package dev.titanlabs.titanbot.listeners.message;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.cache.UserCache;
import dev.titanlabs.titanbot.objects.TitanUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.ThreadLocalRandom;

public class ResearchMessageListener extends ListenerAdapter {
    private final UserCache userCache;

    public ResearchMessageListener(TitanBot bot) {
        this.userCache = bot.getUserCache();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User author = event.getAuthor();
        if (author.isBot()) {
            return;
        }
        TitanUser user = this.userCache.getUser(author.getId());
        if (user.getLastResearchGainTime() == 0 || System.currentTimeMillis() - user.getLastResearchGainTime() > 30000) {
            int researchToAdd = ThreadLocalRandom.current().nextInt(1, 5);
            TitanBot.getLogger().info("Gave user {} {} research. (Totals: {})", user.getId(), researchToAdd, user.getResearch() + researchToAdd);
            user.modifyResearch(research -> research += researchToAdd);
            user.updateLastResearchGainTime();
        }
    }
}

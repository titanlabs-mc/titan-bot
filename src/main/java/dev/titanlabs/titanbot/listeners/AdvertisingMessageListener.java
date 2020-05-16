package dev.titanlabs.titanbot.listeners;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.cache.AdvertisingCache;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AdvertisingMessageListener extends ListenerAdapter {
    private final AdvertisingCache advertisingCache;

    public AdvertisingMessageListener(TitanBot bot) {
        this.advertisingCache = bot.getAdvertisingCache();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentStripped();
        if (!message.contains("http") || event.getAuthor().isBot()) {
            return;
        }
        for (String tld : this.advertisingCache.getBlockedTlds()) {
            if (message.contains(tld)) {
                int tldIndex = message.indexOf(tld);
                event.getChannel().sendMessage(String.valueOf(tldIndex)).queue();

                int startIndex = tldIndex;
                char character = message.charAt(tldIndex);
                while (character != ' ') {
                    startIndex--;
                    character = message.charAt(startIndex);
                    event.getChannel().sendMessage("CHAR: ".concat(String.valueOf(character)));
                }
                String domain = message.substring(startIndex, tldIndex);
                event.getChannel().sendMessage("Final: ".concat(domain)).queue();
            }
        }
    }
}

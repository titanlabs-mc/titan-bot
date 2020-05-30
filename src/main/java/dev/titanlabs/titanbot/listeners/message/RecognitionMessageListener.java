package dev.titanlabs.titanbot.listeners.message;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.managers.RecognitionManager;
import dev.titanlabs.titanbot.recognition.RecognitionType;
import dev.titanlabs.titanbot.service.PasteUtils;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

import java.util.concurrent.TimeUnit;

public class RecognitionMessageListener extends ListenerAdapter {
    private final RecognitionManager recognitionManager;
    private final PasteUtils pasteUtils;
    private final Cache<String, String> pasteCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build();

    public RecognitionMessageListener(TitanBot bot) {
        this.recognitionManager = bot.getRecognitionManager();
        this.pasteUtils = bot.getPasteUtils();
    }

    @SneakyThrows
    @Override
    @SubscribeEvent
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }
        String messageToParse = event.getMessage().getContentStripped().toLowerCase();
        if (messageToParse.contains("pastebin.com/")) {
            String pasteId = messageToParse.split("pastebin.com")[1].replace("/", "").replace("\\", "");
            if (this.pasteCache.asMap().containsKey(pasteId)) {
                messageToParse = this.pasteCache.getIfPresent(pasteId);
            } else {
                messageToParse = this.pasteUtils.getRawPaste(pasteId).toLowerCase();
                this.pasteCache.put(pasteId, messageToParse);
            }
        }
        for (RecognitionType type : this.recognitionManager.getEnabledTypes().values()) {
            if (type.query(messageToParse)) {
                type.run(event.getTextChannel());
                return;
            }
        }
    }
}

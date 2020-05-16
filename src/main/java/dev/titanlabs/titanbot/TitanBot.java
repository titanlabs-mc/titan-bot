package dev.titanlabs.titanbot;

import com.google.common.collect.Sets;
import dev.titanlabs.titanbot.cache.AdvertisingCache;
import dev.titanlabs.titanbot.cache.UserCache;
import dev.titanlabs.titanbot.commands.HelpCommand;
import dev.titanlabs.titanbot.commands.recognition.RecognitionCommand;
import dev.titanlabs.titanbot.commands.ticket.TicketCommand;
import dev.titanlabs.titanbot.listeners.MemberLeaveListener;
import dev.titanlabs.titanbot.listeners.RecognitionMessageListener;
import dev.titanlabs.titanbot.listeners.TicketReplyListener;
import dev.titanlabs.titanbot.managers.RecognitionManager;
import dev.titanlabs.titanbot.registry.ArgumentRegistry;
import dev.titanlabs.titanbot.service.PasteUtils;
import dev.titanlabs.titanbot.service.TicketUtils;
import dev.titanlabs.titanbot.storage.UserStorage;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import pink.zak.simplediscord.bot.JdaBot;
import pink.zak.simplediscord.config.Config;

import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class TitanBot extends JdaBot {
    private UserStorage userStorage;
    private UserCache userCache;
    private AdvertisingCache advertisingCache;
    private Guild guild;
    private TicketUtils ticketUtils;
    private PasteUtils pasteUtils;
    private RecognitionManager recognitionManager;

    public TitanBot() {
        super(path -> path.resolve("bot-data"));
    }

    @SneakyThrows
    public void load() {
        this.getConfigStore().config("settings", Path::resolve, true)
                .config("links", Path::resolve, true);

        this.initialize(this.getConfig("settings").string("token"), "-", this.getIntents());
        this.getJda().getPresence().setActivity(Activity.watching("over the lab"));

        this.userStorage = new UserStorage(this);
        this.userCache = new UserCache(this);
        this.advertisingCache = new AdvertisingCache(this).cache();

        TimeUnit.SECONDS.sleep(5);
        this.guild = this.getJda().getGuildById("686657872481878080");
        this.ticketUtils = new TicketUtils(this);
        this.pasteUtils = new PasteUtils(this);

        this.recognitionManager = new RecognitionManager();
        this.registerRegistries(
                new ArgumentRegistry(this),
                this.recognitionManager
        );
        this.registerCommands(
                new RecognitionCommand(this),
                new TicketCommand(this),
                new HelpCommand(this)
        );
        this.registerListeners(

                new MemberLeaveListener(this),
                new TicketReplyListener(this),
                new RecognitionMessageListener(this)
        );
        System.out.println("Loaded Successfully");
    }

    public Set<GatewayIntent> getIntents() {
        Set<GatewayIntent> intents = Sets.newHashSet();
        intents.add(GatewayIntent.GUILD_MEMBERS);
        intents.add(GatewayIntent.GUILD_MESSAGES);
        return intents;
    }

    @Override
    public void unload() {
        System.out.println("Shutting down...");
        this.userCache.save(true);
        System.exit(0);
    }

    public Config getConfig(String name) {
        return this.getConfigStore().getConfig(name);
    }

    public UserStorage getUserStorage() {
        return this.userStorage;
    }

    public UserCache getUserCache() {
        return this.userCache;
    }

    public AdvertisingCache getAdvertisingCache() {
        return this.advertisingCache;
    }

    public Guild getGuild() {
        return this.guild;
    }

    public TicketUtils getTicketUtils() {
        return this.ticketUtils;
    }

    public PasteUtils getPasteUtils() {
        return this.pasteUtils;
    }

    public RecognitionManager getRecognitionManager() {
        return this.recognitionManager;
    }
}

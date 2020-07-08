package dev.titanlabs.titanbot;

import com.google.common.collect.Sets;
import dev.titanlabs.titanbot.cache.AdvertisingCache;
import dev.titanlabs.titanbot.cache.UserCache;
import dev.titanlabs.titanbot.commands.HelpCommand;
import dev.titanlabs.titanbot.commands.adminstats.AdminStatsCommand;
import dev.titanlabs.titanbot.commands.balance.BalanceCommand;
import dev.titanlabs.titanbot.commands.baltop.BalTopCommand;
import dev.titanlabs.titanbot.commands.buy.BuyCommand;
import dev.titanlabs.titanbot.commands.recognition.RecognitionCommand;
import dev.titanlabs.titanbot.commands.shop.ShopCommand;
import dev.titanlabs.titanbot.commands.ticket.TicketCommand;
import dev.titanlabs.titanbot.commands.ticket.alternates.CloseTicketCommand;
import dev.titanlabs.titanbot.commands.ticket.alternates.NewTicketCommand;
import dev.titanlabs.titanbot.listeners.MemberLeaveListener;
import dev.titanlabs.titanbot.listeners.message.RecognitionMessageListener;
import dev.titanlabs.titanbot.listeners.message.ResearchMessageListener;
import dev.titanlabs.titanbot.listeners.message.TicketReplyListener;
import dev.titanlabs.titanbot.listeners.message.UserStatsMessageListener;
import dev.titanlabs.titanbot.managers.RecognitionManager;
import dev.titanlabs.titanbot.managers.ShopItemManager;
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

public class TitanBot extends JdaBot {
    private UserStorage userStorage;
    private UserCache userCache;
    private AdvertisingCache advertisingCache;
    private Guild guild;
    private TicketUtils ticketUtils;
    private PasteUtils pasteUtils;
    private RecognitionManager recognitionManager;
    private ShopItemManager shopItemManager;

    public TitanBot() {
        super(path -> path);
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

        this.guild = this.getJda().getGuildById("686657872481878080");
        this.ticketUtils = new TicketUtils(this);
        this.pasteUtils = new PasteUtils(this);

        this.recognitionManager = new RecognitionManager();
        this.shopItemManager = new ShopItemManager(this);

        this.registerRegistries(
                new ArgumentRegistry(this),
                this.recognitionManager,
                this.shopItemManager
        );

        this.registerCommands(
                new AdminStatsCommand(this),
                new BalanceCommand(this),
                new BalTopCommand(this),
                new BuyCommand(this),
                new RecognitionCommand(this),
                new ShopCommand(this),
                new CloseTicketCommand(this),
                new NewTicketCommand(this),
                new TicketCommand(this),
                new HelpCommand(this)
        );

        this.registerListeners(
                new RecognitionMessageListener(this),
                new ResearchMessageListener(this),
                new MemberLeaveListener(this),
                new TicketReplyListener(this),
                new UserStatsMessageListener(this)
        );
        System.out.println("Loaded Successfully");
    }

    public Set<GatewayIntent> getIntents() {
        return Sets.newHashSet(
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES
        );
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

    public ShopItemManager getShopItemManager() {
        return this.shopItemManager;
    }
}

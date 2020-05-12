package dev.titanlabs.titanbot.registry;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.cache.UserCache;
import dev.titanlabs.titanbot.objects.TitanUser;
import pink.zak.simplediscord.registry.Registry;

import java.util.Optional;

public class ArgumentRegistry implements Registry {
    private final TitanBot bot;
    private final UserCache userCache;

    public ArgumentRegistry(TitanBot bot) {
        this.bot = bot;
        this.userCache = bot.getUserCache();
    }

    @Override
    public void register() {
        this.bot.getCommandBase()
                .registerArgumentType(TitanUser.class, (string, guild) -> {
                    String id = string.length() == 21 ? string.substring(2, 20) : string.length() == 22 ? string.substring(3, 21) : null;
                    return id == null ? Optional.empty() : Optional.of(this.userCache.getUser(string));
                });
    }
}

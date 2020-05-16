package dev.titanlabs.titanbot.cache;

import com.google.common.collect.Sets;
import dev.titanlabs.titanbot.TitanBot;
import pink.zak.simplediscord.config.Config;

import java.util.Set;

public class AdvertisingCache {
    private final TitanBot bot;
    private final Set<String> blockedTlds = Sets.newHashSet();
    private final Set<String> whitelistedUrls = Sets.newHashSet();

    public AdvertisingCache(TitanBot bot) {
        this.bot = bot;
    }

    public Set<String> getBlockedTlds() {
        return this.blockedTlds;
    }

    public Set<String> getWhitelistedUrls() {
        return this.whitelistedUrls;
    }

    public AdvertisingCache cache() {
        Config config = this.bot.getConfig("links");
        this.blockedTlds.addAll(config.stringList("tlds"));
        this.whitelistedUrls.addAll(config.stringList("whitelisted-domains"));
        return this;
    }
}

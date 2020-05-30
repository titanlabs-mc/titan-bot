package dev.titanlabs.titanbot.commands.baltop;

import com.google.common.collect.Maps;
import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.cache.UserCache;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SimpleCommand;

import java.awt.*;
import java.util.Collections;
import java.util.Map;

public class BalTopCommand extends SimpleCommand {
    private final TitanBot bot;
    private final UserCache userCache;
    private final Map<Integer, String> cachedLeaderboard = Maps.newTreeMap(Collections.reverseOrder());
    private long lastCacheTime;

    public BalTopCommand(TitanBot bot) {
        super(bot, "baltop", false);
        this.bot = bot;
        this.userCache = bot.getUserCache();

        this.setAliases("leaderboard", "lb");
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        if (this.cachedLeaderboard.isEmpty() || System.currentTimeMillis() - this.lastCacheTime > 300000) {
            this.cache();
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i <= 10 && this.cachedLeaderboard.size() >= i; i++) {
            int research = (Integer) this.cachedLeaderboard.keySet().toArray()[i - 1];
            builder.append(i)
                    .append(") <@")
                    .append(this.cachedLeaderboard.get(research))
                    .append("> - ")
                    .append(research)
                    .append(" research\n");
        }
        container.getChannel().sendMessage(new EmbedBuilder()
                .setTitle("Top Research Leaderboard")
                .setColor(Color.CYAN)
                .setDescription(builder.toString())
                .build()).queue();
    }

    private void cache() {
        this.cachedLeaderboard.clear();
        this.userCache.modifyAllUsers(titanUser -> {
            this.cachedLeaderboard.put(titanUser.getResearch(), titanUser.getId());
            return titanUser;
        });
        this.lastCacheTime = System.currentTimeMillis();
    }
}

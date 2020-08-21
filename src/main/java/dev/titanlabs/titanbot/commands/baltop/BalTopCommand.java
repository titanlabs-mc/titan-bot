package dev.titanlabs.titanbot.commands.baltop;

import com.google.common.collect.Lists;
import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.cache.UserCache;
import dev.titanlabs.titanbot.objects.TitanUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SimpleCommand;

import java.awt.*;
import java.util.List;


public class BalTopCommand extends SimpleCommand {
    private final UserCache userCache;
    private List<TitanUser> cachedLeaderboard = Lists.newArrayList();
    private long lastCacheTime;

    public BalTopCommand(TitanBot bot) {
        super(bot, "baltop", false);
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
            TitanUser user = this.cachedLeaderboard.get(i - 1);
            builder.append(i)
                    .append(") <@")
                    .append(user.getId())
                    .append("> - ")
                    .append(user.getResearch())
                    .append(" research\n");
        }
        container.getChannel().sendMessage(new EmbedBuilder()
                .setTitle("Top Research Leaderboard")
                .setColor(Color.CYAN)
                .setDescription(builder.toString())
                .build()).queue();
    }

    private void cache() {
        long startTime = System.currentTimeMillis();
        List<TitanUser> updatedLeaderboard = Lists.newArrayList();
        this.userCache.modifyAllUsers(titanUser -> {
            updatedLeaderboard.add(titanUser);
            return titanUser;
        });
        updatedLeaderboard.sort(TitanUser::compareTo);
        this.cachedLeaderboard = Lists.reverse(updatedLeaderboard);
        TitanBot.getLogger().info("Produced updated leaderboard in {}ms", System.currentTimeMillis() - startTime);
        this.lastCacheTime = System.currentTimeMillis();
    }
}

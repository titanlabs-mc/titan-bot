package dev.titanlabs.titanbot.service;

import com.google.common.collect.Sets;
import dev.titanlabs.titanbot.TitanBot;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import pink.zak.simplediscord.config.Config;

import java.util.Set;

public class TicketUtils {
    private final Set<Permission> ticketPermissions = Sets.newHashSet();
    private final Role ticketRole;
    private final Category pendingCategory;
    private final Category answeredCategory;

    public TicketUtils(TitanBot bot) {
        Config settings = bot.getConfig("settings");
        Guild guild = bot.getGuild();
        this.ticketRole = guild.getRoleById(settings.string("ticket-role-id"));
        this.pendingCategory = guild.getCategoryById(settings.string("pending-ticket-category"));
        this.answeredCategory = guild.getCategoryById(settings.string("answered-ticket-category"));

        for (String value : settings.stringList("ticket-permissions")) {
            this.ticketPermissions.add(Permission.valueOf(value));
        }
    }

    public Set<Permission> getTicketPermissions() {
        return this.ticketPermissions;
    }

    public Role getTicketRole() {
        return this.ticketRole;
    }

    public Category getPendingCategory() {
        return this.pendingCategory;
    }

    public Category getAnsweredCategory() {
        return this.answeredCategory;
    }
}

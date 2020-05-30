package dev.titanlabs.titanbot.shop.items;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.shop.ShopItem;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class VipShopItem extends ShopItem {
    private final Role vipRole;

    public VipShopItem(TitanBot bot, String name, String identifier, String description, int cost) {
        super(name, identifier, description, cost);
        this.vipRole = bot.getGuild().getRoleById("711294509287342170");
    }

    @Override
    public void onPurchase(Member member) {
        member.getGuild().addRoleToMember(member, this.vipRole).queue();
    }
}

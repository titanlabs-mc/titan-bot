package dev.titanlabs.titanbot.commands.shop;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.managers.ShopItemManager;
import dev.titanlabs.titanbot.shop.ShopItem;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SimpleCommand;

public class ShopCommand extends SimpleCommand {
    private final ShopItemManager shopItemManager;

    public ShopCommand(TitanBot bot) {
        super(bot, "shop", false);
        this.shopItemManager = bot.getShopItemManager();
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        StringBuilder builder = new StringBuilder();
        for (ShopItem item : this.shopItemManager.getShopItems()) {
            builder.append(item.getName())
                    .append(" - costs ")
                    .append(item.getCost())
                    .append(" research (-buy ")
                    .append(item.getIdentifier())
                    .append(") \n")
                    .append(item.getDescription());
        }
        container.getChannel().sendMessage(new EmbedBuilder()
                .setTitle("Research Shop")
                .setDescription(builder.toString())
                .build()).queue();
    }
}

package dev.titanlabs.titanbot.commands.buy.subs;

import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.cache.UserCache;
import dev.titanlabs.titanbot.managers.ShopItemManager;
import dev.titanlabs.titanbot.objects.TitanUser;
import dev.titanlabs.titanbot.shop.ShopItem;
import net.dv8tion.jda.api.entities.Member;
import pink.zak.simplediscord.command.CommandContainer;
import pink.zak.simplediscord.command.command.SubCommand;

import java.util.Optional;

public class BuyItemSub extends SubCommand {
    private final ShopItemManager shopItemManager;
    private final UserCache userCache;

    public BuyItemSub(TitanBot bot) {
        super(bot, false);
        this.shopItemManager = bot.getShopItemManager();
        this.userCache = bot.getUserCache();

        this.addArgument(String.class, "identifier");
    }

    @Override
    public void onExecute(Member sender, CommandContainer container, String[] args) {
        String identifier = this.parseArgument(args, container.getGuild(), 0);
        Optional<ShopItem> optionalShopItem = this.shopItemManager.getShopItem(identifier);
        if (optionalShopItem.isPresent()) {
            TitanUser user = this.userCache.getUser(sender.getId());
            ShopItem item = optionalShopItem.get();
            if (item.getCost() > user.getResearch()) {
                container.getChannel().sendMessage("You do not have enough research to purchase ".concat(item.getName())).queue();
                return;
            }
            item.onPurchase(sender);
            container.getChannel().sendMessage("You have successfully purchased ".concat(item.getName())
                    .concat(" for ").concat(String.valueOf(item.getCost())).concat(" research.")).queue();
            return;
        }
        container.getChannel().sendMessage("Could not find that item. See -shop.").queue();
    }
}

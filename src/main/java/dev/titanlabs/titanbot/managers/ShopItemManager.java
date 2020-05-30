package dev.titanlabs.titanbot.managers;

import com.google.common.collect.Maps;
import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.shop.ShopItem;
import dev.titanlabs.titanbot.shop.items.VipShopItem;
import pink.zak.simplediscord.registry.Registry;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class ShopItemManager implements Registry {
    private final TitanBot bot;
    private final Map<String, ShopItem> shopItemMap = Maps.newHashMap();

    public ShopItemManager(TitanBot bot) {
        this.bot = bot;
    }

    @Override
    public void register() {
        this.addShopItems(
                new VipShopItem(this.bot, "VIP", "vip", "- Get a special role\n- Viewed separately on the side", 5000)
        );
    }

    private void addShopItems(ShopItem... items) {
        for (ShopItem item : items) {
            this.shopItemMap.put(item.getIdentifier(), item);
        }
    }

    public Collection<ShopItem> getShopItems() {
        return this.shopItemMap.values();
    }

    public Optional<ShopItem> getShopItem(String identifier) {
        return this.shopItemMap.containsKey(identifier) ? Optional.of(this.shopItemMap.get(identifier)) : Optional.empty();
    }
}

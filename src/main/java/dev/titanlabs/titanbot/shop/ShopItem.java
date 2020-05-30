package dev.titanlabs.titanbot.shop;

import net.dv8tion.jda.api.entities.Member;

public abstract class ShopItem {
    private final String name;
    private final String identifier;
    private final String description;
    private final int cost;

    protected ShopItem(String name, String identifier, String description, int cost) {
        this.name = name;
        this.identifier = identifier;
        this.description = description;
        this.cost = cost;
    }

    public abstract void onPurchase(Member member);

    public String getName() {
        return this.name;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getDescription() {
        return this.description;
    }

    public int getCost() {
        return this.cost;
    }
}

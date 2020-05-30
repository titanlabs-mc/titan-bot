package dev.titanlabs.titanbot.objects;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

public class TitanUser {
    private final String id;
    private final AtomicInteger ticketsOpened;
    private final AtomicInteger messageAmount;
    private int research;
    private long lastResearchGainTime;
    private String ticketChannelId;

    public TitanUser(String id) {
        this.id = id;
        this.ticketsOpened = new AtomicInteger();
        this.messageAmount = new AtomicInteger();
        this.research = 0;
        this.ticketChannelId = "N/A";
    }

    public TitanUser(String id, int ticketsOpened, int messageAmount, int research, String ticketChannelId) {
        this.id = id;
        this.ticketsOpened = new AtomicInteger(ticketsOpened);
        this.messageAmount = new AtomicInteger(messageAmount);
        this.research = research;
        this.ticketChannelId = ticketChannelId;
    }

    public String getId() {
        return this.id;
    }

    public AtomicInteger getTicketsOpened() {
        return this.ticketsOpened;
    }

    public AtomicInteger getMessageAmount() {
        return this.messageAmount;
    }

    public void modifyResearch(UnaryOperator<Integer> research) {
        this.research = research.apply(this.research);
    }

    public int getResearch() {
        return this.research;
    }

    public long getLastResearchGainTime() {
        return this.lastResearchGainTime;
    }

    public void updateLastResearchGainTime() {
        this.lastResearchGainTime = System.currentTimeMillis();
    }

    public Optional<String> getTicketChannelId() {
        return this.ticketChannelId.equals("N/A") ? Optional.empty() : Optional.of(this.ticketChannelId);
    }

    public void setTicketChannelId(String id) {
        this.ticketChannelId = id;
    }

    public String getTicketChannelIdLegacy() {
        return this.ticketChannelId;
    }
}

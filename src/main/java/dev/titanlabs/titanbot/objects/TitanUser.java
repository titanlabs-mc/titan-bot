package dev.titanlabs.titanbot.objects;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

public class TitanUser implements Comparable<TitanUser> {
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

    public void modifyResearch(IntUnaryOperator research) {
        this.research = research.applyAsInt(this.research);
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

    public boolean hasOpenTicket() {
        return !this.ticketChannelId.equals("N/A");
    }

    public String getTicketChannelId() {
        return this.ticketChannelId;
    }

    public void setTicketChannelId(String id) {
        this.ticketChannelId = id;
    }

    @Override
    public int compareTo(@NotNull TitanUser otherUser) {
        return Integer.compare(this.research, otherUser.getResearch());
    }
}

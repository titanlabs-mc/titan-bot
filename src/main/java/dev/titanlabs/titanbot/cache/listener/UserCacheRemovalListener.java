package dev.titanlabs.titanbot.cache.listener;

import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.objects.TitanUser;
import dev.titanlabs.titanbot.storage.UserStorage;

public class UserCacheRemovalListener implements RemovalListener {
    private final UserStorage userStorage;
    private final boolean verbose;

    public UserCacheRemovalListener(TitanBot bot, boolean verbose) {
        this.userStorage = bot.getUserStorage();
        this.verbose = verbose;
    }

    @Override
    public void onRemoval(RemovalNotification notification) {
        if (notification.getCause() != RemovalCause.EXPIRED) {
            return;
        }
        TitanUser user = (TitanUser) notification.getValue();
        this.userStorage.save(user.getId(), user);
        if (this.verbose) {
            System.out.println("Saved cached user: ".concat(user.getId()));
        }
    }
}

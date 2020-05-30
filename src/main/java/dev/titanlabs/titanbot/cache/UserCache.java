package dev.titanlabs.titanbot.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.titanlabs.titanbot.TitanBot;
import dev.titanlabs.titanbot.cache.listener.UserCacheRemovalListener;
import dev.titanlabs.titanbot.objects.TitanUser;
import dev.titanlabs.titanbot.storage.UserStorage;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;
import java.util.function.UnaryOperator;

public class UserCache {
    private final UserStorage userStorage;
    private final Cache<String, TitanUser> userCache;

    public UserCache(TitanBot bot) {
        this.userStorage = bot.getUserStorage();
        this.userCache = CacheBuilder
                .newBuilder().expireAfterAccess(15, TimeUnit.MINUTES)
                .removalListener(new UserCacheRemovalListener(bot, true))
                .build();
    }

    @SneakyThrows
    public TitanUser getUser(String id) {
        return this.userCache.get(id, () -> {
            TitanUser titanUser = this.userStorage.load(id);
            return titanUser == null ? new TitanUser(id) : titanUser;
        });
    }

    public void modifyAllUsers(UnaryOperator<TitanUser> userOperator) {
        for (TitanUser cachedUser : this.userCache.asMap().values()) {
            userOperator.apply(cachedUser);
            this.userStorage.save(cachedUser.getId(), cachedUser);
        }
        for (TitanUser savedUser : this.userStorage.loadAll()) {
            if (this.userCache.getIfPresent(savedUser.getId()) == null) {
                userOperator.apply(savedUser);
                this.userStorage.save(savedUser.getId(), savedUser);
            }
        }
    }

    public void save(TitanUser user, boolean verbose) {
        this.userCache.invalidate(user.getId());
        this.userStorage.save(user.getId(), user);
        if (verbose) {
            System.out.println("Saved cached user: ".concat(user.getId()));
        }
    }

    public void save(boolean verbose) {
        for (TitanUser user : this.userCache.asMap().values()) {
            this.userStorage.save(user.getId(), user);
            this.userCache.invalidate(user.getId());
            if (verbose) {
                System.out.println("Saved cached user: ".concat(user.getId()));
            }
        }
    }
}

package chicken.butt.Utility;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import chicken.butt.App;

public class Cache {    
    private static HashMap<Long, String> usernames = new HashMap<Long, String>();

    private static final transient ZoneId zoneId = ZoneId.of("America/Los_Angeles");
    private static ScheduledExecutorService autoClear = Executors.newSingleThreadScheduledExecutor();
    private static Runnable clearCache = () -> {
        usernames.clear();
    };
    public Cache() {
        autoClear.scheduleWithFixedDelay(clearCache, LocalDate.now().atStartOfDay(zoneId).plusDays(1L).toEpochSecond()-Instant.now().getEpochSecond(), 1, TimeUnit.DAYS);
    }

    /**
     * @param userID
     * @return Users last cached username (cache resets daily)
     */
    public static String getUsername(long userID) {
        return usernames.computeIfAbsent(userID, k -> {
            try {
                return App.api.getUserById(userID).get().getName();
            } catch (InterruptedException | ExecutionException e) {
                return "???";
            }
        });
    }
}

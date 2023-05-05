package chicken.butt;

import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class AppTest 
{
    private static final transient ZoneId zoneId = ZoneId.of("America/Los_Angeles");
    private static transient ScheduledExecutorService autoClearDailyData = Executors.newSingleThreadScheduledExecutor();
    private static transient Runnable resetTodaysData = () -> {
        System.out.println("reset");
    };
    private static transient Runnable initialDayReset = () -> {
        System.out.println("reset");
        autoClearDailyData.scheduleAtFixedRate(resetTodaysData, 5, 5, TimeUnit.SECONDS);
    };
    public static void main(String[] args) {
        //autoClearDailyData.schedule(initialDayReset, , TimeUnit.SECONDS);
        System.out.println(Instant.now().getEpochSecond());
        System.out.println(LocalDate.now().atStartOfDay(zoneId).plusDays(1L).toEpochSecond());
        System.out.println(LocalDate.now().atStartOfDay(zoneId).plusDays(1L).toEpochSecond()-Instant.now().getEpochSecond());
    }
}

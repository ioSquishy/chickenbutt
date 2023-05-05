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

import org.apache.commons.math3.stat.descriptive.SynchronizedSummaryStatistics;
import org.junit.Test;

public class AppTest 
{
    public static void main(String[] args) {
        String number = "3";
        int answer = Integer.parseInt(number)-1;

        System.out.println(answer);
    }
}

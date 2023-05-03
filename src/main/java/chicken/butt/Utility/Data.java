package chicken.butt.Utility;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;

import chicken.butt.App;

public class Data implements Serializable {
    private static final long serialVersionUID = 1;
    
    public TreeMap<Long, BRData> getAllData() {
        TreeMap<Long, BRData> data = new TreeMap<>();
        for (UserData user : App.users) {
            data.putAll(user.getUserData());
        }
        return data;
    }

    public NavigableMap<Long, BRData> getDataBetween(long epochA, long epochB) {        
        return getAllData().subMap(epochA, true, epochB, true);
    }

    public NavigableMap<Long, BRData> getDataLastWeek() {
        long timeB = Instant.now().toEpochMilli();
        long timeA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timeB), ZoneId.of("America/Los_Angeles")).minusWeeks(1L).toInstant().toEpochMilli();

        return getDataBetween(timeA, timeB);
    }
    
}

package chicken.butt;

import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.junit.Test;

public class AppTest 
{
    public static void main(String[] args) {
        Instant start = Instant.now();
        NavigableMap<Long, String> mapA = new TreeMap<Long, String>();
        mapA.put(100010L, "a");
        mapA.put(100011L, "b");
        mapA.put(100012L, "c");
        mapA.put(100013L, "d");
        mapA.put(100014L, "e");
        mapA.put(100015L, "f");
        NavigableMap<Long, String> submapA = mapA.subMap(100L, true, 100019L, true);

        NavigableMap<Long, String> mapB = new TreeMap<Long, String>();
        mapB.put(100016L, "s");
        mapB.put(100017L, "q");
        mapB.put(100018L, "u");
        mapB.put(100019L, "i");
        mapB.put(100020L, "s");
        mapB.put(100021L, "h");
        NavigableMap<Long, String> submapB = mapB.subMap(100L, true, 100019L, true);

        TreeMap<Long, String> combinedMap = new TreeMap<Long, String>();
        combinedMap.putAll(submapA);
        combinedMap.putAll(submapB);

        System.out.println(Instant.now().getNano() - start.getNano());

        for (Entry<Long, String> entry : combinedMap.entrySet()) {
            System.out.println(entry.getValue());
        }

        System.out.println("\n");
        //aaaa
        Instant start2 = Instant.now();

        TreeMap<Long, String> tMapA = new TreeMap<Long, String>();
        tMapA.put(100010L, "a");
        tMapA.put(100011L, "b");
        tMapA.put(100012L, "c");
        tMapA.put(100013L, "d");
        tMapA.put(100014L, "e");
        tMapA.put(100015L, "f");

        TreeMap<Long, String> tMapB = new TreeMap<Long, String>();
        tMapB.put(100016L, "s");
        tMapB.put(100017L, "q");
        tMapB.put(100018L, "u");
        tMapB.put(100019L, "i");
        tMapB.put(100020L, "s");
        tMapB.put(100021L, "h");

        TreeMap<Long, String> combinedTMap = new TreeMap<Long, String>();
        combinedTMap.putAll(tMapA);
        combinedTMap.putAll(tMapB);

        NavigableMap<Long, String> subTMap = combinedTMap.subMap(100L, true, 100019L, true);

        System.out.println(Instant.now().getNano() - start2.getNano());

        for (Entry<Long, String> entry : subTMap.entrySet()) {
            System.out.println(entry.getValue());
        }
    }
}

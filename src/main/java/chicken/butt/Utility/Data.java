package chicken.butt.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;

import chicken.butt.App;
import chicken.butt.Commands.BRSheet;

public class Data implements Serializable {
    private static final long serialVersionUID = 0;
    private static final transient ZoneId zoneId = ZoneId.of("America/Los_Angeles");
    private static HashMap<Long, UserData> allUserData = new HashMap<>();
    private static transient TreeMap<Long, BRData> todaysData = new TreeMap<Long, BRData>();
    private static transient TreeMap<Long, BRData> deletedEntries = new TreeMap<>();

    private static transient ScheduledExecutorService autoSave = Executors.newSingleThreadScheduledExecutor();
    private static transient ScheduledExecutorService autoClearDailyData = Executors.newSingleThreadScheduledExecutor();
    private static transient Runnable backup = () -> {
        try {
            saveData();
        } catch(IOException e) {
            App.api.getOwner().get().join().sendMessage("could save data :0").join();
        }
    };
    private static transient Runnable resetTodaysData = () -> {
        todaysData.clear();
        BRSheet.updateEmbed();
    };
    private static transient Runnable initialDayReset = () -> {
        todaysData.clear();
        autoClearDailyData.scheduleAtFixedRate(resetTodaysData, 1, 1, TimeUnit.DAYS);
    };

    public Data() {
        try {
            retrieveData();
        } catch (ClassNotFoundException | IOException e) {
            App.api.getOwner().get().join().sendMessage("could not retrieve data :(").join();
        }
        autoSave.scheduleWithFixedDelay(backup, 1, 1, TimeUnit.MINUTES);
        autoClearDailyData.schedule(initialDayReset, LocalDate.now().atStartOfDay(zoneId).plusDays(1L).toEpochSecond()-Instant.now().getEpochSecond(), TimeUnit.SECONDS);
    }
    private static void saveData() throws IOException {
        File file = new File("userData.ser");
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(allUserData);
        out.close();
        fos.close();
    }
    private static void retrieveData() throws ClassNotFoundException, IOException {
        FileInputStream fin = new FileInputStream("userData.ser");
        ObjectInputStream in = new ObjectInputStream(fin);
        allUserData = (HashMap) in.readObject();
        in.close();
        fin.close();

        System.out.println("data retrieved");
    }

    //Chicken Butt
    public static void addChickenButt(long userID) {
        getUserData(userID).addButt();
    }
    /**
     * 
     * @return
     * String = username
     * Integer = chicken butts
     */
    public static LinkedHashMap<String, Integer> getChickenRanks() {
        ArrayList<UserData> unsortedData = new ArrayList<>();
        allUserData.forEach((userId, userData) -> {
            unsortedData.add(userData);
        });

        ArrayList<UserData> sortedData = new ArrayList<>();

        while (unsortedData.size() > 0) {
            UserData highest = unsortedData.get(0);
            int hiIn = 0;
            for (int j = 0; j < unsortedData.size(); j++) {
                if (unsortedData.get(j).getChickenButts() > highest.getChickenButts()) {
                    highest = unsortedData.get(j);
                    hiIn = j;
                }

            }
            unsortedData.remove(hiIn);
            sortedData.add(highest);
        }

        LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<>();
        sortedData.forEach(userData -> {
            sortedMap.put(App.api.getUserById(userData.getUserID()).join().getName(), userData.getChickenButts());
        });
        return sortedMap;
    }
    
    /**
     * Gets users data and adds them to HashMap if needed
     * @param userID
     * @return
     * UserData
     */
    public static UserData getUserData(long userID) {
        UserData data = allUserData.putIfAbsent(userID, new UserData(userID));
        return data != null ? data : allUserData.get(userID);
    }

    //Bathroom Data
    public static void brbUpdate(long userID) {
        BRData data = getUserData(userID).switchSign();
        todaysData.putIfAbsent(data.getEpochID(), data);
        BRSheet.updateEmbed();
    }
    public static void removeLastBRB(long userID) {
        UserData userData = getUserData(userID);
        Entry<Long, BRData> lastEntry = userData.removeLastEntry();
        
        if (lastEntry != null) {
            todaysData.remove(lastEntry.getKey());
            BRSheet.updateEmbed();
        }
    }

    public static boolean removeEntry(long epochID) {
        BRData entry = getUserData(getAllData().get(epochID).getUserID()).removeEntry(epochID);
        if (entry != null) {
            deletedEntries.put(entry.getEpochID(), entry);
            return true;
        } else {
            return false;
        }
    }
    public static TreeMap<Long, BRData> getDeletedData() {
        return deletedEntries;
    }
    public static boolean retrieveEntry(long epochID) {
        BRData entry = deletedEntries.remove(epochID);
        if (entry != null) {
            getUserData(entry.getUserID()).addEntry(entry);
            return true;
        } else {
            return false;
        }
    }

    public static TreeMap<Long, BRData> getAllData() {
        TreeMap<Long, BRData> data = new TreeMap<>();
        allUserData.forEach((userId, userData) -> {
            data.putAll(userData.getBRData());
        });
        return data;
    }

    /**
     * epochA <= data <= epochB
     * @param epochA Epoch in millis to get data from
     * @param epochB Epoch in millis to get data to
     * @return
     * NavigableMap<epochID, BRData> of all data between epochs
     */
    public static NavigableMap<Long, BRData> getDataBetween(long epochA, long epochB) {        
        return getAllData().subMap(epochA, true, epochB, true);
    }

    /**
     * 
     * @return
     * All user data from a week ago to when the function is run.
     */
    public static NavigableMap<Long, BRData> getDataLastWeek() {
        long timeB = Instant.now().toEpochMilli();
        long timeA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timeB), zoneId).minusWeeks(1L).toInstant().toEpochMilli();

        return getDataBetween(timeA, timeB);
    }

    public static TreeMap<Long, BRData> getTodaysData() {
        return todaysData;
    }

    /**
     * 
     * @return
     * All user data from the start of the day the function is run.
     */
    public static NavigableMap<Long, BRData> getDayData(int dayA, int dayB) {
        long timeA = LocalDate.now().atStartOfDay(zoneId).toInstant().toEpochMilli();
        long timeB = Instant.now().toEpochMilli();
        return getDataBetween(timeA, timeB);
    }

    /**
     * 
     * @return
     * Long = userID
     * ArrayList<BRData> = respectives users data
     */
	public static HashMap<Long, ArrayList<BRData>> getDataLastWeekPerUser() {
		HashMap<Long, ArrayList<BRData>> result = new HashMap<>();
		getDataLastWeek().forEach((epochID, brData) -> {
			result.putIfAbsent(brData.getUserID(), new ArrayList<BRData>());
            result.get(brData.getUserID()).add(brData);
		});
        return result;
	}

    /**
     * 
     * @param zdt
     * @return zdt in the format: hr:minute am/pm
     */
    public static String formatTime(ZonedDateTime zdt) {
        return zdt.format(DateTimeFormatter.ofPattern("h:mm a"));
    }

    
}

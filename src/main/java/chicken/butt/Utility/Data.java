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
import java.util.ArrayList;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;

import chicken.butt.App;

public class Data implements Serializable {
    private static final long serialVersionUID = 0;
    private static final transient ZoneId zoneId = ZoneId.of("America/Los_Angeles");
    private static HashMap<Long, UserData> allUserData = new HashMap<>();
    private static transient HashMap<Long, ArrayList<BRData>> todaysData = new HashMap<>();

    private static transient ScheduledExecutorService autoSave = Executors.newSingleThreadScheduledExecutor();
    private static transient Runnable backup = () -> {
        try {
            saveData();
        } catch(IOException e) {
            App.api.getOwner().get().join().sendMessage("could save data :0").join();
        }
    };

    public Data() {
        try {
            retrieveData();
        } catch (ClassNotFoundException | IOException e) {
            App.api.getOwner().get().join().sendMessage("could not retrieve data :(").join();
        }
        autoSave.scheduleWithFixedDelay(backup, 1, 1, TimeUnit.MINUTES);
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
        ArrayList<UserData> sortedData = new ArrayList<>();
        allUserData.forEach((userId, userData) -> {
            unsortedData.add(userData);
        });

        for(int i = 0; i < unsortedData.size(); i++) {
            if (sortedData.isEmpty()) {
                sortedData.add(unsortedData.get(0));
            } else {
                //sort
                int highest = sortedData.get(0).getChickenButts();
                int lowest = sortedData.get(i-1).getChickenButts();
                int current = unsortedData.get(i).getChickenButts();

                if (current > highest) {
                    
                }
            }
        }

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
        getUserData(userID).switchSign();
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

    /**
     * 
     * @return
     * All user data from the start of the day the function is run.
     */
    public static NavigableMap<Long, BRData> getTodaysData() {
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

    
}

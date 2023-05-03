package chicken.butt.Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;

import chicken.butt.App;

public class Data implements Serializable {
    private static final long serialVersionUID = 0;
    private static HashMap<Long, UserData> users = new HashMap<>();

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
        autoSave.scheduleWithFixedDelay(backup, 1, 30, TimeUnit.MINUTES);
    }
    private static void saveData() throws IOException {
        File file = new File("userData.ser");
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(users);
        out.close();
        fos.close();
    }
    private static void retrieveData() throws ClassNotFoundException, IOException {
        FileInputStream fin = new FileInputStream("userData.ser");
        ObjectInputStream in = new ObjectInputStream(fin);
        users = (HashMap) in.readObject();
        in.close();
        fin.close();

        System.out.println("data retrieved");
    }
    
    /**
     * Gets users data and adds them to HashMap if needed
     * @param userID
     * @return
     * UserData
     */
    public static UserData getUserData(long userID) {
        UserData data = users.putIfAbsent(userID, new UserData(userID));
        return data != null ? data : users.get(userID);
    }

    public static void brbUpdate(long userID) {
        getUserData(userID).switchSign();
    }


    public static TreeMap<Long, BRData> getAllData() {
        TreeMap<Long, BRData> data = new TreeMap<>();
        users.forEach((userId, userData) -> {
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
        long timeA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timeB), ZoneId.of("America/Los_Angeles")).minusWeeks(1L).toInstant().toEpochMilli();

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * reads the data files and passes them back to the map
 */
public class Reader {

    private HashMap<String, Stop> stopIDMap = new HashMap<>();
    private BufferedReader reader;

    public ArrayList<Trip> readTrips(File file){
        ArrayList<Trip> setOfTrips = new ArrayList<>();
        ArrayList<Stop> stops = new ArrayList<>();
        try{
            reader = new BufferedReader(new FileReader(file));
            reader.readLine();
            reader.lines().forEach(line -> {
                String[] data = line.split("\t");
                List<String> stopList = new ArrayList<>(Arrays.asList(data));
                stopList.remove(0);
                stopList.forEach(i -> {
                    stops.add(stopIDMap.get(i));
                });

                setOfTrips.add(new Trip(data[0], (ArrayList) stops.clone()));
                stops.clear();
                    });
        reader.close();
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.toString());
        }

        return setOfTrips;
    }

    public ArrayList<Stop> readStops(File file){
        ArrayList<Stop> setOfStops = new ArrayList<>();
        try{
            reader = new BufferedReader(new FileReader(file));
            reader.readLine();
            reader.lines().forEach(line -> {
                String[] data = line.split("\t");
                Location stopLocation = Location.newFromLatLon(Double.parseDouble(data[2]), Double.parseDouble(data[3]));
                Stop newStop = new Stop(data[0], stopLocation, data[1].toLowerCase());
                setOfStops.add(newStop);
                stopIDMap.put(newStop.getID(), newStop);
            });
        reader.close();
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.toString());

        }
        return setOfStops;
    }
}

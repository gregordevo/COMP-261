import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Reader {

    private HashMap<String, Stop> stopIDMap = new HashMap<>();
    private BufferedReader reader;

    public Set<Trip> readTrips(File file){
        Set<Trip> setOfTrips = new HashSet<>();
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

                setOfTrips.add(new Trip(data[0],stops));
                    });
        reader.close();
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.toString());
        }

        return setOfTrips;
    }

    public Set<Stop> readStops(File file){
        Set<Stop> setOfStops = new HashSet<>();
        try{
            reader = new BufferedReader(new FileReader(file));
            reader.readLine();
            reader.lines().forEach(line -> {
                String[] data = line.split("\t");
                Location stopLocation = Location.newFromLatLon(Double.parseDouble(data[2]), Double.parseDouble(data[3]));
                Stop newStop = new Stop(data[0], stopLocation, data[1]);
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

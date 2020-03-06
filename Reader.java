import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class Reader {

    private BufferedReader reader;

    public Set<Trip> readTrips(File file){
        Set<Trip> setOfTrips = new HashSet<>();
        try{
            reader = new BufferedReader(new FileReader(file));
            reader.readLine();
            reader.lines().forEach(line -> {
                String[] data = line.split("/t");
                ArrayList<String> stopList = (ArrayList) Arrays.asList(data);
                stopList.remove(0);
                setOfTrips.add(new Trip(data[0], stopList)); // need to turn stop list from strings into actual stops.
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
                String[] data = line.split("/t");
                Location stopLocation = Location.newFromLatLon(Double.parseDouble(data[2]), Double.parseDouble(data[3]));
                setOfStops.add(new Stop(data[0], stopLocation, data[1]));
            });
        reader.close();
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.toString());
        }
        return setOfStops;
    }
}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

public class Reader {

    private BufferedReader reader;

    public Set<Trip> readTrips(File file){
        Set<Trip> setOfTrips = new HashSet<>();
        try{
            reader = new BufferedReader(new FileReader(file));
            reader.readLine();
            reader.lines().forEach(line -> {
                String[] data = line.split("/t");

                    });

        } catch (Exception e) {

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
                Location stopLocation = Location.newFromLatLon(Double.parseDouble(data[3]), Double.parseDouble(data[4]));
                setOfStops.add(new Stop(data[1], stopLocation, data[2]));
            });

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.toString());
        }
        return setOfStops;
    }
}

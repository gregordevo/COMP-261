import java.util.Set;

public class Trip {

    private final String id;
    private final Set<Stop> stops;

    public Trip(String id, Set<Stop> stops) {
        this.id = id;
        this.stops = stops;
    }


    public String getId() {
        return id;
    }

    public Set<Stop> getStops() {
        return stops;
    }
}


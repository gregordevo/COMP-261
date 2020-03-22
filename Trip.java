import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Trip extends Drawable {

    private final List<Stop> stops;
    private final List<Connection> connections = new ArrayList<>();

    public Trip(String id, List<Stop> stops) {
        this.ID = id;
        this.stops = stops;
        for(int i =1; i < stops.size(); i++){
            Connection newCon =  new Connection(stops.get(i-1), stops.get(i));
            connections.add(newCon);
        }
    }

    public List<Stop> getStops() {
        return stops;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(colour);
        for(Connection c : connections) c.draw(g);
    }
}


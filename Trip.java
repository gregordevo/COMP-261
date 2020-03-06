import java.awt.*;
import java.util.List;
import java.util.Set;

public class Trip extends Drawable {

    private final List<Stop> stops;

    public Trip(String id, List<Stop> stops) {
        this.ID = id;
        this.stops = stops;
    }

    public List<Stop> getStops() {
        return stops;
    }

    @Override
    public void draw(Graphics g) {
        for(int i = 1; i < stops.size(); i++){
            Point firstPoint = stops.get(i-1).getPoint();
            Point secondPoint = stops.get(i).getPoint();
            g.drawLine((int)firstPoint.getX(), (int)firstPoint.getY(), (int)secondPoint.getX(), (int)secondPoint.getY());
        }
    }
}


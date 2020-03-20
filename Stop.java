import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.awt.*;

public class Stop extends Drawable implements Locatable {

    private final static int STOP_SIZE = 4;
    private final Location location;
    private final String name;
    private Point point;

    public Stop(String id, Location loc, String name) {
        this.ID = id;
        this.location = loc;
        this.name = name;
        this.point = location.asPoint(origin, scale);
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString(){return name; }

    @Override
    public void draw(Graphics g) {
        Point point = location.asPoint(origin, scale);
        g.setColor(colour);
        g.fillRect((int)point.getX(), (int)point.getY(), STOP_SIZE, STOP_SIZE);
    }

    public Point getPoint(){
        this.point = location.asPoint(origin, scale);
        return this.point;
    }


}

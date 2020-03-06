import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.awt.*;

public class Stop extends Drawable {

    private final static int STOP_SIZE = 4;
    private final Location location;
    private final String name;

    public Stop(String id, Location loc, String name) {
        this.ID = id;
        this.location = loc;
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    @Override
    public void draw(Graphics g) {
        Point point = location.asPoint(origin, scale);
        g.setColor(colour);
        g.fillRect((int)point.getX(), (int)point.getY(), STOP_SIZE, STOP_SIZE);
    }

    public Point getPoint(){
        return location.asPoint(origin, scale);
    }
}

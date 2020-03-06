import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashSet;

public class Map extends GUI {

    private Reader read;
    private HashSet<Trip> tripSet;
    private HashSet<Stop> stopSet;


    @Override
    protected void redraw(Graphics g) {
        if(tripSet == null || stopSet == null){
            return;
        }else {
            for (Stop stop : stopSet) {
                stop.draw(g);
            }
            for (Trip trip : tripSet) {
                trip.draw(g);
            }
        }
    }

    @Override
    protected void onClick(MouseEvent e) {

    }

    @Override
    protected void onSearch() {

    }

    @Override
    protected void onMove(Move m) {

    }

    @Override
    protected void onLoad(File stopFile, File tripFile) {
        tripSet = (HashSet) read.readTrips(tripFile);
        stopSet = (HashSet) read.readStops(stopFile);

        redraw();
    }

    private Map(){
        super();
        read = new Reader();
    }

    public static void main(String[] args){
        new Map();
    }
}

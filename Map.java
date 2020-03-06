import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class Map extends GUI {

    private Reader read;
    private HashSet<Trip> tripSet;
    private HashSet<Stop> stopSet;
    private ArrayList<Drawable> highlighted;

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
        if(highlighted != null) {
            for (Drawable d : highlighted) d.highlightToggle();
        }
        highlighted = new ArrayList<>();
        Stop closestStop = (Stop) stopSet.toArray()[0];
        for (Stop stop : stopSet){
            if (stop.getPoint().distance(e.getPoint()) < closestStop.getPoint().distance(e.getPoint())){
                closestStop = stop;
            }
        }
        for (Trip trip : tripSet){
            if (trip.getStops().contains(closestStop)){
                trip.highlightToggle();
                highlighted.add(trip);
            }
        }
        highlighted.add(closestStop);
        closestStop.highlightToggle();
    }


    @Override
    protected void onSearch() {

    }

    @Override
    protected void onMove(Move m) {
        int baseMovement = 20;
        switch(m){
            case NORTH:
                changePosition(0, baseMovement);
                break;
            case EAST:
                changePosition(baseMovement, 0);
                break;
            case SOUTH:
                changePosition(0, -baseMovement);
                break;
            case WEST:
                changePosition(-baseMovement, 0);
                break;
            case ZOOM_IN:
                for(Stop stop : stopSet){
                    stop.zoomIn();
                }
                break;
            case ZOOM_OUT:
                for(Stop stop : stopSet){
                    stop.zoomOut();
                }
                break;
        }
        redraw();
    }

    public void changePosition(double x, double y){
        for(Stop stop : stopSet){
            stop.move(x,y);
        }
    }


    @Override
    protected void onLoad(File stopFile, File tripFile) {
        stopSet = (HashSet) read.readStops(stopFile);
        tripSet = (HashSet) read.readTrips(tripFile);
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

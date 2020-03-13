import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class Map extends GUI {

    private Reader read;
    private HashSet<Trip> tripSet;
    private HashSet<Stop> stopSet;
    private ArrayList<Drawable> highlighted;
    private Trie listNames;
    private int lastX = 0;
    private int lastY = 0;
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
        getTextOutputArea().setText("");
        getTextOutputArea().append("" + e.getPoint().toString());
        getTextOutputArea().append("\n" + new Point((int)getDrawingAreaDimension().getWidth()/2, (int)getDrawingAreaDimension().getHeight()/2).toString());
        if(highlighted != null) {
            for (Drawable d : highlighted) d.unhighlight();
        }
        highlighted = new ArrayList<>();
        Stop closestStop = (Stop) stopSet.toArray()[0];
        for (Stop stop : stopSet){
            if (stop.getPoint().distance(e.getPoint()) < closestStop.getPoint().distance(e.getPoint())){
                closestStop = stop;
            }
        }
        getTextOutputArea().append(closestStop.getName() + "\n");
        for (Trip trip : tripSet){
            if (trip.getStops().contains(closestStop)){
                trip.highlight();
                highlighted.add(trip);
                getTextOutputArea().append(trip.getID() + "\n");
            }
        }
        highlighted.add(closestStop);
        closestStop.highlight();

    }

    protected void onScroll(MouseWheelEvent e){
        //for(Stop stop : stopSet) stop.origin = new Location(e.getX()/stop.scale, e.getY()/stop.scale);
        for(int i = 0; i < Math.abs(e.getWheelRotation()); i++){
            if(e.getWheelRotation() < 0){
                onMove(Move.ZOOM_IN);
            }else{
                onMove(Move.ZOOM_OUT);
            }
        }
    }

    @Override
    protected void onDrag(MouseEvent e) {
        changePosition(lastX - e.getX(), e.getY() - lastY );
        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    protected void onPress(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    protected void onSearch() {

        ArrayList<Stop> stoplist = new ArrayList<>();
        stoplist.addAll(stopSet);
        listNames = new Trie(stoplist);
        getTextOutputArea().setText("");
        if (highlighted == null) { highlighted = new ArrayList<>();}

            for (Drawable d : highlighted) {
                d.unhighlight();
            }

        highlighted.clear();
        String userInput = getSearchBox().getText();
        userInput = userInput.toLowerCase();

        ArrayList<Stop> returnedNames = listNames.findAllNode(userInput);

        if(userInput.equals("")){
            getTextOutputArea().setText("");
        }else{
            for(Stop stop : returnedNames){
                getTextOutputArea().append(stop.getName() + "\n");
                stop.highlight();
                highlighted.add(stop);
                for(Trip trip : tripSet){
                    if(trip.getStops().contains(stop)) trip.highlight();
                    highlighted.add(trip);
                }
            }

        }
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

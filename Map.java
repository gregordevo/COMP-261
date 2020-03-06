import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
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
        getTextOutputArea().append(closestStop.getName() + "\n");
        for (Trip trip : tripSet){
            if (trip.getStops().contains(closestStop)){
                trip.highlightToggle();
                highlighted.add(trip);
                getTextOutputArea().append(trip.getID() + "\n");
            }
        }
        highlighted.add(closestStop);
        closestStop.highlightToggle();


    }


    @Override
    protected void onSearch() {
//        for(Drawable d : highlighted) d.highlightToggle(); highlighted.clear();
//        ArrayList<Stop> searchResult =  trie.findAllNode(getSearchBox().getText());
//        highlighted.addAll(searchResult);
//        for(Trip trip : tripSet){
//            for(Stop s : searchResult){
//                if(trip.getStops().contains(s.getName())){
//                    highlighted.add(trip);
//
//                }
//            }
//        }
//        for(Drawable d : highlighted) d.highlightToggle();


        ArrayList<Stop> stoplist = new ArrayList<>();
        stoplist.addAll(stopSet);
        listNames = new Trie(stoplist);
        getTextOutputArea().setText("");
        if (highlighted == null) { highlighted = new ArrayList<>();}

            for (Drawable d : highlighted) {
                d.highlightToggle();
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
                stop.highlightToggle();
                highlighted.add(stop);
            }
        }

        // getTextOutputArea().append(Boolean.toString(list.Search(userInput)) + "\node");

        //redraw();


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

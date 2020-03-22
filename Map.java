
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.ArrayList;

public class Map extends GUI {

    private Reader read;
    private ArrayList<Stop> stopList;
    private ArrayList<Trip> tripList;
    private ArrayList<Drawable> highlighted;
    private Quad<Stop> quad;
    private int lastX = 0;
    private int lastY = 0;


    /**
     * iterates over all objects in graph and draws them
     *
     * @param g
     */
    @Override
    protected void redraw(Graphics g) {
        if (tripList == null || stopList == null) {
            return;
        } else {
            for (Stop stop : stopList) {
                stop.draw(g);
            }
            for (Trip trip : tripList) {
                trip.draw(g);
            }
        }
    }

    /**
     * Uses a quadtree to select a node that the user clicks on.
     * This method works, however it only works consistently if the
     * user clicks on the exact node they want to select. If the user
     * clicks near the node they want to select it may not accurately
     * select the closest node to the click.
     *
     * @param e
     */
    @Override
    protected void onClick(MouseEvent e) {
        if (highlighted != null) {
            for (Drawable d : highlighted) d.unhighlight();
        }

        highlighted = new ArrayList<>();

        Stop randomStop = stopList.get(0);
        Location mouseLoc = Location.newFromPoint(e.getPoint(), randomStop.origin, randomStop.getScale());
        Stop closestStop = quad.getClosest(mouseLoc);

        if (closestStop == null) return;
        getTextOutputArea().setText(closestStop.getName() + "\n");
        for (Trip trip : tripList) {
            if (trip.getStops().contains(closestStop)) {
                trip.highlight();
                highlighted.add(trip);
                getTextOutputArea().append(trip.getID() + "\n");
            }
        }
        highlighted.add(closestStop);
        closestStop.highlight();

    }

    /**
     * performs a linear search and selects the stop closest to the
     * mouse click (working)
     * @param e
     */
//    @Override
//    protected void onClick(MouseEvent e) {
//        getTextOutputArea().setText("");
////        getTextOutputArea().append("" + e.getPoint().toString());
////        getTextOutputArea().append("\n" + new Point((int)getDrawingAreaDimension().getWidth()/2, (int)getDrawingAreaDimension().getHeight()/2).toString());
//        if(highlighted != null) {
//            for (Drawable d : highlighted) d.unhighlight();
//        }
//        highlighted = new ArrayList<>();
//        Stop closestStop = (Stop) stopList.toArray()[0];
//        for (Stop stop : stopList){
//            if (stop.getPoint().distance(e.getPoint()) < closestStop.getPoint().distance(e.getPoint())){
//                closestStop = stop;
//            }
//        }
//        getTextOutputArea().append(closestStop.getName() + "\n");
//        for (Trip trip : tripList){
//            if (trip.getStops().contains(closestStop)){
//                trip.highlight();
//                highlighted.add(trip);
//                getTextOutputArea().append(trip.getID() + "\n");
//            }
//        }
//
//        Location mouseLoc = Location.newFromPoint(e.getPoint(), closestStop.origin, closestStop.getScale());
//        getTextOutputArea().append("mouse: " +mouseLoc.toString() + "\n");
//        getTextOutputArea().append("stop: " +closestStop.getLocation().toString() + "\n");
//        highlighted.add(closestStop);
//        closestStop.highlight();
//
//    }

    /**
     * zooms in or out based on the scroll wheel
     *
     * @param e
     */
    protected void onScroll(MouseWheelEvent e) {
        for (int i = 0; i < Math.abs(e.getWheelRotation()); i++) {
            if (e.getWheelRotation() < 0) {
                onMove(Move.ZOOM_IN);
            } else {
                onMove(Move.ZOOM_OUT);
            }
        }
    }


    /**
     * moves the map around based on the users mouse drag
     *
     * @param e
     */
    @Override
    protected void onDrag(MouseEvent e) {
        changePosition(lastX - e.getX(), e.getY() - lastY);
        lastX = e.getX();
        lastY = e.getY();
    }

    @Override
    protected void onPress(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
    }

    /**
     * utilises a trie tree to search all stops corresponding to
     * a users input and returns them, then selects each one.
     */
    @Override
    protected void onSearch() {


        Trie listNames = new Trie(stopList);
        getTextOutputArea().setText("");
        if (highlighted == null) {
            highlighted = new ArrayList<>();
        }

        for (Drawable d : highlighted) {
            d.unhighlight();
        }

        highlighted.clear();
        String userInput = getSearchBox().getText();
        userInput = userInput.toLowerCase();

        ArrayList<Stop> returnedNames = listNames.listElements(userInput);

        if (userInput.equals("")) {
            getTextOutputArea().setText("");
        } else {
            for (Stop stop : returnedNames) {
                getTextOutputArea().append(stop.getName() + "\n");
                stop.highlight();
                highlighted.add(stop);
                for (Trip trip : tripList) {
                    if (trip.getStops().contains(stop)) trip.highlight();
                    highlighted.add(trip);
                }
            }

        }
    }

    /**
     * moves all objects in the graph by a set amount based on the direction
     * it is given.
     *
     * @param m
     */
    @Override
    protected void onMove(Move m) {
        int baseMovement = 20;
        switch (m) {
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
                for (Stop stop : stopList) {
                    stop.zoomIn();
                }
                break;
            case ZOOM_OUT:
                for (Stop stop : stopList) {
                    stop.zoomOut();
                }
                break;
        }
        redraw();
    }

    public void changePosition(double x, double y) {
        for (Stop stop : stopList) {
            stop.move(x, y);
        }
    }

    /**
     * finds the furthest most top left stop and the adds 100 to the x and y.
     *
     * @return
     */
    public Location findTopLeft() {

        double furthestStopx = stopList.get(0).getLocation().x;
        double furthestStopy = stopList.get(0).getLocation().y;
        double x = 0;
        double y = 0;
        for (Stop s : stopList) {
            if (s.getLocation().x < furthestStopx) {
                x = s.getLocation().x;
                furthestStopx = x;
            }
            if (s.getLocation().y > furthestStopy) {
                y = s.getLocation().y;
                furthestStopy = y;
            }
        }

        Location loc = new Location(x - 100, y + 100);
        return loc;
    }

    /**
     * finds the furthest most bottom right stop and the adds 100 to the x and y.
     *
     * @return
     */
    public Location findBottomRight() {

        double furthestStopx = stopList.get(0).getLocation().x;
        double furthestStopy = stopList.get(0).getLocation().y;
        double x = 0;
        double y = 0;
        for (Stop s : stopList) {
            if (s.getLocation().x > furthestStopx) {
                x = s.getLocation().x;
                furthestStopx = x;
            }
            if (s.getLocation().y < furthestStopy) {
                y = s.getLocation().y;
                furthestStopy = y;
            }
        }

        Location loc = new Location(x + 100, y - 100);
        return loc;
    }


    /**
     * reads the data files and creates a quadtree based on the locations
     * of the furthest outlying Stops
     *
     * @param stopFile
     * @param tripFile
     */
    @Override
    protected void onLoad(File stopFile, File tripFile) {
        stopList = read.readStops(stopFile);
        tripList = read.readTrips(tripFile);
        quad = new Quad(findTopLeft(), findBottomRight());
        quad.insertElements(stopList);
        redraw();
    }

    private Map() {
        super();
        read = new Reader();
    }

    public static void main(String[] args) {
        new Map();
    }
}

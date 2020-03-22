
import javafx.geometry.Rectangle2D;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Quad tree that recursively for the closest element to a given point
 * @param <E>
 */
public class Quad<E extends Locatable> {

    private Quad<E> topLeftQuad, bottomLeftQuad, topRightQuad, bottomRightQuad;
    private ArrayList<E> quadElements = new ArrayList<>();
    private Location topLeftMostPoint, bottomRightMostPoint;
    private Rectangle2D quadRect;
    final int maxElements = 5;

    Quad(Location topLeftMostPoint, Location bottomRightMostPoint) {
        this.topLeftMostPoint = topLeftMostPoint;
        this.bottomRightMostPoint = bottomRightMostPoint;
         quadRect = new Rectangle2D( topLeftMostPoint.x,  topLeftMostPoint.y,   (bottomRightMostPoint.x - topLeftMostPoint.x),  ( topLeftMostPoint.y - bottomRightMostPoint.y ));

    }


    public void insertElement(E e) {
        if (e == null) return;
        quadElements.add(e);
        if (quadElements.size() > maxElements || isParent()) {
            splitQuads();
            for (E quadElement : quadElements) {
                if (topRightQuad.Contains(quadElement)) topRightQuad.insertElement(quadElement);
                else if (topLeftQuad.Contains(quadElement)) topLeftQuad.insertElement(quadElement);
                else if (bottomRightQuad.Contains(quadElement)) bottomRightQuad.insertElement(quadElement);
                else if (bottomLeftQuad.Contains(quadElement)) bottomLeftQuad.insertElement(quadElement);
            }
            quadElements.clear();

        }
    }

    private boolean isParent() {
        return (bottomLeftQuad != null && topLeftQuad != null && bottomRightQuad != null && topRightQuad != null);
    }

    public void insertElements(Collection<E> elements) {
        for (E e : elements) {
            insertElement(e);
        }
    }


    private Location middlePoint() {
        return new Location((topLeftMostPoint.x + bottomRightMostPoint.x) / 2, (topLeftMostPoint.y + bottomRightMostPoint.y) / 2);
    }


    private void splitQuads() {
        if (topLeftQuad == null) topLeftQuad = new Quad<>(topLeftMostPoint, middlePoint());
        if (bottomRightQuad == null) bottomRightQuad = new Quad<>(middlePoint(), bottomRightMostPoint);
        if (topRightQuad == null)
            topRightQuad = new Quad<>(new Location(middlePoint().x, topLeftMostPoint.y), new Location(bottomRightMostPoint.x, middlePoint().y));
        if (bottomLeftQuad == null)
            bottomLeftQuad = new Quad<>(new Location(topLeftMostPoint.x, middlePoint().y), new Location(middlePoint().x, bottomRightMostPoint.y));
    }


    private boolean Contains(E element) {
        Location p = element.getLocation();
        System.out.println(element.getLocation().toString());
        return (p.x < bottomRightMostPoint.x && p.x > topLeftMostPoint.x && p.y < topLeftMostPoint.y && p.y > bottomRightMostPoint.y);
    }

    private boolean Contains(Location p) {
        return (p.x < bottomRightMostPoint.x && p.x > topLeftMostPoint.x && p.y < topLeftMostPoint.y && p.y > bottomRightMostPoint.y);
    }

    public E getClosest(Location p) {
        return get(p).peek();
//        E closestElement = get(p).peek();
//        double topLeftx =  (p.x -closestElement.getLocation().x);
//        double topLefty = (p.y - closestElement.getLocation().y);
//        Rectangle2D searchRectangle = new Rectangle2D(topLeftx, topLefty, closestElement.getLocation().distance(p), closestElement.getLocation().distance(p));
//        closestElement = rectangleSearch(searchRectangle, p).peek();
//        return closestElement;
    }

    /**
     * unused method, was created to give a more accurate value when clicking
     * on the map by creating a rectangle around the mouse click based on the
     * distance from the closest found node, then seeing if that rectangle
     * intersected or contained any quads, if it did then it would search the
     * quads and see if they were closer than the already closest stop.
     *
     * Stopped working on it because ran out of time to finish it properly.
     * @param rect
     * @param p
     * @return
     */
    private PriorityQueue<E> rectangleSearch(Rectangle2D rect, Location p){
        PriorityQueue<E> queue = new PriorityQueue<>(1, Comparator.comparing(d -> d.getLocation().distance(p)));
        if(intersectsQuad(rect) ||  containsRect(rect) ){
            if(isParent()) {
                if (topLeftQuad.intersectsQuad(rect) || topLeftQuad.containsRect(rect)) {
                    queue.addAll(topLeftQuad.rectangleSearch(rect, p));
                }
                if (bottomLeftQuad.intersectsQuad(rect) || bottomLeftQuad.containsRect(rect)) {
                    queue.addAll(bottomLeftQuad.rectangleSearch(rect, p));
                }
                if (topRightQuad.intersectsQuad(rect) || topRightQuad.containsRect(rect)) {
                    queue.addAll(topRightQuad.rectangleSearch(rect, p));
                }
                if (bottomRightQuad.intersectsQuad(rect) || bottomRightQuad.containsRect(rect)) {
                    queue.addAll(bottomRightQuad.rectangleSearch(rect, p));
                }
            }
        }
        queue.addAll(quadElements);
        return queue;
    }

    public PriorityQueue<E> get(Location p ) {
        PriorityQueue<E> queue = new PriorityQueue<>(1, Comparator.comparing(d -> d.getLocation().distance(p)));

        if (topLeftQuad != null && topLeftQuad.quadElements != null) queue.addAll(topLeftQuad.quadElements);
        if (bottomRightQuad != null && bottomRightQuad.quadElements != null) queue.addAll(bottomRightQuad.quadElements);
        if (topRightQuad != null && topRightQuad.quadElements != null) queue.addAll(topRightQuad.quadElements);
        if (bottomLeftQuad != null && bottomLeftQuad.quadElements != null) queue.addAll(bottomLeftQuad.quadElements);
        queue.addAll(quadElements);
        if (quadElements.isEmpty() && isParent()) {
            if (topLeftQuad.Contains(p)) queue.addAll(topLeftQuad.get(p));
            if (bottomRightQuad.Contains(p)) queue.addAll(bottomRightQuad.get(p));
            if (topRightQuad.Contains(p)) queue.addAll(topRightQuad.get(p));
            if (bottomLeftQuad.Contains(p)) queue.addAll(bottomLeftQuad.get(p));
        }
         return queue;
    }

    private Boolean intersectsQuad(Rectangle2D r) {
           return(quadRect.intersects(r) || r.intersects(quadRect));
    }

    private Boolean containsRect(Rectangle2D rect){
        return Contains(new Location(rect.getMinX(), rect.getMinY())) && Contains(new Location(rect.getMaxX(), rect.getMaxY() ));
    }
}
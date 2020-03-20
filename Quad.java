import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Quad<E extends Locatable> {

    private Quad<E> topLeftQuad, bottomLeftQuad, topRightQuad, bottomRightQuad;
    private ArrayList<E> quadElements = new ArrayList<>();
     private Location topLeftMostPoint, bottomRightMostPoint;


    final int maxElements = 4;

    Quad(Location topLeftMostPoint, Location bottomRightMostPoint) {
        this.topLeftMostPoint = topLeftMostPoint;
        this.bottomRightMostPoint = bottomRightMostPoint;
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

    public E getClosest(Location p){
        return get(p).peek();
    }

    public PriorityQueue<E> get(Location p) {
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

}
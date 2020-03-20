   import java.util.ArrayList;
import java.util.Collection;

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

    private boolean isParent(){
        return(bottomLeftQuad != null && topLeftQuad != null && bottomRightQuad != null && topRightQuad != null );
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
        Location p = element. getLocation();
          System.out.println(element. getLocation().toString());
        return (p.x < bottomRightMostPoint.x && p.x > topLeftMostPoint.x && p.y < topLeftMostPoint.y && p.y > bottomRightMostPoint.y);
    }

    private boolean Contains(Location p) {
        return (p.x < bottomRightMostPoint.x && p.x > topLeftMostPoint.x && p.y < topLeftMostPoint.y && p.y > bottomRightMostPoint.y);
    }

    private boolean containsElements() {
        return isParent() || quadElements.size() > 0;
    }


    public E get(Location p) {
        if (quadElements.isEmpty() && isParent() ) {
            if ( topLeftQuad.Contains(p) ) {
                return topLeftQuad.get(p);
            }
            if ( bottomRightQuad.Contains(p) ) {
                 return bottomRightQuad.get(p);
            }
            if ( topRightQuad.Contains(p) ) {
                return topRightQuad.get(p);
            }
            if ( bottomLeftQuad.Contains(p) ) {
                return bottomLeftQuad.get(p);
            }
        } else if(!containsElements()){
             return null;
        } else {
            E base = null;
            for (E e : quadElements) {
               if(base == null)base = e;
                if (e. getLocation().distance(p) < base. getLocation().distance(p)) base = e;
            }
            return base;
        }
         return null;
    }
}
import java.awt.*;

public class Connection extends Drawable {
    Stop startNode;
    Stop endNode;

    Connection(Stop startNode, Stop endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
    }


    /**
     * Draws a line from one stop to another
     */
    @Override
    public void draw(Graphics g) {
        Point firstPoint = startNode.getPoint();
        Point secondPoint = endNode.getPoint();
        g.drawLine((int) firstPoint.getX(), (int) firstPoint.getY(), (int) secondPoint.getX(), (int) secondPoint.getY());
    }
}

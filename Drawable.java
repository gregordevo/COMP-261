import java.awt.*;
import java.awt.*;

    /**
     * Abstract class for all objects that are part
     * of the graph.
     */
    public abstract class Drawable {
        String ID;
        public int scale = 20;
        public Location origin = new Location(0,0);
        Color colour = Color.black;

        /**
         * Draw the information on the graphics canvas
         * @param g Graphics object to draw on
         */
        public abstract void draw(Graphics g);

        /**
         * move object by x and y
         * @param x
         * @param y
         */
        public void move(double x, double y){
            origin = origin.moveBy(x/scale, y/scale);
        }

        /**
         * increases the scale of the object effectively magnifying it.
         */
        public void zoomIn(){
            if(scale >= 1000)return;
            scale *= 1.1;

        }


        public void highlight(){
            colour = Color.RED;
        }

        public void unhighlight(){
            colour = Color.black;
        }

        /**
         * decreases the scale of the object which effectively shrinks it.
         */
        public void zoomOut(){
            if(scale <= 11)return;
            scale *= 0.9;
        }

        public String getID(){ return this.ID; }
}

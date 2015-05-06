
package com.gdu.bw.svg.model;










/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class Point implements Cloneable, java.io.Serializable,Translatable{

    private static final long serialVersionUID = 1L;
    
    
    public static final Point SINGLETON = new Point();
    
    
    public int x;
    
    public int y;

    public Point() {
    }
    
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Point(double x, double y) {
        this.x = (int) x;
        this.y = (int) y;
    }
    
    public Dimension getDifference(Point p) {
        return new Dimension(this.x - p.x(), this.y - p.y());
    }

    public Point(Point p) {
        x = p.x();
        y = p.y();
    }
    
    public double preciseX() {
        return x;
    }

    public double preciseY() {
        return y;
    }
    
    public Point setX(int x) {
        this.x = x;
        return this;
    }

    public Point setY(int y) {
        this.y = y;
        return this;
    }
    
    public int x() {
        return x;
    }
    
    public int y() {
        return y;
    }
    
    public String toString() {
        return "Point(" + x + ", " + y + ")";//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
    }
    
    public double getDistance(Point p) {
        double i = p.preciseX() - preciseX();
        double j = p.preciseY() - preciseY();
        return Math.sqrt(i * i + j * j);
    }
    
    public Point getCopy() {
        return new Point(this);
    }
    
    public Point scale(double factor) {
        return scale(factor, factor);
    }

    public Point scale(double xFactor, double yFactor) {
        x = (int) Math.floor(x * xFactor);
        y = (int) Math.floor(y * yFactor);
        return this;
    }
    
    public Point translate(Dimension d) {
        return translate(d.width(), d.height());
    }


    public Point translate(int dx, int dy) {
        x += dx;
        y += dy;
        return this;
    }

    public Point translate(Point p) {
        return translate(p.x(), p.y());
    }

    public Point getTranslated(Point p) {
        return getCopy().translate(p);
    }
    
    public Point getTransposed() {
        return getCopy().transpose();
    }
    
    public Point transpose() {
        int temp = x;
        x = y;
        y = temp;
        return this;
    }

    /**
     * @see com.tibco.bw.core.svg.model.Translatable#performTranslate(int, int)
     */
    public void performTranslate(int dx, int dy) {
        translate(dx, dy);
        
    }

    /**
     * @see com.tibco.bw.core.svg.model.Translatable#performScale(double)
     */
    public void performScale(double factor) {
        scale(factor);
        
    }
    
    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

}


package com.gdu.bw.svg.model;




/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class Dimension implements Cloneable, java.io.Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public static final Dimension SINGLETON = new Dimension();

    public static Dimension max(Dimension d1, Dimension d2) {
        return new Dimension(Math.max(d1.width(), d2.width()), Math.max(
                d1.height(), d2.height()));
    }

    public static Dimension min(Dimension d1, Dimension d2) {
        return new Dimension(Math.min(d1.width(), d2.width()), Math.min(
                d1.height(), d2.height()));
    }

    public int width;

    public int height;

    public Dimension() {
    }

    public Dimension(Dimension d) {
        width = d.width();
        height = d.height();
    }

    public Dimension(int w, int h) {
        width = w;
        height = h;
    }
    
    public double preciseHeight() {
        return height;
    }

    public double preciseWidth() {
        return width;
    }
    
    public int height() {
        return height;
    }

    public int width() {
        return width;
    }
    public Dimension getTransposed() {
        return getCopy().transpose();
    }
    
    public Dimension getCopy() {
        return new Dimension(this);
    }
    
    public Dimension transpose() {
        int temp = width;
        width = height;
        height = temp;
        return this;
    }
    public Dimension scale(double factor) {
        return scale(factor, factor);
    }
    
    public Dimension scale(double widthFactor, double heightFactor) {
        width = (int) (Math.floor(width * widthFactor));
        height = (int) (Math.floor(height * heightFactor));
        return this;
    }
    
    public int getArea() {
        return width * height;
    }
}


package com.gdu.bw.svg.model;









/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class PrecisionPoint extends Point {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public double preciseX;
    
    public double preciseY;
    
    public PrecisionPoint(double x, double y) {
        setPreciseLocation(x, y);
    }
    
    public PrecisionPoint setPreciseLocation(double x, double y) {
        setPreciseX(x);
        setPreciseY(y);
        return this;
    }
    
    public PrecisionPoint(Point p) {
        this(p.preciseX(), p.preciseY());
    }
    
    public PrecisionPoint setPreciseX(double x) {
        preciseX = x;
        updateXInt();
        return this;
    }
    public PrecisionPoint setPreciseY(double y) {
        preciseY = y;
        updateYInt();
        return this;
    }
    private final void updateYInt() {
        y = doubleToInteger(preciseY);
    }
    private final void updateXInt() {
        x = doubleToInteger(preciseX);
    }
    public Point setX(int x) {
        return setPreciseX(x);
    }

    public Point setY(int y) {
        return setPreciseY(y);
    }
    protected  int doubleToInteger(double doubleValue) {
        return (int) Math.floor(doubleValue + 0.000000001);
    }
    
    public final void updateInts() {
        updateXInt();
        updateYInt();
    }
    public Point getCopy() {
        return getPreciseCopy();
    }
    
    public PrecisionPoint getPreciseCopy() {
        return new PrecisionPoint(preciseX(), preciseY());
    }
    
    public double preciseX() {
        updatePreciseXDouble();
        return preciseX;
    }
    
    public double preciseY() {
        updatePreciseYDouble();
        return preciseY;
    }

    private final void updatePreciseXDouble() {
        if (x != doubleToInteger(preciseX)) {
            preciseX = x;
        }
    }

    /**
     * Updates the preciseY double field using the value of y.
     */
    private final void updatePreciseYDouble() {
        if (y != doubleToInteger(preciseY)) {
            preciseY = y;
        }
    }
    public Point translate(Dimension d) {
        return translatePrecise(d.preciseWidth(), d.preciseHeight());
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#translate(int, int)
     */
    public Point translate(int dx, int dy) {
        return translatePrecise(dx, dy);
    }

    /**
     * @see org.eclipse.draw2d.geometry.Point#translate(org.eclipse.draw2d.geometry.Point)
     */
    public Point translate(Point p) {
        return translatePrecise(p.preciseX(), p.preciseY());
    }
    
    private PrecisionPoint translatePrecise(double dx, double dy) {
        setPreciseX(preciseX() + dx);
        setPreciseY(preciseY() + dy);
        return this;
    }
    
    public Point transpose() {
        double temp = preciseX();
        setPreciseX(preciseY());
        setPreciseY(temp);
        return this;
    }
    
    public PrecisionPoint setPreciseLocation(PrecisionPoint p) {
        return setPreciseLocation(p.preciseX(), p.preciseY());
    }
    
    public boolean equals(Object o) {
        if (o instanceof PrecisionPoint) {
            PrecisionPoint p = (PrecisionPoint) o;
            return p.preciseX() == preciseX() && p.preciseY() == preciseY();
        }
        return super.equals(o);
    }

}

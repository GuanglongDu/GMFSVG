
package com.gdu.bw.svg.model;






















/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class Rectangle implements Cloneable, java.io.Serializable,Translatable{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public static final Rectangle SINGLETON = new Rectangle();
    
    public int x;
    public int y;
    public int width;
    public int height;
    public Rectangle() {
    }
    /**
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    
    public Rectangle(Rectangle rect){
        this(rect.x, rect.y, rect.width, rect.height);
    }
    /**
     * @return the x
     */
    public int getX() {
        return this.x;
    }
    /**
     * @param x the x to set
     */
    public Rectangle setX(int x) {
        this.x = x;
        return this;
    }
    /**
     * @return the y
     */
    public int getY() {
        return this.y;
    }
    /**
     * @param y the y to set
     */
    public Rectangle setY(int y) {
        this.y = y;
        return this;
    }
    /**
     * @return the width
     */
    public int getWidth() {
        return this.width;
    }
    /**
     * @param width the width to set
     */
    public Rectangle setWidth(int width) {
        this.width = width;
        return this;
    }
    /**
     * @return the height
     */
    public int getHeight() {
        return this.height;
    }
    /**
     * @param height the height to set
     */
    public Rectangle setHeight(int height) {
        this.height = height;
        return this;
    }
    public boolean contains(int x, int y) {
        return y >= this.y && y < this.y + this.height && x >= this.x
                && x < this.x + this.width;
    }
    
    public boolean contains(Point p) {
        return contains(p.x(), p.y());
    }
    
    public Rectangle setLocation(Point p) {
        return setLocation(p.x(), p.y());
    }
    
    public Rectangle setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
    public void union(Point p) {
        union(p.x(), p.y());
    }
    
    public Rectangle union(int x1, int y1) {
        if (x1 < x) {
            width += (x - x1);
            x = x1;
        } else {
            int right = x + width;
            if (x1 >= right) {
                right = x1 + 1;
                width = right - x;
            }
        }
        if (y1 < y) {
            height += (y - y1);
            y = y1;
        } else {
            int bottom = y + height;
            if (y1 >= bottom) {
                bottom = y1 + 1;
                height = bottom - y;
            }
        }
        return this;
    }
    
    public double preciseHeight() {
        return height;
    }
    public int right() {
        return x + width;
    }
    public double preciseWidth() {
        return width;
    }

    public double preciseX() {
        return x;
    }

    public double preciseY() {
        return y;
    }
    
    public int bottom() {
        return y + height;
    }
    
    public boolean isEmpty() {
        return width <= 0 || height <= 0;
    }
    
    public Point getBottom() {
        return new Point(x + width / 2, bottom());
    }
    
    public Point getBottomLeft() {
        return new Point(x, y + height);
    }

    public Point getRight() {
        return new Point(right(), y + height / 2);
    }
    
    public Point getLeft() {
        return new Point(x, y + height / 2);
    }
    
    public Point getTop() {
        return new Point(x + width / 2, y);
    }

    public Point getTopLeft() {
        return new Point(x, y);
    }
    
    public Rectangle setSize(int w, int h) {
        width = w;
        height = h;
        return this;
    }
    public Rectangle crop(Insets insets) {
        return shrink(insets);
    }
    
    public Rectangle shrink(Insets insets) {
        if (insets == null)
            return this;
        x += insets.left;
        y += insets.top;
        width -= (insets.getWidth());
        height -= (insets.getHeight());
        return this;
    }
    
    public Rectangle getCopy() {
        if (getClass() == Rectangle.class) {
            return new Rectangle(this);
        } else {
            try {
                return (Rectangle) clone();
            } catch (CloneNotSupportedException exc) {
                return new Rectangle(this);
            }
        }
    }
    
    public Rectangle getTransposed() {
        return getCopy().transpose();
    }
    
    public Rectangle transpose() {
        int temp = x;
        x = y;
        y = temp;
        temp = width;
        width = height;
        height = temp;
        return this;
    }
    
    public Rectangle setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        return this;
    }
    
    public Rectangle setBounds(Rectangle rect) {
        return setBounds(rect.x(), rect.y(), rect.width(), rect.height());
    }

    public int width() {
        return width;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }
    public int height() {
        return height;
    }
    
    public Point getCenter() {
        return new Point(x + width / 2, y + height / 2);
    }
    
    public boolean intersects(Rectangle rect) {
        return !getIntersection(rect).isEmpty();
    }

    public Rectangle getIntersection(Rectangle rect) {
        return getCopy().intersect(rect);
    }
    
    public Rectangle intersect(Rectangle rect) {
        int x1 = Math.max(x, rect.x());
        int x2 = Math.min(x + width, rect.x() + rect.width());
        int y1 = Math.max(y, rect.y());
        int y2 = Math.min(y + height, rect.y() + rect.height());
        if (((x2 - x1) < 0) || ((y2 - y1) < 0))
            return setBounds(0, 0, 0, 0); // no intersection
        else {
            return setBounds(x1, y1, x2 - x1, y2 - y1);
        }
    }
    
    public Rectangle setSize(Dimension d) {
        return setSize(d.width(), d.height());
    }
    
    public String toString() {
        return "Rectangle(" + x + ", " + y + ", " + //$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
                width + ", " + height + ")";//$NON-NLS-2$//$NON-NLS-1$
    }
    
    public Point getTopRight() {
        return new Point(x + width, y);
    }
    
    public Point getBottomRight() {
        return new Point(x + width, y + height);
    }
    
    public Rectangle expand(Insets insets) {
        x -= insets.left;
        y -= insets.top;
        width += insets.getWidth();
        height += insets.getHeight();
        return this;
    }
    
    public Rectangle expand(int h, int v) {
        return shrink(-h, -v);
    }

    public Rectangle shrink(int h, int v) {
        x += h;
        width -= (h + h);
        y += v;
        height -= (v + v);
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
    
    public Rectangle translate(int dx, int dy) {
        x += dx;
        y += dy;
        return this;
    }
    public final Rectangle scale(double scaleFactor) {
        return scale(scaleFactor, scaleFactor);
    }
    
    public Rectangle scale(double scaleX, double scaleY) {
        int oldX = x;
        int oldY = y;
        x = (int) (Math.floor(x * scaleX));
        y = (int) (Math.floor(y * scaleY));
        width = (int) (Math.ceil((oldX + width) * scaleX)) - x;
        height = (int) (Math.ceil((oldY + height) * scaleY)) - y;
        return this;
    }
    
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof Rectangle) {
            Rectangle r = (Rectangle) o;
            return (x == r.x()) && (y == r.y()) && (width == r.width())
                    && (height == r.height());
        }
        return false;
    }
}

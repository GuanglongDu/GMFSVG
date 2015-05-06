
package com.gdu.bw.svg.model.figure;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.gdu.bw.svg.helper.BWDiagramUtil;
import com.gdu.bw.svg.model.BWNode;
import com.gdu.bw.svg.model.Insets;
import com.gdu.bw.svg.model.LineSeg;
import com.gdu.bw.svg.model.Point;
import com.gdu.bw.svg.model.PointList;
import com.gdu.bw.svg.model.PointListUtilities;
import com.gdu.bw.svg.model.PrecisionPoint;
import com.gdu.bw.svg.model.PrecisionRectangle;
import com.gdu.bw.svg.model.Rectangle;
import com.gdu.bw.svg.model.Translatable;

/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class SVGFigure {
    
    protected String id;
    
    protected String nodeName;
    
    protected BWNode node;
    
    protected List<SVGFigure> childList;
    
    protected String type;
    
    protected Rectangle bounds;
    
    protected SVGFigure parentFigure;
    
    protected Point absolutePoint = new Point(0,0);
    
    protected Rectangle absoluteBound = new Rectangle();
    
    protected Element diagram;
    
    protected List<BorderLayer> layers = new ArrayList<BorderLayer>();
    
    protected PrecisionPoint relativeReference;
    
    static private int STRAIGHT_LINE_TOLERANCE = 10;
    
    private Boolean isCollapse = false;
    
    private Insets insets = new Insets(0,0,0,0);
    
    protected Boolean isRelativeRoot = false;
    
    protected String name;
   
    
    public SVGFigure() {
        initChildList();
    }
 
    public SVGFigure(SVGFigure parentFigure) {
        this.parentFigure = parentFigure;
        parentFigure.addChildFigure(this);
        initChildList();
    }
 
    public void initChildList() {
        if (childList == null)
            childList = new ArrayList<SVGFigure>();
    }
 
    /**
     * @return the id
     */
    public String getId() {
        return this.id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the nodeName
     */
    public String getNodeName() {
        return this.nodeName;
    }
    /**
     * @param nodeName the nodeName to set
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    /**
     * @return the node
     */
    public BWNode getNode() {
        return this.node;
    }
    /**
     * @param node the node to set
     */
    public void setNode(BWNode node) {
        this.node = node;
    }
    /**
     * @return the childList
     */
    public List<SVGFigure> getChildList() {
        return this.childList;
    }
    
    public void addChildFigure(SVGFigure figure) {
        initChildList();
        childList.add(figure);
    }
    /**
     * @param childList the childList to set
     */
    public void setChildList(List<SVGFigure> childList) {
        this.childList = childList;
    }
    /**
     * @return the type
     */
    public String getType() {
        return this.type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    /**
     * @return the bounds
     */
    public Rectangle getBounds() {
        return this.bounds;
    }
    /**
     * @param bounds the bounds to set
     */
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }
    /**
     * @return the parentFigure
     */
    public SVGFigure getParentFigure() {
        return this.parentFigure;
    }
    /**
     * @param parentFigure the parentFigure to set
     */
    public void setParentFigure(SVGFigure parentFigure) {
        this.parentFigure = parentFigure;
    }
    /**
     * @return the absolutePoint
     */
    public Point getAbsolutePoint() {
        int x = 0;
        int y = 0;
        SVGFigure parent = getParentFigure();
        int layerX = 0;
        int layerY = 0;
        if(layers.size()>0){
            for(BorderLayer layer:layers){
                layerX += layer.getX();
                layerY += layer.getY(); 
            }
        }
        
        if(parent != null){
            Point absolutePoint2 = parent.getAbsolutePoint();
            x = this.absolutePoint.x + absolutePoint2.x+layerX;
            y = this.absolutePoint.y + absolutePoint2.y+layerY;
        }else{
            x = this.absolutePoint.x;
            y = this.absolutePoint.y;
        }
        return new Point(x,y);
    }
    /**
     * @param absolutePoint the absolutePoint to set
     */
    public void setAbsolutePoint(Point absolutePoint) {
        this.absolutePoint = absolutePoint;
    }
    
    public Rectangle getHandlerBounds() {
        if(bounds == null){
            return new Rectangle();
        }
        int leftInsect = 37;
        int rightInsect = 37;
        int topInsect = 0;
        int bottomInsect = 26;
        Rectangle newBounds = new Rectangle(bounds.getCopy());
        int newWidth = 24;
        int newHeight = 24;
        if (BWDiagramUtil.INSTANCE.isGroupStart(getNode().getElement())) {
            newBounds.width = newWidth;
            newBounds.height = newHeight;
            return newBounds;
        } else if (BWDiagramUtil.INSTANCE.isGroupEnd(getNode().getElement())) {
            newBounds.width = newWidth;
            newBounds.height = newHeight;
            return newBounds;
        }

        if (BWDiagramUtil.INSTANCE.isOnMessageStart(getNode().getElement())) {
            newBounds.width = newWidth;
            newBounds.height = newHeight;
            return newBounds;
        } else if (BWDiagramUtil.INSTANCE.isOnMessageEnd(getNode().getElement())) {
            newBounds.width = newWidth;
            newBounds.height = newHeight;
            return newBounds;
        }

        newBounds.x += leftInsect;
        newBounds.width -= (leftInsect + rightInsect);
        newBounds.y += topInsect;
        newBounds.height -= bottomInsect;
        return newBounds;
    }
    
    public PointList getPolygonPoints() {
        PointList points = new PointList(5);
        Rectangle anchorableRectangle = getHandlerBounds().getCopy();
        points.addPoint(anchorableRectangle.x, anchorableRectangle.y);
        points.addPoint(anchorableRectangle.x + anchorableRectangle.width,
                anchorableRectangle.y);
        points.addPoint(anchorableRectangle.x + anchorableRectangle.width,
                anchorableRectangle.y + anchorableRectangle.height);
        points.addPoint(anchorableRectangle.x, anchorableRectangle.y
                + anchorableRectangle.height);
        points.addPoint(anchorableRectangle.x, anchorableRectangle.y);
        return points;
    }
    
    public Point getReferencePoint() {
        return getAnchorPosition();
    }
    
    public Point getAnchorPosition() {
        Rectangle sourceRect = getHandlerBounds().getCopy();
        translateToAbsolute(sourceRect);
        PrecisionRectangle rBox = new PrecisionRectangle(sourceRect);
        if (isDefaultAnchor()){
            return rBox.getCenter();
        }
        return new PrecisionPoint(relativeReference.preciseX * rBox.preciseWidth
                + rBox.preciseX, relativeReference.preciseY * rBox.preciseHeight
                + rBox.preciseY);
    }
    /**
     * @param relativeReference the relativeReference to set
     */
    public void setRelativeReference(PrecisionPoint relativeReference) {
        this.relativeReference = relativeReference;
    }
    
    public PrecisionRectangle translateToAbsolute(PrecisionRectangle bounds){
            Point absolutePoint2 = getAbsolutePoint();
            bounds.x = bounds.x+absolutePoint2.x;
            bounds.y = bounds.y+absolutePoint2.y;
        return bounds;
    }
    
    public PointList translateToAbsolute(PointList bounds){
        PointList cache = bounds.getCopy();
        cache.removeAllPoints();
        Point absolutePoint2 = getAbsolutePoint();
       for(int i=0;i<bounds.size();i++){
           Point point = bounds.getPoint(i);
           point.x = point.x +absolutePoint2.x;
           point.y = point.y +absolutePoint2.y;
           cache.addPoint(point);
       }
       return cache;
    }
    
    /**
     * @param ptAbsE2
     * @return
     */
    public void translateToAbsolute(Translatable t) {
        int layerX = 0;
        int layerY = 0;
        if(layers.size()>0){
            for(BorderLayer layer:layers){
                layerX += layer.getX();
                layerY += layer.getY(); 
            }
        }
        t.performTranslate(this.absolutePoint.x + getInsets().left+layerX, absolutePoint.y+ getInsets().top+layerY);
        if(getParentFigure() != null){
            getParentFigure().translateToAbsolute(t);
        }
    }
    
    
    /**
     * @return the diagram
     */
    public Element getDiagram() {
        return this.diagram;
    }

    /**
     * @param diagram the diagram to set
     */
    public void setDiagram(Element diagram) {
        this.diagram = diagram;
    }
    
    public SVGFigure findSVGFigureByPath(String uriFragmentSegment) {
        int lastIndex = uriFragmentSegment.length() - 1;
        char lastChar = uriFragmentSegment.charAt(lastIndex);
        if (lastChar == ']') {
            int index = uriFragmentSegment.indexOf('[');
            if (index >= 0) {
                String predicate = uriFragmentSegment.substring(index + 1, lastIndex);
            }
        } else {
            int dotIndex = -1;
            if (Character.isDigit(lastChar)) {
                dotIndex = uriFragmentSegment.lastIndexOf('.', lastIndex - 1);
                if (dotIndex >= 0) {
                    int position = 0;
                    try
                    {
                      position = Integer.parseInt(uriFragmentSegment.substring(dotIndex + 1));
                    }
                    catch (NumberFormatException exception)
                    {
                      throw exception;
                    }
                       return findSVGFigureByPath(position);
                }
            }
            if (dotIndex < 0) {
                return findSVGFigureByPath(0);
            }
        }

        return null;
    }
    
    
    public SVGFigure findSVGFigureByPath(int position){
        if(position<= childList.size()){
            return childList.get(position);
        }
        return null;
    }
    
    public BorderLayer createBorderLayerFigure(int x,int y){
        if(x==0 && y==0){
            return null;
        }
        BorderLayer layer = new BorderLayer(x,y);
        if(!layers.contains(layer)){
            layers.add(layer);
        }
        return layer;
    }

    /**
     * @return the layers
     */
    public List<BorderLayer> getLayers() {
        return this.layers;
    }

    protected SVGFigure findGroupStartAndGroupEndScope(){
        List<SVGFigure> childList = this.getParentFigure().getChildList();
        for(SVGFigure figure:childList){
            if(figure instanceof ScopeFigure){
                return (ScopeFigure) figure;
            }
        }
        return null;
    }
  
    public void addLocation(Rectangle rect) {
        if (node != null) {
            Element element = node.getElement();
            if (BWDiagramUtil.INSTANCE.isGroupStart(element)) {
                SVGFigure figure = findGroupStartAndGroupEndScope();
                Rectangle activityFigureBounds = figure.getBounds().getCopy();
                Rectangle groupStartFigureBounds = new Rectangle();
                groupStartFigureBounds.x = 0;
                groupStartFigureBounds.y = activityFigureBounds.y + activityFigureBounds.height / 2 - 12;
                groupStartFigureBounds.width = 24;
                groupStartFigureBounds.height = 24;
                Rectangle bounds = groupStartFigureBounds;
                setBounds(bounds);
                int y = (bounds.height - 16) / 2 + bounds.y;
                int x = (bounds.width - 16) / 2 + bounds.x;
                String activityTypeId = BWDiagramUtil.INSTANCE.getGroupTypeId(element);
                this.setId(activityTypeId);
                addLocation(this,16, 16, y, x);
            } else if (BWDiagramUtil.INSTANCE.isGroupEnd(element)) {
                SVGFigure figure = findGroupStartAndGroupEndScope();
                Rectangle activityFigureBounds = figure.getBounds();
                Rectangle groupEndFigureBounds = new Rectangle();
                groupEndFigureBounds.x = activityFigureBounds.width + 3;
                groupEndFigureBounds.y = activityFigureBounds.y + activityFigureBounds.height / 2 - 12;
                groupEndFigureBounds.width = 24;
                groupEndFigureBounds.height = 24;
                Rectangle bounds = groupEndFigureBounds;
                setBounds(bounds);
                int y = (bounds.height - 16) / 2 + bounds.y;
                int x = (bounds.width - 16) / 2 + bounds.x;
                String activityTypeId = BWDiagramUtil.INSTANCE.getGroupTypeId(element);
                this.setId(activityTypeId);
                 addLocation(this,16, 16, y, x);
            }else if(BWDiagramUtil.INSTANCE.isOnMessageStart(element)){
                Rectangle bounds = this.bounds;
                bounds.height = 24;
                bounds.width = 24;
                setBounds(bounds);
                int y = (bounds.height - 16) / 2 + bounds.y;
                int x = (bounds.width - 16) / 2 + bounds.x;
                String activityTypeId = BWDiagramUtil.INSTANCE.getOnMessageGroupId(element);
                this.setId(activityTypeId);
                addLocation(this, 16, 16, y, x);
            } else if( BWDiagramUtil.INSTANCE.isOnMessageEnd(element)){
                Rectangle bounds = this.bounds;
                bounds.height = 24;
                bounds.width = 24;
                setBounds(bounds);
                int y = (bounds.height - 16) / 2 + bounds.y;
                int x = (bounds.width - 16) / 2 + bounds.x;
                String activityTypeId = BWDiagramUtil.INSTANCE.getOnMessageGroupId(element);
                this.setId(activityTypeId);
                addLocation(this, 16, 16, y, x);
            }else {
                setBounds(rect);
                if (rect.height < 75) {
                    rect.height = 75;
                }
                if (rect.width < 123) {
                    rect.width = 123;
                }
                rect.width = 123;
                int height = 48;
                int y1 = (height - 48) / 2 + rect.y;
                int x1 = (rect.width - 48) / 2 + rect.x;
                int width = 48;
                String acticityName = BWDiagramUtil.INSTANCE.getActivityName(element);
                this.setName(acticityName);
                addLocation(this, height, width, y1, x1);
            }
        }
    }

   

  
    /**
     * @param element
     * @param height
     * @param width
     * @param y
     * @param x
     */
    public void addLocation(SVGFigure figure, int height, int width, int y, int x) {
        this.setRelativeRoot(true);
        PrecisionRectangle rectangle = figure.translateToAbsolute(new PrecisionRectangle(x,y,width,height));
        rectangle.x = rectangle.x + 10;
        rectangle.y = rectangle.y + 10;
        Element element = figure.getNode().getElement();
        element.addAttribute("x", String.valueOf(rectangle.x));
        element.addAttribute("y", String.valueOf(rectangle.y));
        element.addAttribute("width", String.valueOf(rectangle.width));
        element.addAttribute("height", String.valueOf(rectangle.height));
        if(figure.getIsCollapse()){
            element.addAttribute("collapse", String.valueOf("true"));
        }
        if(figure.getId() != null){
            element.addAttribute("type", figure.getId());
        }
        if(figure.getName() != null){
            element.addAttribute("name", figure.getName());
        }
        System.out.println(rectangle+" =--------=  "+figure.getName());
        figure.getNode().setAbsolutePoint(rectangle);
    }

    public Rectangle getElementRectangle(Element element){
        Rectangle rect =null;
        Element layout = element.element("layoutConstraint");
        if (layout != null){
            String x = layout.attributeValue("x");
            if(x==null){
                x="0";
            }
            String y = layout.attributeValue("y");
            if(y==null){
                y="0";
            }
            String width = layout.attributeValue("width");//default 123
            if(width==null){
                width = "123";
            }
            String height = layout.attributeValue("height");//default 75
            if(height == null){
               height= "75";
            }
           rect = new Rectangle(Integer.parseInt(x),Integer.parseInt(y),Integer.parseInt(width),Integer.parseInt(height));
        }
        return rect;
    }
    /**
     * @param node2
     * @param childFigure
     * @param type2
     * @param child
     * @param rect
     */
    public void adjustScopeEditPart(BWNode node2, SVGFigure childFigure, String type2, Element child, Rectangle rect,int compartmentWidth,int compartmentHeight) {
        
    }

    public final Rectangle getClientArea() {
        return getClientArea(new Rectangle());
    }
    public Rectangle getClientArea(Rectangle rect) {
        rect.setBounds(getBounds());
        rect.crop(getInsets());
        return rect;
    }

    /**
     * @return
     */
    public Insets getInsets() {
        return insets;
    }
    
    /**
     * @param insets the insets to set
     */
    public void setInsets(Insets insets) {
        this.insets = insets;
    }

    public boolean isDefaultAnchor() {
        return relativeReference == null;
    }
    
    public String getTerminal() {
        if (isDefaultAnchor())
            return BWDiagramUtil.BLANK;
        return composeTerminalString(relativeReference);
    }
    
    private String composeTerminalString(PrecisionPoint p) {
        StringBuffer s = new StringBuffer(24);
        s.append(BWNode.TERMINAL_START_CHAR);      // 1 char
        s.append(p.preciseX);       // 10 chars
        s.append(BWNode.TERMINAL_DELIMITER_CHAR);  // 1 char
        s.append(p.preciseY);       // 10 chars
        s.append(BWNode.TERMINAL_END_CHAR);        // 1 char
        return s.toString();                // 24 chars max (+1 for safety, i.e. for string termination)
    }

    public Point getLocation(Point reference) {
        Point ownReference = normalizeToStraightlineTolerance(reference, getReferencePoint(), STRAIGHT_LINE_TOLERANCE);
        
        Point location = getLocation(ownReference, reference);
        if (location == null) {
            location = getLocation(new PrecisionPoint(getBox().getCenter()), reference);
            if (location == null) {
                location = getBox().getCenter();
            }
        }
        
        return location;
    }
    protected Rectangle getBox() {
        
        return new Rectangle();
    }
    protected PointList getIntersectionPoints(Point ownReference, Point foreignReference) {
        PointList polygon = getPolygonPoints().getCopy();
        polygon = this.translateToAbsolute(polygon);
        return (new LineSeg(ownReference, foreignReference)).getLineIntersectionsWithLineSegs(polygon); 
    }
    
    protected Point getLocation(Point ownReference, Point foreignReference) {
        PointList intersections = getIntersectionPoints(ownReference, foreignReference);
        if (intersections!=null && intersections.size()!=0) {
            Point location = PointListUtilities.pickClosestPoint(intersections,
                    foreignReference);
            return location;
        }
        return null;
    }
    
    protected Point normalizeToStraightlineTolerance(Point foreignReference, Point ownReference, int tolerance) {
        PrecisionPoint preciseOwnReference = new PrecisionPoint(ownReference);
        PrecisionPoint normalizedReference = (PrecisionPoint)preciseOwnReference.getCopy();
        PrecisionPoint preciseForeignReference = new PrecisionPoint(foreignReference);
        if (Math.abs(preciseForeignReference.preciseX - preciseOwnReference.preciseX) < tolerance) {
            normalizedReference.preciseX = preciseForeignReference.preciseX;
            normalizedReference.updateInts();
            return normalizedReference;
        }
        if (Math.abs(preciseForeignReference.preciseY - preciseOwnReference.preciseY) < tolerance) {
            normalizedReference.preciseY = preciseForeignReference.preciseY;
            normalizedReference.updateInts();
        }
        return normalizedReference;
    }
    /**
     * 
     */
    public void layout() {
        if(this.bounds != null){
            addLocation(this.bounds);
        }
        if(childList != null ){
            for(SVGFigure figure:childList){
                figure.layout();
            }
        }
    }

    /**
     * @return the isCollapse
     */
    public Boolean getIsCollapse() {
        return this.isCollapse;
    }

    /**
     * @param isCollapse the isCollapse to set
     */
    public void setIsCollapse(Boolean isCollapse) {
        this.isCollapse = isCollapse;
    }

    /**
     * @return the isRelativeRoot
     */
    public Boolean isRelativeRoot() {
        return this.isRelativeRoot;
    }

    /**
     * @param isRelativeRoot the isRelativeRoot to set
     */
    public void setRelativeRoot(Boolean isRelativeRoot) {
        this.isRelativeRoot = isRelativeRoot;
    }
    
    public Point getAbsolutePointPosition(){
        return this.absolutePoint;
    }

    /**
     * @return the absoluteBound
     */
    public Rectangle getAbsoluteBound() {
        return this.absoluteBound;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    

}

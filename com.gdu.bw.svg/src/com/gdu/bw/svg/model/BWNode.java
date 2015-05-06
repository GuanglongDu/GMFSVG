
package com.gdu.bw.svg.model;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.gdu.bw.svg.model.figure.SVGFigure;

/**
 * @author <a>Davy Du</a>
 *
 * @since 1.0.0
 */
public class BWNode {
    private String id;
    private String selfId;
    protected String nodeName;
    protected Element element;
    protected BWNode parentNode;
    protected List<BWNode> childList;
    
    protected List<BWNode> avtivities;
    protected List<BWNode> faultHandlers;
    protected List<BWNode> catches;
    protected List<BWNode> reference;
    protected List<BWNode> compensationHandlers;
    protected List<BWNode> catchAlls;
    protected List<BWNode> eventHandlers;
    protected List<BWNode> events;
    protected List<BWNode> messages;
    protected List<BWNode> links;
    protected List<BWNode> variables;
    public SVGFigure figure;
    private Rectangle absolutePoint = new Rectangle();
    private PointList pointlist = new PointList();
    public final static char TERMINAL_START_CHAR = '(';
    public final static char TERMINAL_DELIMITER_CHAR = ',';
    public final static char TERMINAL_END_CHAR = ')'; 
   
    public BWNode() {
        initChildList();
    }
 
    public BWNode(BWNode parentNode) {
        this.parentNode = parentNode;
        initChildList();
    }
 
    public BWNode getParentNode() {
        return parentNode;
    }
 
    public void addChildNode(BWNode element) {
        initChildList();
        childList.add(element);
    }
 
    public void initChildList() {
        if (childList == null)
            childList = new ArrayList<BWNode>();
        if (avtivities == null)
            avtivities = new ArrayList<BWNode>();
        if (faultHandlers == null)
            faultHandlers = new ArrayList<BWNode>();
        if (catches == null)
            catches = new ArrayList<BWNode>();
        if (reference == null)
            reference = new ArrayList<BWNode>();
        if (compensationHandlers == null)
            compensationHandlers = new ArrayList<BWNode>();
        if(eventHandlers == null)
            eventHandlers =  new ArrayList<BWNode>(); 
        if(events == null)
            events = new ArrayList<BWNode>();
        if (catchAlls == null)
            catchAlls = new ArrayList<BWNode>();
        if(messages == null)
            messages =  new ArrayList<BWNode>();
        if(links == null)
            links = new ArrayList<BWNode>();
        if(variables == null)
            variables = new ArrayList<BWNode>();
    }

    public List<BWNode> getChildList() {
        return childList;
    }
 
    public BWNode findBWNodeByPath(String uriFragmentSegment) {
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
                    String listName = uriFragmentSegment.substring(1, dotIndex);
                    int position = 0;
                    try
                    {
                      position = Integer.parseInt(uriFragmentSegment.substring(dotIndex + 1));
                    }
                    catch (NumberFormatException exception)
                    {
                      throw exception;
                    }
                    
                    //if (position < avtivities.size()){
                       return findBWNodeByPath(listName,position);
                    //}
                }
            }
            if (dotIndex < 0) {
                return findBWNodeByPath(uriFragmentSegment,0);
            }
        }

        return null;
    }
    
    public BWNode findBWNodeByPath(String listName,int position) {
        if("@activity".equals(listName) || "activities".equals(listName)){
            return this.avtivities.get(position);
        }else if("@faultHandlers".equals(listName)){
            return this.faultHandlers.get(position);
        }else if("catch".equals(listName)){
            return this.catches.get(position);
        }else if("@compensationHandler".equals(listName)){
           return compensationHandlers.get(position);
        }else if("@process".equals(listName)){
            return this;
        }else if("@catchAll".equals(listName)){
            return this.catchAlls.get(position);
        }else if("@eventHandlers".equals(listName)){
            return this.eventHandlers.get(position);
        }else if("events".equals(listName)){
            return this.events.get(position);
        }else if("messages".equals(listName)){
            return this.messages.get(position);
        }else if("@variables".equals(listName)){
            return this.variables.get(position);
        }
        return null;
    }
    
    public BWNode findTreeNodeByBpelId(String id) {
        if (this.selfId .equals(id))
            return this;
        if (childList.isEmpty() || childList == null) {
            return null;
        } else {
            int childNumber = childList.size();
            for (int i = 0; i < childNumber; i++) {
                BWNode child = childList.get(i);
                BWNode resultNode = child.findTreeNodeByBpelId(id);
                if (resultNode != null) {
                    return resultNode;
                }
            }
            return null;
        }
    }
    
    public Boolean isHaveXpdlId(){
        if(element != null){
            if(element.attribute("xpdlId") != null){
                return true;
            }else {
                int childNumber = childList.size();
                for (int i = 0; i < childNumber; i++) {
                    BWNode child = childList.get(i);
                    if(child.getElement().attribute("xpdlId") != null){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * @return the element
     */
    public Element getElement() {
        return this.element;
    }

    /**
     * @param element the element to set
     */
    public void setElement(Element element) {
        this.element = element;
    }

    /**
     * @return the selfId
     */
    public String getSelfId() {
        return this.selfId==null?"":this.selfId;
    }

    /**
     * @param selfId the selfId to set
     */
    public void setSelfId(String selfId) {
        this.selfId = selfId;
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
     * @return the figure
     */
    public SVGFigure getFigure() {
        return this.figure;
    }

    /**
     * @param figure the figure to set
     */
    public void setFigure(SVGFigure figure) {
        this.figure = figure;
    }

    /**
     * @return the avtivities
     */
    public List<BWNode> getAvtivities() {
        return this.avtivities;
    }

    /**
     * @param avtivities the avtivities to set
     */
    public void addAvtivities(BWNode avtivity) {
        initChildList();
        this.avtivities.add(avtivity);
    }

    /**
     * @return the handlers
     */
    public List<BWNode> getFaultHandlers() {
        return this.faultHandlers;
    }

    /**
     * @param handlers the handlers to set
     */
    public void addFaultHandlers(BWNode handler) {
        initChildList();
        this.faultHandlers.add(handler);
    }
    
    
    public List<BWNode> getEventHandlers() {
        return this.eventHandlers;
    }

    /**
     * @param handlers the handlers to set
     */
    public void addEventHandlers(BWNode handler) {
        initChildList();
        this.eventHandlers.add(handler);
    }
    
    public List<BWNode> getMessages() {
        return this.messages;
    }

    /**
     * @param handlers the handlers to set
     */
    public void addMessages(BWNode handler) {
        initChildList();
        this.messages.add(handler);
    }
    
    
    /**
     * @return the handlers
     */
    public List<BWNode> getCompensationHandlers() {
        return this.compensationHandlers;
    }

    /**
     * @param handlers the handlers to set
     */
    public void addCompensationHandlers(BWNode handler) {
        initChildList();
        this.compensationHandlers.add(handler);
    }
    /**
     * @return the catches
     */
    public List<BWNode> getCatches() {
        return this.catches;
    }

    /**
     * @param catches the catches to set
     */
    public void addCatches(BWNode catche) {
        initChildList();
        this.catches.add(catche);
    }

    public List<BWNode> getCatcheAlls() {
        return this.catchAlls;
    }

    /**
     * @param catches the catches to set
     */
    public void addCatchAlls(BWNode catche) {
        initChildList();
        this.catchAlls.add(catche);
    }
    
    /**
     * @return the reference
     */
    public List<BWNode> getReference() {
        return this.reference;
    }

    /**
     * @param reference the reference to set
     */
    public void addReference(BWNode reference) {
        initChildList();
        this.reference.add(reference);
    }

    /**
     * @param eventNode
     */
    public void addEvents(BWNode eventNode) {
        initChildList();
        this.events.add(eventNode);
    }
 
    public List<BWNode> getEvents() {
        return this.events;
    }

    public void addLinks(BWNode linkNode) {
        initChildList();
        this.links.add(linkNode);
    }
 
    public List<BWNode> getLinks() {
        return this.links;
    }
    
    public void addVariables(BWNode variablesNode) {
        initChildList();
        this.variables.add(variablesNode);
    }
 
    public List<BWNode> getVariables() {
        return this.variables;
    }

    
    public static PrecisionPoint parseTerminalString(String terminal) {
        try {
            return new PrecisionPoint(Double.parseDouble(terminal.substring(
                terminal.indexOf(TERMINAL_START_CHAR) + 1,
                terminal.indexOf(TERMINAL_DELIMITER_CHAR))),
                Double.parseDouble(terminal.substring(terminal
                    .indexOf(TERMINAL_DELIMITER_CHAR) + 1,
                    terminal.indexOf(TERMINAL_END_CHAR))));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return the absolutePoint
     */
    public Rectangle getAbsolutePoint() {
        return this.absolutePoint;
    }

    /**
     * @param absolutePoint the absolutePoint to set
     */
    public void setAbsolutePoint(Rectangle absolutePoint) {
        this.absolutePoint = absolutePoint;
    }

    /**
     * @return the pointlist
     */
    public PointList getPointlist() {
        return this.pointlist;
    }

    /**
     * @param pointlist the pointlist to set
     */
    public void setPointlist(PointList pointlist) {
        this.pointlist = pointlist;
    }
    
}

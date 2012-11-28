import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "Cruiser Command" application. 
 * "Cruiser Command" is a very simple, text based adventure game. Users should
 *  try to escape the spaceship before it is destroyed.
 *  
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.10
 */

public class Room{
	
    private String description;
    private String long_desc;
    private HashMap<String, Door> exits;        // stores exits of this room.
    private Inventory contents;
    private boolean dark;
	private String goString;
	private String name;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this(description, null);
        dark = false;
    }
    
    public Room(String description, boolean dark){
    	this(description);
    	this.dark = dark;
    }
    
    public Room(String description, boolean dark, String goString, String name){
    	this(description,dark);
    	this.goString = goString;
    	this.name = name;
    }
    
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description, String long_desc) 
    {
    	this.goString = "";
    	this.name = "";
        this.description = description;
        this.long_desc = long_desc;
        this.contents = new Inventory();
        exits = new HashMap<String, Door>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, new Door(this,neighbor,direction,getOppositeDirection(direction)));
    }
    
    /**
     * Gets the opposite direction. "west" will return "east". "north" will return "south" and so on.
     */
    private String getOppositeDirection(String direction){
    	if(direction.equals("south")){
    		return "north";
    	} else if(direction.equals("north")){
    		return "south";
    	} else if(direction.equals("west")){
    		return "east";
    	} else {
    		return "west";
    	}
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
    	if(this.dark){
	    	for(Object o : this.getContents()){
	    		if(o.getClass() == Player.class){
	    			if(((Player) o).isLampLit() && ((Player) o).hasItem("lantern")){
	    				return description + "\n" + getExitString()+"\n" + getInventory().getList();
	    			}
	    		}
	    	}
	    	return "It is pitch black. You are likely to be eaten by a grue.";
    	} else {
    		return description + "\n" + getExitString()+"\n" + getInventory().getList();
    	}
    }
    
    public void setDescription(String desc){
    	this.description = desc;
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits: ";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
        	if(!exit.equals("")){
        		returnString += exit + ", ";
        	}
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Door getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    public void addDoor(String direction, Door d){
    	exits.put(direction, d);
    }
    
    public Inventory getInventory(){
    	return contents;
    }
    
    public ArrayList<Object> getContents(){
    	return contents.getObjects();
    }

	public String getGoString() {
		return goString;
	}

	public void setGoString(String goString) {
		this.goString = goString;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}


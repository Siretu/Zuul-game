import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of KROZ" application. 
 * "World of KROZ" is a very simple, text based adventure game. Users should
 *  try to find a lucia bun before they starve
 *  
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via doors.  For each existing exit, the room 
 * stores a reference to a door which can connect it to the other room.
 * 
 * @version 2012.12.01
 */

public class Room{
	
    private String description;				// The room description.
    private HashMap<String, Door> exits;    // stores exits of this room.
    private Inventory contents;				// The room's inventory. This is where room contents and the player is stored.
    private boolean dark;					// Is the room dark or not? You can't see what's inside a dark room if you don't have a light source.
	private String name;					// The room's name.

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard". It also creates a new inventory for the room,
     * as well as initializing the instance variables.
     * 
     * @param description The room's description.
     */
    public Room(String description) 
    {
    	dark = false;
    	this.name = "";
        this.description = description;
        this.contents = new Inventory();
        exits = new HashMap<String, Door>();
    }
    
    
    /**
     * Creates a room like in the Room(String) constructor,
     * but also sets the dark parameter. If a room is dark,
     * you can't see what it looks like if you don't have something to light it for you.
     * 
     * @param description	the room's description
     * @param dark			if the room is dark or not
     */
    public Room(String description, boolean dark){
    	this(description);
    	this.dark = dark;
    }
    
    
    /**
     * Creates a room like in Room(String,boolean) but also specifies a name and a goString.
     * The goString is displayed when you 
     * 
     * @param description	the room's description
     * @param dark			if the room is dark or not
     * @param name			name of the room
     */
    public Room(String description, boolean dark, String name){
    	this(description,dark);
    	this.name = name;
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
	    	for(GameObject o : this.getContents()){
	    		if(o.getClass() == Player.class){
	    			if(((Player) o).isLampLit() && ((Player) o).hasItem("lantern")){
	    				return description + "\n" + getExitString()+"\n" + getInventory().getList();
	    			}
	    		}
	    	}
	    	return "It is pitch black. You are likely to be eaten by a grue.";
    	} else {
    		return description + "\n" + getInventory().getList();
    	}
    }
    
    
    
    /**
     * Sets the description of the room.
     * 
     * @param desc	the new description of the room.
     */
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
     * Return the Door that is used if we go from this room in direction
     * "direction". If there is no Door in that direction, return null.
     * 
     * @param direction The exit's direction.
     * @return The door in the given direction.
     */
    public Door getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * Adds a door to the room in the direction.
     * 
     * @param direction		The door's direction
     * @param d				The door in that direction
     */
    public void addDoor(String direction, Door d){
    	exits.put(direction, d);
    }
    
    /**
     * Returns the room's inventory. This inventory is also where room objects and players are stored.
     * 
     * @return	the room's inventory.
     */
    public Inventory getInventory(){
    	return contents;
    }
    
    /**
     * Used to get the contents of the room's inventory.
     * 
     * @return an arraylist filled with the contents of the room's inventory.
     */
    public ArrayList<GameObject> getContents(){
    	return contents.getObjects();
    }

	/**
	 * Returns the Room's name
	 * 
	 * @return the room name
	 */
	public String getName() {
		return name;
	}

	
	/** 
	 * Sets the room name
	 * @param name	the name
	 */
	public void setName(String name) {
		this.name = name;
	}
}


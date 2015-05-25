/**
 * Class Door - a passage between rooms in an adventure game.
 *
 * This class is part of the "World of KROZ" application. 
 * "World of KROZ" is a very simple, text based adventure game. Users should
 *  try to find a lucia bun before they starve
 *  
 * A "Door" represents one passage between two rooms. It can also handle locked passages.
 * 
 * @version 2012.12.01
 */

public class Door {
	private Room r1;				// The first room connected by the door.
	private Room r2;				// The second room connected by the door.
	private String lockedMessage;	// The message showed if you try to use a door that is locked. Unlock a door by setting this to ""
	
	/**
	 * Creates a door between two rooms. 
	 * This will automatically modify the Room's to add in the door reference in the Room objects.
	 * 
	 * @param r1	this is the first room to connect.
	 * @param r2	this is the second room to connect. 
	 * @param dir1	this is the direction from the first room that brings you to the second room.
	 * @param dir2	this is the direction from the second room that brings you to the first room.
	 */
	public Door(Room r1, Room r2, String dir1, String dir2){
		this.r1 = r1;
		this.r2 = r2;
		r1.addDoor(dir1, this);
		r2.addDoor(dir2, this);
		this.lockedMessage = "";
	}
	
	
	/**
	 * Create a door like normal but lock it with the specified lockedMessage.
	 * 
	 * @param r1			first room
	 * @param r2			second room
	 * @param dir1			direction from first room
	 * @param dir2			direction from second room
	 * @param lockedMessage	message to show when you try to go in a locked direction
	 */
	public Door(Room r1, Room r2, String dir1, String dir2, String lockedMessage){
		this(r1,r2,dir1,dir2);
		this.lockedMessage = lockedMessage;
	}
	
	
	/**
	 * Returns the connected room if the door is not locked. 
	 * If it's locked, the method returns null.
	 * 
	 * @param p		the player that wants to go to the room
	 * @return		returns the connected room if it's not locked.
	 */
	public Room getConnectedRoom(Player p){
		if(this.getLockedMessage().equals("")){
			if(p.getCurrentRoom() == r1){
				return r2;
			} else {
				return r1;
			}
		} else {
			return null;
		}
	}

	/**
	 * Get the locked message.
	 * 
	 * @return locked message
	 */
	public String getLockedMessage() {
		return lockedMessage;
	}

	/**
	 * Set the locked message.
	 * 
	 * @param lockedMessage	new locked message
	 */
	public void setLockedMessage(String lockedMessage) {
		this.lockedMessage = lockedMessage;
	}
	
	
}

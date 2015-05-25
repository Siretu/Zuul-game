import java.util.ArrayList;

/**
 * Class Player - a player in the text adventure world.
 *
 * This class is part of the "World of KROZ" application. 
 * "World of KROZ" is a very simple, text based adventure game. Users should
 *  try to find a lucia bun before they starve
 *  
 * A "Player" class handles the player and monster(s). Keeps track of light source and a personal inventory.
 * 
 * @version 2012.12.01
 */

public class Player extends GameObject{
	private Inventory contents;
	private Room currentRoom;
	private boolean lampLit;
	
	/**
	 * Creates a new player with the specified name and description.
	 * 
	 * @param name	the player's name
	 * @param desc	the player's description
	 */
	public Player(String name, String desc) {
		super(name,desc);
		this.listable = false;;
		this.contents = new Inventory("p_inv","");
		lampLit = false;
	}
	
    /**
     * Returns the player's inventory
     * 
     * @return the inventory of the player
     */
    public Inventory getInventory(){
    	return contents;
    }
    
    /**
     * Returns the contents of the player's inventory.
     * 
     * @return an arraylist with game objects from the player inventory.
     */
    public ArrayList<GameObject> getContents(){
    	return contents.getObjects();
    }

    
	/**
	 * Returns the room that player is in right now.
	 * 
	 * @return the player's current room
	 */
	public Room getCurrentRoom() {
		return currentRoom;
	}

	
	/**
	 * Sets the current room and makes sure to remove the player object from
	 * the room's inventory as well as adding the player to the new room's inventory.
	 * 
	 * @param newRoom	the new room
	 */
	public void setCurrentRoom(Room newRoom) {
		if(this.currentRoom != null){
			this.currentRoom.getInventory().removeObject(this);
		}
		this.currentRoom = newRoom;
		this.currentRoom.getInventory().addObject(this);
	}
	
	
	/**
	 * Searches the player's inventory for an item with a specific name.
	 * 
	 * @param name	the name of the item to search for.
	 * @return	true if the player is carrying such an item, otherwise false.
	 */
	public boolean hasItem(String name){
		for(GameObject o : this.getContents()){
			if(o.getName().equals(name)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks the state of the lamp. Returns true or false depending on if it's providing light or not.
	 * 
	 * @return	the state of the lamp
	 */
	public boolean isLampLit() {
		return lampLit;
	}

	
	/**
	 * Sets the state of the lamp. 
	 * 
	 * @param lampLit	true if lamp is lit, otherwise false
	 */
	public void setLampLit(boolean lampLit) {
		this.lampLit = lampLit;
	}
	
	
	/**
	 *	Kills the player. Displays a death message and starts a new game. 
	 */
	public void die(){
		System.out.println("I'm afraid you are\ndead.\n\n   **** You have died ****\n\nNow let's take a look here... Well, you probably deserve another chance.\nI can't quite fix you up completely, but you can't have everything.");
    	Game game = new Game();
    	game.play();
	}
	
	/**
	 * A command method to attack a player. 
	 * Tell the player that he can't attack himself and do different stuff depending on weapon.
	 * 
	 * @param p		a reference to player is sent to all commands to refer to the player who used the command
	 */
	@Command
	public void attack(Player p){
		if(!p.equals(this)){
			if(p.hasItem("sword")){
				System.out.println("You attack the " + this.getName() + " with your sword. The " + this.getName() + " dies.");
				p.getCurrentRoom().getInventory().removeObject(this);
				p.getCurrentRoom().setDescription("This is a small room with a passage to the east. Bloodstains and deep scratches \n(perhaps made by an axe) mar the walls.");
				p.getCurrentRoom().getExit("east").setLockedMessage("");
			} else if(p.hasItem("knife")){
				System.out.println("You attack the " + this.getName() + " with your knife. The " + this.getName() + " dies.");
				p.getCurrentRoom().getInventory().removeObject(this);
				p.getCurrentRoom().setDescription("This is a small room with a passage to the east. Bloodstains and deep scratches \n(perhaps made by an axe) mar the walls.");
				p.getCurrentRoom().getExit("east").setLockedMessage("");
			} else {
				System.out.println("You attack the " + this.getName() + " with your bare hands. \nIt proves to be very inefficient and the " + this.getName() + "attacking you until you die.");
				p.die();
			}
		} else {
			System.out.println("You can't attack yourself. Are you stupid!?");
		}
		
	}
}

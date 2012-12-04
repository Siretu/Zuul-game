import java.util.ArrayList;

/**
 * Class GameObject - an object in the adventure game world.
 *
 * This class is part of the "World of KROZ" application. 
 * "World of KROZ" is a very simple, text based adventure game. Users should
 *  try to find a lucia bun before they starve
 *  
 * A "GameObject" class handles all the game objects in the world. A lot of commands can be used
 * on game objects and therefore this class contains a lot of command methods.
 * 
 * @author  Erik Ihrén
 * @version 2012.12.01
 */

public class GameObject {
	protected String description;			// The object's description
	protected String name;					// The object's name
	protected boolean listable;				// If the object is listable, it will show up in the item list of a room.
	protected boolean portable;				// If the object is portable, it can be picked up.
	protected String id;					// An unique string identifier
	protected ArrayList<String> otherNames;	// A list of other names that the player can use to specify a target. "take rug" could affect a carpet object if it had "rug" in otherNames.
	

	/**
	 * Creates a game object with the specified name and description.
	 * The other instance variables are set to default values.
	 * 
	 * @param name		game object's name
	 * @param desc		game object's description
	 */
	public GameObject(String name, String desc){
		this.description = desc;
		this.name = name;
		this.listable = true;
		this.portable = true;
		this.id = "";
		this.otherNames = new ArrayList<String>();
	}
	
	/**
	 * Creates a game object like normal but also specifies if it's listable and/or portable.
	 * 
	 * @param name		game object's name
	 * @param desc		game object's description
	 * @param listable	is the game object shown in the content list of inventories?
	 * @param portable	can players pick up the game object?
	 */
	public GameObject(String name, String desc, boolean listable, boolean portable){
		this(name,desc);
		this.listable = listable;
		this.portable = portable;
	}
	
	/**
	 * Creates a game object like normal but also specifies if it's listable and/or portable as well as an unique string id.
	 * 
	 * @param name		game object's name
	 * @param desc		game object's description
	 * @param listable	is the game object shown in the content list of inventories?
	 * @param portable	can players pick up the game object?
	 * @param id		a unique string identifier
	 */
	public GameObject(String name, String desc, boolean listable, boolean portable, String id){
		this(name,desc,listable,portable);
		this.id = id;
	}
	
	/**
	 * @return the name of the game object.
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * @return the description of the game object.
	 */
	public String getDesc(){
		if(this.description.equals("")){
			return "There's nothing special about the "+this.getName().toLowerCase();
		} else {
			return this.description;
		}
	}
	
	/**
	 * @return true if it should be shown in the inventory, false otherwise.
	 */
	public boolean isListable() {
		return listable;
	}

	
	/**
	 * @return the unique string ID.
	 */
	public String getId() {
		return id;
	}

	
	/**
	 * @return the list of other names
	 */
	public ArrayList<String> getOtherNames() {
		return otherNames;
	}
	
	/**
	 * @param name adds another name that can be used by the player to refer to an item.
	 */
	public void addOtherName(String name) {
		otherNames.add(name);
	}


	/**
	 * A command method to investigate a game object.
	 * The method prints out the description and name of the item.
	 * 
	 * @param p		a reference to player is sent to all commands to refer to the player who used the command
	 */
	@Command
	public void investigate(Player p){
		System.out.println(("You examine the "+this.getName()+". "+this.getDesc()));
	}
	
	/**
	 * A command method that just does the same thing as investigate. It exists to give an alternative name for the same command.
	 * 
	 * @param p		a reference to player is sent to all commands to refer to the player who used the command
	 */
	@Command
	public void examine(Player p){
		this.investigate(p);
	}
	
	/**
	 * A command method used to open items. It's set up to do specific stuff depending on what object is opened.
	 * 
	 * @param p		a reference to player is sent to all commands to refer to the player who used the command
	 */
	@Command
	public void open(Player p){
		if(this.getId().equals("kitchenWindow")){
			System.out.println("With great effort, you open the window far enough to allow entry");
			p.getCurrentRoom().getExit("inside").setLockedMessage("");
			p.getCurrentRoom().getExit("in").setLockedMessage("");
		} else if(this.getId().equals("engravedDoor")){
			System.out.println("The door cannot be opened");
		} else if(this.getId().equals("trapdoor")){
			if(p.getCurrentRoom().getName().equals("Cellar")){
				System.out.println("The door is locked from above.");
			} else {
				System.out.println("The door reluctantly opens to reveal a rickety staircase descending into darkness.");
				p.getCurrentRoom().getExit("down").setLockedMessage("");
			}
		} else {
			System.out.println("You cannot open the " + this.getName());
		}
		
	}
	
	/**
	 * A command method to print a response to the destroy command.
	 * 
	 * @param p		a reference to player is sent to all commands to refer to the player who used the command
	 */
	@Command
	public void destroy(Player p){
		if(this.getId().equals("kitchenWindow")){
			System.out.println("Despite your violent tendencies, you manage to restrain yourself from breaking the window.");
		} else {
			System.out.println("You cannot destroy the " + this.getName());
		}
	}
	
	/**
	 * A command method to close an object. It's set up to do specific things depending on the object it's used on.
	 * 
	 * @param p		a reference to player is sent to all commands to refer to the player who used the command
	 */
	@Command
	public void close(Player p){
		if(this.getId().equals("trapdoor")){
			System.out.println("The door swings shut and closes.");
			p.getCurrentRoom().getExit("down").setLockedMessage("The trap door is closed");
		} else {
			System.out.println("You cannot close the " + this.getName());
		}
	}
	
	/**
	 * A command method to move an object. It's set up to move the carpet in the living room.
	 * 
	 * @param p		a reference to player is sent to all commands to refer to the player who used the command
	 */
	@Command
	public void move(Player p){
		if(this.getId().equals("orientalRug")){
			if(p.getCurrentRoom().getExit("down").getLockedMessage().equals("The trap door is closed") || p.getCurrentRoom().getExit("down").getLockedMessage().equals("")){
				System.out.println("Having moved the carpet previously, you find it impossible to move it again.");
			} else {
				System.out.println("With a great effort, the carpet is moved to one side of the room, revealing the dusty cover of a closed trapdoor.");
				GameObject trapdoor = new GameObject("trapdoor", "The trap door is closed.",false,false,"trapdoor");
				trapdoor.addOtherName("trap door");
				p.getCurrentRoom().getInventory().addObject(trapdoor);
				p.getCurrentRoom().getExit("down").setLockedMessage("The trap door is closed");
			}
		} else {
			System.out.println("You can't move the " + this.getName().toLowerCase());
		}
	}
	
	/**
	 * A command method to give an alternative name to move.
	 * 
	 * @param p		a reference to player is sent to all commands to refer to the player who used the command
	 */
	@Command
	public void pull(Player p){
		this.move(p);
	}
	
	/**
	 * A command method to give an alternative name to move.
	 * 
	 * @param p		a reference to player is sent to all commands to refer to the player who used the command
	 */
	@Command
	public void push(Player p){
		this.move(p);
	}
	
	/**
	 * A command method to give an alternative name to move.
	 * 
	 * @param p		a reference to player is sent to all commands to refer to the player who used the command
	 */
	@Command
	public void remove(Player p){
		this.move(p);
	}
	
	/**
	 * A command method used to use game objects. Currenly only used by the lantern.
	 * 
	 * @param p		a reference to player is sent to all commands to refer to the player who used the command
	 */
	@Command
	public void use(Player p){
		this.light(p);
	}
	
	/**
	 * A command method used to light a game object. Currently only used by the lantern
	 * 
	 * @param p		a reference to player is sent to all commands to refer to the player who used the command
	 */
	@Command
	public void light(Player p){
		if(this.getName().equals("lantern")){
			if(p.isLampLit()){
				System.out.println("It is already on.");
			} else {
				System.out.println("The brass lantern is now on.");
				p.setLampLit(true);
			}
		}
	}
}

import java.util.ArrayList;

/**
 * Class Inventory - a collection of objects in an adventure game.
 *
 * This class is part of the "World of KROZ" application. 
 * "World of KROZ" is a very simple, text based adventure game. Users should
 *  try to find a lucia bun before they starve
 *  
 * An "Inventory" represents a collection of objects in the game. It can be opened by the
 * player and has several methods for managing the list of items.
 * 
 * @author  Erik Ihrén
 * @version 2012.12.01
 */

public class Inventory extends Item{
	private ArrayList<GameObject> objects;	// The list of items
	private boolean open;					// A boolean representing an open/closed state
	private String openMessage;				// The message showed when opened
	
	/**
	 * Default constructor. Creates an inventory with an empty name and description.
	 */
	public Inventory(){
		this("","");
	}
	
	/**
	 * Creates an inventory with a specific name and description.
	 * 
	 * @param name	the inventory's name
	 * @param desc	the inventory's description
	 */
	public Inventory(String name, String desc){
		super(name, desc);
		objects = new ArrayList<GameObject>();
		open = true;
	}
	
	/**
	 * Creates an openable inventory. It creates an inventory just like 
	 * normal but also sets the open flag and the open message.
	 * 
	 * @param name	inventory name
	 * @param desc	inventory description
	 * @param open	true if the inventory should be openable
	 * @param openMessage	the message shown when the inventory is opened
	 */
	public Inventory(String name, String desc, boolean open, String openMessage){
		this(name,desc);
		this.open = open;
		this.openMessage = openMessage;
	}
	
	/**
	 * Adds an object to the inventory.
	 * 
	 * @param newObject the added object
	 */
	public void addObject(GameObject newObject){
		objects.add(newObject);
	}
	
	/**
	 * Removes an object from the inventory.
	 * 
	 * @param removedObject the item to be removed
	 */
	public void removeObject(GameObject removedObject){
		objects.remove(removedObject);
	}

	/**
	 * Used to get the correct a/an article for a word.
	 * 
	 * @param word  the word to get the article of
	 * @return a/an depending on the word
	 */
	public String correctArticle(String word){
		word = word.toLowerCase();
		char[] vowels = {'a','e','i','u','o','y','å','ä','ö'};
		for(char c : vowels){
			if(word.charAt(0) == c){
				return "an";
			}
		}
		return "a";
	}
	
	/**
	 * Returns a print-friendly string with the contents of the inventory.
	 * 
	 * @return the string with the list of contents.
	 */
	public String getList(){
		String list;
		list = "There is: ";
		for(GameObject o : objects){
			if(o.isListable() || this.getName().equals("p_inv")){
				list += correctArticle(o.getName()) + " " + o.getName()+", ";
			}
		}
		if(list.equals("There is: ")){
			if(this.getName().equals("p_inv")){
				list = "Your inventory is empty";
			} else if(this.getName().equals("")){
				list = "There is nothing here.";
			} else {
				list = "There is nothing in";
			}
		}
		return list;
	}
	
	/**
	 * Returns the list of items contained in the inventory.
	 * 
	 * @return	the list of contents
	 */
	public ArrayList<GameObject> getObjects(){
		return objects;
	}

	/**
	 * Adds an item to the inventory
	 * 
	 * @param item	the item to add
	 */
	public void addItem(Item item) {
		objects.add(item);
	}

	/**
	 * Removes an item from the inventory.
	 * 
	 * @param item	the item to remove
	 */
	public void removeItem(Item item) {
		objects.remove(item);
	}
	
	
	/**
	 * Returns the state of the open variable.
	 * 
	 * @return true if it's open and false otherwise.
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * A command method to investigate an object. It shows the item description.
	 * 
	 * @param p		a reference to player is sent to all commands to refer to the player who used the command
	 */
	@Command
	public void investigate(Player p){
		if(open){
			System.out.println(("You investigate the "+this.name+". "+this.description + ". " + getList()));
		} else {
			System.out.println(("You investigate the "+this.name+". "+this.description + "."));
		}
	}
	
	/**
	 * A command method to open an object. It prints different message depending on if it's open or not.
	 * 
	 * @param p		a reference to player is sent to all commands to refer to the player who used the command
	 */
	@Command
	public void open(Player p){
		if(open){
			System.out.println("It is already open");
		} else {
			this.open = true;
			System.out.println(openMessage);
		}
		
	}
}

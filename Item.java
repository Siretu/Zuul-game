/**
 * Class Item - an item in the adventure game world.
 *
 * This class is part of the "World of KROZ" application. 
 * "World of KROZ" is a very simple, text based adventure game. Users should
 *  try to find a lucia bun before they starve
 *  
 * An "Item" class handles the items in the world. They extend the GameObject class and works similarly. 
 * Items are meant to be picked up and dropped by players.
 * 
 * @version 2012.12.01
 */

public class Item extends GameObject{
	private Player holder;	// The player that holds this item.
	
	/**
	 * Creates an item with a specific name and description.
	 * 
	 * @param name	the item's name
	 * @param desc	the item's description
	 */
	public Item(String name, String desc){
		super(name, desc);
		this.holder = null;
	}
	
	/**
	 * Creates an item like normal but also specifies the holder
	 * 
	 * @param name		the item's name
	 * @param desc		the item's description
	 * @param holder	the item's holder
	 */
	public Item(String name, String desc, Player holder){
		super(name,desc);
		this.holder = holder;
	}

	
	/**
	 * A command method to take an item. It checks if you already have the item 
	 * 
	 * @param p		a reference to player is sent to all commands to refer to the player who used the command
	 */
	@Command
	public void take(Player p){
		if(this.holder != p){
			p.getInventory().addItem(this);
			p.getCurrentRoom().getInventory().removeItem(this);	//TODO: Bug if the item is inside an inventory that is not the room inventory, it is still left in the inventory.
			this.holder = p;
			System.out.println("You took the "+this.getName().toLowerCase());
		} else {
			System.out.println("You're already carrying the "+this.getName().toLowerCase());
		}
	}
	
	/**
	 * A command method to drop an item. The item is then switched from 
	 * the player's inventory to the room's inventory.
	 * 
	 * @param p		a reference to player is sent to all commands to refer to the player who used the command
	 */
	@Command
	public void drop(Player p){
		if(this.holder == p){
			p.getInventory().removeItem(this);
			p.getCurrentRoom().getInventory().addItem(this);
			this.holder = null;
			System.out.println("You dropped the "+this.getName().toLowerCase());
		} else {
			System.out.println("You're not holding the "+this.getName().toLowerCase());
		}
	}
	
	/**
	 * A command method to eat an item. If a lucia bun is eaten, the player wins the game.
	 * 
	 * @param p		a reference to player is sent to all commands to refer to the player who used the command
	 */
	@Command
	public void eat(Player p){
		if(p.getContents().contains(this)){
			System.out.println("Eating");
			if(this.getName().equals("lunch") || this.getName().equals("clove of garlic")){
				System.out.println("You eat the " + this.getName() + " but your hunger for lucia buns remain.");
				this.holder = null;
				p.getInventory().removeItem(this);
			} else if(this.getName().toLowerCase().equals("luciabun") || this.getName().equals("lucia bun")){
				System.out.println("You finally taste the incredible lucia bun that you've been hungering for for a long time.\n\n   *** You won the game ****\n\n");
				System.exit(0);
			}
		} else {
			System.out.println("You're not holding the " + this.getName());
		}
	}
	
	
	
}

import java.util.ArrayList;


public class Player extends Object{
	private Inventory contents;
	private Room currentRoom;
	private boolean lampLit;
	
	public Player(String name, String desc){
		super(name, desc);
		this.contents = new Inventory("p_inv","");
	}
	
	public Player(String name, String desc, boolean listable) {
		super(name,desc,listable);
		this.contents = new Inventory("p_inv","");
		lampLit = false;
	}
	
    public Inventory getInventory(){
    	return contents;
    }
    
    public ArrayList<Object> getContents(){
    	return contents.getObjects();
    }

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(Room currentRoom) {
		if(this.currentRoom != null){
			this.currentRoom.getInventory().removeObject(this);
		}
		this.currentRoom = currentRoom;
		this.currentRoom.getInventory().addObject(this);
	}
	
	public boolean hasItem(String name){
		for(Object o : this.getContents()){
			if(o.getName().equals(name)){
				return true;
			}
		}
		return false;
	}

	public boolean isLampLit() {
		return lampLit;
	}

	public void setLampLit(boolean lampLit) {
		this.lampLit = lampLit;
	}
	
	public void die(){
		System.out.println("I'm afraid you are\ndead.\n\n   **** You have died ****\n\nNow let's take a look here... Well, you probably deserve another chance.\nI can't quite fix you up completely, but you can't have everything.");
    	Game game = new Game();
    	game.play();
	}
	
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

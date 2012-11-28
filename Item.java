
public class Item extends Object{
	private Player holder;
	
	public Item(String name, String desc){
		super(name, desc);
	}
	
	public Item(String name, String desc, Player holder){
		super(name,desc);
		this.holder = holder;
	}
	
	public Item(String name, String desc, boolean listable) {
		super(name,desc,listable);
	}

	@Command
	public void take(Player p){
		if(this.holder != p){
			p.getInventory().addItem(this);
			p.getCurrentRoom().getInventory().removeItem(this);
			this.holder = p;
			System.out.println("You took the "+this.getName().toLowerCase());
		} else {
			System.out.println("You're already carrying the "+this.getName().toLowerCase());
		}
	}
	
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
	
	
	
}

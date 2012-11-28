 	import java.util.ArrayList;
import java.util.Arrays;


public class Inventory extends Item{
	private ArrayList<Object> objects;
	private boolean open;
	private String openMessage;
	
	public Inventory(){
		this("","");
	}
	
	public Inventory(String name, String desc){
		super(name, desc);
		objects = new ArrayList<Object>();
		open = true;
	}
	
	public Inventory(String name, String desc, boolean open, String openMessage){
		this(name,desc);
		this.open = open;
		this.openMessage = openMessage;
	}
	
	public void addObject(Object newObject){
		objects.add(newObject);
	}
	
	public void removeObject(Object removedObject){
		objects.remove(removedObject);
	}
	
	public Object removeObject(int index){
		Object removedObject = objects.get(index);
		objects.remove(index);
		return removedObject;
	}
	
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
	
	public String getList(){
		String list;
		list = "There is: ";
		for(Object o : objects){
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
	
	public ArrayList<Object> getObjects(){
		return objects;
	}

	public void addItem(Item item) {
		objects.add(item);
	}

	public void removeItem(Item item) {
		objects.remove(item);
	}
	
	@Command
	public void investigate(Player p){
		if(open){
			System.out.println(("You investigate the "+this.name+". "+this.description + ". " + getList()));
		} else {
			System.out.println(("You investigate the "+this.name+". "+this.description + "."));
		}
	}
	
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


public class Object {
	protected String description;
	protected String name;
	protected boolean listable;
	protected boolean portable;
	protected String id;
	

	public Object(String name, String desc){
		this.description = desc;
		this.name = name;
		this.listable = true;
		this.portable = true;
		this.id = "";
	}
	
	public Object(String name, String desc, boolean listable){
		this(name,desc);
		this.listable = listable;
	}
	
	public Object(boolean portable, String name, String desc){
		this(name,desc);
		this.portable = portable;
	}
	
	public Object(String name, String desc, boolean listable, boolean portable){
		this(name,desc);
		this.listable = listable;
		this.portable = portable;
	}
	
	public Object(String name, String desc, boolean listable, boolean portable, String id){
		this(name,desc,listable,portable);
		this.id = id;
	}

	public Object(){
		
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getDesc(){
		if(this.description.equals("")){
			return "There's nothing special about the "+this.getName().toLowerCase();
		} else {
			return this.description;
		}
	}
	
	public boolean isListable() {
		return listable;
	}

	public void setListable(boolean listable) {
		this.listable = listable;
	}

	public boolean isPortable() {
		return portable;
	}

	public void setPortable(boolean portable) {
		this.portable = portable;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Command
	public void investigate(Player p){
		System.out.println(("You examine the "+this.getName()+". "+this.getDesc()));
	}
	
	@Command
	public void examine(Player p){
		this.investigate(p);
	}
	
	@Command
	public void eat(Player p){
		System.out.println("Eating");
		if(this.getName().equals("lunch") || this.getName().equals("clove of garlic")){
			System.out.println("You eat the " + this.getName() + " but your hunger for lucia buns remain.");
		} else if(this.getName().equals("Luciabun")){
			System.out.println("You finally taste the incredible lucia bun that you've been hungering for for a long time.\n\n   *** You won the game ****\n\n");
			System.exit(0);
		}
	}
	
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
	
	@Command
	public void close(Player p){
		if(this.getId().equals("trapdoor")){
			System.out.println("The door swings shut and closes.");
			p.getCurrentRoom().getExit("down").setLockedMessage("The trap door is closed");
		} else {
			System.out.println("You cannot close the " + this.getName());
		}
	}
	
	@Command
	public void move(Player p){
		if(this.getId().equals("orientalRug")){
			if(p.getCurrentRoom().getExit("down").getLockedMessage().equals("The trap door is closed") || p.getCurrentRoom().getExit("down").getLockedMessage().equals("")){
				System.out.println("Having moved the carpet previously, you find it impossible to move it again.");
			} else {
				System.out.println("With a great effort, the carpet is moved to one side of the room, revealing the dusty cover of a closed trapdoor.");
				p.getCurrentRoom().getInventory().addObject(new Object("trapdoor", "The trap door is closed.",false,false,"trapdoor"));
				p.getCurrentRoom().getExit("down").setLockedMessage("The trap door is closed");
			}
		} else {
			System.out.println("You can't move the " + this.getName().toLowerCase());
		}
	}
	
	@Command
	public void pull(Player p){
		this.move(p);
	}
	
	@Command
	public void push(Player p){
		this.move(p);
	}
	
	@Command
	public void remove(Player p){
		this.move(p);
	}
	
	@Command
	public void use(Player p){
		if(this.getName().equals("lantern")){
			this.light(p);
		}
	}
	
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


public class Door {
	private Room r1;
	private Room r2;
	private String lockedMessage;
	
	public Door(Room r1, Room r2, String dir1, String dir2){
		this.r1 = r1;
		this.r2 = r2;
		r1.addDoor(dir1, this);
		r2.addDoor(dir2, this);
		this.lockedMessage = "";
	}
	
	public Door(Room r1, Room r2, String dir1, String dir2, String lockedMessage){
		this(r1,r2,dir1,dir2);
		this.lockedMessage = lockedMessage;
	}
	
	public Room go(Player p){
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

	public String getLockedMessage() {
		return lockedMessage;
	}

	public void setLockedMessage(String lockedMessage) {
		this.lockedMessage = lockedMessage;
	}
	
	
}



/**
 *  This class is the main class of the "World of KROZ" application. 
 *  "World of KROZ" is a very simple, text based adventure game. Users should
 *  try to find a lucia bun before they starve
 * 
 *  To play this game, run the main method in the Game class.
 * 
 *  This main class creates and initializes all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Erik Ihrén
 * @version 2012.12.01
 */

public class Game {
    private Parser parser;		// A parser object used for parsing user input.
    private Player player;		// A player object used for keeping track of the player.
    public static int moves;	// The total amount of moves since start.
        
    /**
     * Create the game and initialize its internal map and also creates the player.
     */
    public Game() {
    	this.player = new Player("Erik","An ordinary person");
        createRooms();
        parser = new Parser();
        moves = 0;
        
    }
    
    /**
     * Main method. Used to start the program by creating a Game-instance and running play on it.
     */
    public static void main(String[] args){
    	Game game = new Game();
    	game.play();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms() {
        Room w_house,n_house,e_house,s_house,kitchen,attic,livingRoom,cellar,trollRoom,treasureRoom;
      
        // create the rooms
        
        // The null room is used for doors that are never going to be opened.
        Room nullRoom = new Room("");

        // Create other rooms
        GameObject house = new GameObject("House", "The house is a beautiful colonial house which is painted white. It is clear that the owners must have been extremely wealthy",false,false);
        
        w_house = new Room("You are standing in an open field west of a white house, with a boarded front door");
        w_house.getInventory().addObject(new GameObject("door","The door is closed.",false,false));
        w_house.getInventory().addObject(house);
        
       
        n_house = new Room("You are facing the north side of a white house. There is no door here, and all the windows are boarded up.");
        n_house.getInventory().addObject(new GameObject("window","",false,false));
        n_house.getInventory().addObject(house);
        
        new Door(w_house,n_house,"north","west");
        
        e_house = new Room("You are behind the white house. In one corner of the white house there is a small window which is slightly ajar.");
        e_house.getInventory().addObject(new GameObject("window","The window is slightly ajar, but not enough to allow entry. Inside it, you can see what appears to be a kitchen",false,false,"kitchenWindow"));
        e_house.getInventory().addObject(house);
        
        new Door(n_house, e_house,"east","north");
        
        s_house = new Room("You are facing the south side of a white house. There is no door here and all the windows are boarded");
        s_house.getInventory().addObject(new GameObject("window","",false,false));
        s_house.getInventory().addObject(house);
        
        new Door(e_house,s_house,"south","east");
        new Door(w_house,s_house,"south","west");
        
        kitchen = new Room("You are in the kitchen of the white house. A table seems to have been used recently for the preparation of food. \nA passage leads to the west and a dark staircase can be seen leading upward. A dark chimney leads down and to the east is a small window which is open.");
        
        Inventory sack = new Inventory("sack","The brown sack is closed",false, "Opening the brown sack reveals a lunch, and a clove of garlic");
		sack.addItem(new Item("lunch",""));
        Item garlic = new Item("clove of garlic","");
        garlic.addOtherName("garlic");
        sack.addItem(garlic);
        kitchen.getInventory().addObject(sack);
        kitchen.getInventory().addObject(new GameObject("chimney","The chimney leads downward, and looks climbable.",false,false));
        kitchen.getInventory().addObject(new GameObject("window", "The kitchen window is open but I can't tell what's beyond it.",false,false));
        
        
        new Door(kitchen,nullRoom,"down","","Only Santa Claus climbs down chimneys.");
        
        new Door(e_house,kitchen,"inside","out","The kitchen window is closed");
        new Door(e_house,kitchen,"in","out","The kitchen window is closed");
        
        attic = new Room("This is the attic. The only exit is a stairway leading down. A large coil of rope is lying in the corner.\nOn a table is a nasty-looking knife.",true);
        attic.getInventory().addItem(new Item("knife",""));
        attic.getInventory().addItem(new Item("rope",""));
        
        new Door(attic,kitchen,"down","up");
        
        livingRoom = new Room("You are in the living room. There is a doorway to the east, a wooden door with\nstrange gothic lettering to the west, which appears to be nailed shut, a trophy case, and a large oriental carpet in the center of the room.\nAbove the trophy-case hangs an elvish sword of great antiquity. A battery-powered brass lantern is on the trophy case.");
        livingRoom.getInventory().addObject(new GameObject("door","The engravings translate to \"This space intentionally left blank\".",false,false,"engravedDoor"));
        livingRoom.getInventory().addObject(new GameObject("trophy case","The trophy case is empty.",false,false));
        GameObject carpet = new GameObject("carpet","This heavy rug covers most of the room. It seems to be slightly higher in the middle.",false,false, "orientalRug");
        carpet.addOtherName("rug");
        livingRoom.getInventory().addObject(carpet);
        
        livingRoom.getInventory().addItem(new Item("sword",""));
        livingRoom.getInventory().addItem(new Item("lantern","The lamp is turned off."));
        
        new Door(livingRoom,nullRoom,"west","","The door is nailed shut");
        new Door(livingRoom,kitchen,"east","west");
        
        cellar = new Room("You are in a dark and damp cellar with a narrow passageway leading north.\nOn the west is the bottom of a steep metal\nramp which is unclimbable.\nThe trap door crashes shut, and you hear someone barring it.",true,"Cellar");
        GameObject trapdoor = new GameObject("trapdoor", "The trap door is closed.",false,false,"trapdoor");
        trapdoor.addOtherName("trap door");
        cellar.getInventory().addObject(trapdoor);
        
        new Door(livingRoom,cellar,"down","up","There is no exit in that direction!");
        new Door(cellar,nullRoom,"west","","You try to ascend the ramp, but it is impossible, and you slide back down.");
        
        trollRoom = new Room("This is a small room with a passage to the east. Bloodstains and deep scratches \n(perhaps made by an axe) mar the walls.\nA nasty-looking troll, brandishing a bloddy axe, blocks all passages out\nof the room.\n\nYour sword has begun to glow very brightly",true,"Troll room");
        trollRoom.getInventory().addObject(new Player("Troll", "A nasty-looking troll, brandishing a bloody axe, blocks the passage to the east."));
        
        new Door(cellar,trollRoom,"north","south");
        
        treasureRoom = new Room("You are in a extraordinary treasure room. It's filled to the brim with lucia buns. There's a passage to the west.");
        treasureRoom.getInventory().addObject(new Item("Lucia bun","Amazing pastry"));
        
        new Door(treasureRoom, trollRoom,"west","east","The troll fends you off with a menacing gesture.");
        
        player.setCurrentRoom(w_house); // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play(){            
        printWelcome();
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        boolean finished = false;
        boolean attacked = false;
        while (!finished) {
        	
        	// The troll starts to attack
        	for(GameObject o : player.getCurrentRoom().getContents()){
            	if(o.getName().equals("Troll")){
            		attacked = true;
            	}
            }
            finished = parser.getCommand(player);
            
            // If the troll is still alive in the room, kill the player
            for(GameObject o : player.getCurrentRoom().getContents()){
            	if(o.getName().equals("Troll") && attacked){
            		player.die();
            	}
            }
            attacked = false;
            System.out.println("Moves: " + Game.moves+"\n");
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome() {
        System.out.println("\nWelcome to the World of KROZ!\n\nYou wake up suddenly in an unknown place. You don't know where you are but you feel a great need for luciabuns. \nThe lack of luciabuns is so painful that you feel you might not be able to take it anymore. Find and eat a luciabun before the lack of luciabuns kills you.\n");
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

}

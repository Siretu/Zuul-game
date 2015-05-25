import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is part of the "World of KROZ" application. 
 * "World of KROZ" is a very simple, text based adventure game. Users should
 *  try to find a lucia bun before they starve
 *  
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line by going finding a target, as specified by the user,
 * in the current room. It then tries to find the issued command as a class method
 * in the target it found.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands if it can't find a target or a method-command in the target's
 * class.
 * 
 * @version 2012.12.01
 */

public class Parser 
{
    private Scanner reader;         // source of command input

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser() 
    {
        reader = new Scanner(System.in);
    }

    /**
     * Gets a command from the player. This method handles the input of the command
     * and sends it on to the parse method for parsing.
     * 
     * @param player the player that enters the command.
     * @return Returns true if the user wants to quit. Otherwise it returns false.
     */
    public boolean getCommand(Player player){
        String inputLine;   // will hold the full input line

        System.out.print("> ");     // print prompt

        inputLine = reader.nextLine();
        return parse(inputLine,player);
    }
    
    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     *
     * @param p			the player who wants to move
     * @param command	the string command containing the direction the player wants to move.
     */
    private void goRoom(Player p, String command){
    	// Try to leave current room.
    	String direction = "";
    	if(command.equals("n")){
    		direction = "north";
    	} else if(command.equals("w")){
    		direction = "west";
    	} else if(command.equals("e")){
    		direction = "east";
    	} else if(command.equals("s")){
    		direction = "south";
    	} else if(command.equals("d")){
    		direction = "down";
    	} else if(command.equals("u")){
    		direction = "up";
    	} else {
    		direction = command;
    	}
    	
    	Door nextDoor = p.getCurrentRoom().getExit(direction);
    	
        if (nextDoor == null) {
            System.out.println("There is no exit in that direction!");
        }
        else {
        	Room nextRoom = nextDoor.getConnectedRoom(p);
        	if(nextRoom != null){ // If the door is not locked
        		Game.moves++;
        		p.setCurrentRoom(nextRoom);
        		System.out.println(p.getCurrentRoom().getLongDescription());
        		if(p.getCurrentRoom().getName().equals("Cellar")){
        			p.getCurrentRoom().getExit("up").setLockedMessage("The trapdoor is closed");
        			p.getCurrentRoom().setDescription("You are in a dark and damp cellar with a narrow passageway leading north.\nOn the west is the bottom of a steep metal\nramp which is unclimbable.");
        			if(p.hasItem("sword")){
        				System.out.println("Your sword is glowing with a faint blue glow.");
        			}
        			
        		}
        		// Display time limit messages
        		if(Game.moves == 10){
        			System.out.println("The hunger is worse than anything you've experienced before. Find a luciabun, quickly!");
        		} else if(Game.moves == 15){
        			System.out.println("Your body is growing weaker due to the lack of luciabuns. You'll die very soon if you don't eat a luciabun");
        		} else if(Game.moves >= 18){
        			System.out.println("You finally give up on life. Your body couldn't survive that long without luciabuns");
        			p.die();
        		}
        	} else { // If the door is locked, print the locked message.
        		System.out.println(p.getCurrentRoom().getExit(direction).getLockedMessage());
        	}
        }
    	
    }
    
    /**
     * This parser reads user input and tries to interpret it as an "Adventure"
     * command. Every time it is called it reads a line from the terminal and
     * tries to interpret the line by going finding a target, as specified by the user,
     * in the current room. It then tries to find the issued command as a class method
     * in the target it found.
     * 
     * @param command	the string entered in the parser
     * @param p			the player to use the command for
     * @return	true if the player wants to stop playing. Otherwise it returns false.
     */
    public boolean parse(String command, Player p){
    	boolean finished = false;
    	
    	// Initial contents of the room. Might be expanded on later on if we find a player or an inventory
    	ArrayList<GameObject> contents = new ArrayList<GameObject>(p.getCurrentRoom().getContents());
    	String name;
    	String methodName;
    	Method finalMethod = null;
    	command = command.toLowerCase();
    	String[] commandWords = command.split(" ");
    	
    	// Special fix for break/destroy since I can't make a break command since break is a protected keyword in java.
    	if(commandWords[0].equals("break")){
    		commandWords[0] = "destroy";
    	}
    	if(commandWords.length > 1){ // Multiple words in command
	    	// Go through all the stuff in the room to find a target for our command
    		int index = 0;
    		GameObject o;
    		boolean foundTarget = false;
    		boolean executed = true;
    		int bestMatchLength = 0;
    		GameObject bestMatch = null;
    		
	    	while(index < contents.size()){
	    		o = contents.get(index);
	    		index++;
	    		if(o.getClass() == Player.class){
	    			for(GameObject content : ((Player) o).getContents()){
	    				contents.add(content);
	    			}
	    		} else if(o.getClass() == Inventory.class && ((Inventory) o ).isOpen()){
	    			for(GameObject content : ((Inventory) o).getObjects()){
	    				contents.add(content);
	    			}
	    		}
	    		name = o.getName().toLowerCase();
	    		
	    		// Search other names
	    		for(String otherName : o.getOtherNames()){
	    			if(command.endsWith(otherName)){
	    				name = otherName;
	    				break;
	    			}
	    		}
	    		if(command.endsWith(name) && name.length() > bestMatchLength){ // the command targets our current object
	    			foundTarget = true;
		    		Class myClass = o.getClass();
		    		
		    		// Go through all methods in the class
		    		for(Method m : myClass.getMethods()){
		    			if(m.isAnnotationPresent(Command.class)){ // You can only call methods with the @Command annotation
			    			// Modify method name to remove packages and brackets to get the pure name.
			    			methodName = m.toString();
			    			String[] method_split = methodName.split("\\."); // Split in packages
			    			if(method_split.length > 0){
				    			methodName = method_split[method_split.length-1].toLowerCase(); // Get the last one
				    			method_split = methodName.split("\\(");
				    			methodName = method_split[0];
				    			if(methodName.equals(commandWords[0])){ // If we found a command that is the one the player specified in the first word, save it.
				    				finalMethod = m;
				    				bestMatchLength = name.length(); // Save the length of the name. A longer name match is most likely better. (eg. prefer "wooden door" to "door")
				    				bestMatch = o;
				    			}
			    			}
		    			}
		    		}
	    		}
	    	}
    		if(finalMethod != null){
    			try {
					finalMethod.invoke(bestMatch,p); // Run the command
				} catch (IllegalArgumentException e) {
					System.out.println("Internal error when Reflection tried to invoke a command on the target.");
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					System.out.println("Reflection tried to access something private.");
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					System.out.println("The invoked command gave an error.");
					e.printStackTrace();
				}
    		} else if(!foundTarget) {
	    		if(commandWords[0].equals("go")){
	    			goRoom(p,commandWords[1]);
	    		} else {
	    			System.out.println("I'm sorry but I can't '" + command + "'");
	    		}
    		} else { // Can't use that command on that target
				System.out.println("I'm sorry but I can't '" + commandWords[0] + "' the " + commandWords[1]);
			}
    
    	} else { // Handle special cases with one-word commands
    		if(command.equals("inventory") || command.equals("i")){
    			System.out.println(p.getInventory().getList());
    		} else if(command.equals("list") || command.equals("look")){
    			System.out.println(p.getCurrentRoom().getLongDescription());
    		} else if(command.equals("n") || command.equals("north") || command.equals("s") || command.equals("south") || command.equals("w") || command.equals("west") || command.equals("e") || command.equals("east") || command.equals("u") || command.equals("up") || command.equals("d") || command.equals("down")){
    			goRoom(p, command);
    		} else if(command.equals("help")){ 
    			System.out.println("You need to find a lucia bun before the time runs out. \n\nYou can go in a direction (for example south) by typing \"go south\", \"south\" or just \"s\". \nTo go to some special places you might need to specify \"go\" first (for example \"go through\").\n\nThere are a lot of intuitive commands available for you to figure out but the most common ones are:\nhelp, examine, look, south, east, west, north, inventory");
    		}else if(command.equals("quit")){
    			finished = true;
    		} else {
    			System.out.println("Huh?");
    		}
    	}
    	return finished;
    }
    
}

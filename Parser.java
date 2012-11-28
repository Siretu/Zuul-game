import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This class is part of the "Cruiser Command" application. 
 * "Cruiser Command" is a very simple, text based adventure game. Users should
 *  try to escape the spaceship before it is destroyed.
 *  
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two-word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.10
 */
public class Parser 
{
    //private CommandWords commands;  // holds all valid command words
    private Scanner reader;         // source of command input

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser() 
    {
        //commands = new CommandWords();
        reader = new Scanner(System.in);
    }

    /**
     * @return The next command from the user.
     */
    public boolean getCommand(Room current, Player player){
        String inputLine;   // will hold the full input line
        String word1 = null;
        String word2 = null;

        System.out.print("> ");     // print prompt

        inputLine = reader.nextLine();
        return parse(inputLine,player);
    }
    
    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
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
        	Room nextRoom = nextDoor.go(p);
        	if(nextRoom != null){
        		Game.moves++;
        		p.setCurrentRoom(nextRoom);
        		System.out.println(p.getCurrentRoom().getLongDescription());
        		System.out.println(p.getCurrentRoom().getGoString());
        		if(p.getCurrentRoom().getName().equals("Cellar")){
        			p.getCurrentRoom().getExit("up").setLockedMessage("The trapdoor is closed");
        			if(p.hasItem("sword")){
        				System.out.println("Your sword is glowing with a faint blue glow.");
        			}
        		}
        		if(Game.moves == 10){
        			System.out.println("The hunger is worse than anything you've experienced before. Find a luciabun, quickly!");
        		} else if(Game.moves == 15){
        			System.out.println("Your body is growing weaker due to the lack of luciabuns. You'll die very soon if you don't eat a luciabun");
        		} else if(Game.moves >= 18){
        			System.out.println("You finally give up on life. Your body couldn't survive that long without luciabuns");
        			p.die();
        		}
        	} else {
        		System.out.println(p.getCurrentRoom().getExit(direction).getLockedMessage());
        	}
        }
    	
    }
    
    public boolean parse(String command, Player p){
    	boolean finished = false;
    	ArrayList<Object> contents = new ArrayList<Object>(p.getCurrentRoom().getContents());
    	String name;
    	String methodName;
    	String finalMethodName = "";
    	Method finalMethod = null;
    	command = command.toLowerCase();
    	String[] commandWords = command.split(" ");
    	if(commandWords.length > 1){ // Multiple words in command
	    	// Go through all the stuff in the room to find a target for our command
    		int index = 0;
    		Object o;
    		boolean foundTarget = false;
	    	while(index < contents.size()){
	    		o = contents.get(index);
	    		index++;
	    		if(o.getClass() == Player.class){
	    			for(Object content : ((Player) o).getContents()){
	    				contents.add(content);
	    			}
	    		} else if(o.getClass() == Inventory.class){
	    			for(Object content : ((Inventory) o).getObjects()){
	    				contents.add(content);
	    			}
	    		}
	    		name = o.getName().toLowerCase();
	    		System.out.println(commandWords[1] + " == " + name);
	    		if(commandWords[1].equals(name)){
	    			System.out.println("Found target");
	    			foundTarget = true;
		    		Class myClass = o.getClass();
		    		for(Method m : myClass.getMethods()){
		    			if(m.isAnnotationPresent(Command.class)){ // You can only call methods with the @Command annotation
			    			// Modify method name to remove packages and brackets to get the pure name.
			    			methodName = m.toString();
			    			String[] method_split = methodName.split("\\."); // Split in packages
			    			if(method_split.length > 0){
				    			methodName = method_split[method_split.length-1].toLowerCase(); // Get the last one
				    			method_split = methodName.split("\\(");
				    			methodName = method_split[0];
				    			if(methodName.equals(commandWords[0])){
				    				finalMethod = m;
				    			}
			    			}
		    			} else {
		    				//System.out.println(m.toString() + " is not a command.");
		    			}
		    			
		    		}
		    		if(finalMethod != null){
		    			try {
							finalMethod.invoke(o,p);
						} catch (IllegalArgumentException e) {
							System.out.println("Wrong arguments");
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							System.out.println("What do you think you're trying to do punk? You don't have the rights!");
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							System.out.println("If you see this, it means something went wrong inside.");
							e.printStackTrace();
						}
		    		} else { // Can't use that command on that target
	    				System.out.println("I'm sorry but I can't '" + commandWords[0] + "' the " + name);
	    			}
		    		break;
	    		}
	    	}
	    	if(!foundTarget){ // Handle special two-word commands that don't target an object.
	    		if(commandWords[0].equals("go")){
	    			goRoom(p,commandWords[1]);
	    		}
	    		
	    	}
    	} else { // Handle special cases with one-word commands
    		if(command.equals("inventory") || command.equals("i")){
    			System.out.println(p.getInventory().getList());
    		} else if(command.equals("list") || command.equals("look")){
    			System.out.println(p.getCurrentRoom().getLongDescription());
    		} else if(command.equals("n") || command.equals("north") || command.equals("s") || command.equals("south") || command.equals("w") || command.equals("west") || command.equals("e") || command.equals("east") || command.equals("u") || command.equals("up") || command.equals("d") || command.equals("down")){
    			goRoom(p, command);
    		} else if(command.equals("quit")){
    			finished = true;
    		} else {
    			System.out.println("I beg you pardon?");
    		}
    	}
    	return finished;
    }
    
    
    /**
     * Print out a list of valid command words.
     */
//    public void showCommands()
//    {
//        commands.showAll();
//    }
}

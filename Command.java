import java.lang.annotation.*;

/**
 * Class Command - a custom annotation.
 *
 * This class is part of the "World of KROZ" application. 
 * "World of KROZ" is a very simple, text based adventure game. Users should
 *  try to find a lucia bun before they starve
 *  
 * The Command class annotation is used to differ the command methods from the normal methods. 
 * This is so that the player can't run internal commands like "getName carpet"
 * 
 * @author  Erik Ihrén
 * @version 2012.12.01
 */

@Retention(RetentionPolicy.RUNTIME)


public @interface Command {

}

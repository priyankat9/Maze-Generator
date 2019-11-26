package byog.Core;

import byog.TileEngine.TETile;

//import java.io.IOException;
import java.io.Serializable;

/**
 * This is the main entry point for the program. This class simply parses
 * the command line inputs, and lets the byog.Core.Game class take over
 * in either keyboard or input string mode.
 */


public class Main implements Serializable {


    public static void main(String[] args) {
        Game myGame = new Game();
        //TETile[][] finalWorld = myGame.playWithInputString("N999WS");
        myGame.playWithKeyboard();
        //myGame.ter.renderFrame(finalWorld); // draws finalWorld
    }

}

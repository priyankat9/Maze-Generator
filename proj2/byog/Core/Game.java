package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

//import jdk.jshell.spi.ExecutionControl;


public class Game implements Serializable {

    /* Feel free to change the width and height. */
    public static final int WIDTH = 90;
    public static final int HEIGHT = 45;
    private static final int TILE_SIZE = 16;

    protected transient TERenderer ter;
    protected TETile[][] world;
    protected Random r;
    protected ArrayList<RoomRectangle> rooms = new ArrayList<>();
    protected Character josh;
    protected Character hug;
    protected String filename = "file.txt";


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        drawMainMenu();
        for (int i = 0; i < 1; ) {
            if (StdDraw.hasNextKeyTyped()) {
                takeInput(StdDraw.nextKeyTyped());
                i++;
            }
        }

    }


    public void drawMainMenu() {
        StdDraw.setCanvasSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font gameFont = new Font("SansSerif", Font.BOLD, 30);
        StdDraw.setFont(gameFont);
        StdDraw.text(0.5, 0.60, "CS61B: THE GAME");
        Font menuFont = new Font("SansSerif", Font.PLAIN, 20);
        StdDraw.setFont(menuFont);
        StdDraw.text(0.5, 0.5, "New Game (N)");
        StdDraw.text(0.5, 0.47, "Load Game (L)");
        StdDraw.text(0.5, 0.44, "Quit (Q)");
        StdDraw.show();
    }

    private void takeInput(char letter) {
        if (letter == 'n' || letter == 'N') {
            StdDraw.clear(Color.BLACK);
            StdDraw.setPenColor(Color.WHITE);
            Font gameFont = new Font("SansSerif", Font.BOLD, 30);
            StdDraw.setFont(gameFont);
            StdDraw.text(0.5, 0.60, "Please enter seed number");
            Font menuFont = new Font("SansSerif", Font.PLAIN, 20);
            StdDraw.setFont(menuFont);
            StdDraw.text(0.5, 0.5, "Press 'S' when done");
            StdDraw.show();
            String seed = generateSeed();
            playWithInputStrings(seed);
        } else if (letter == 'l' || letter == 'L') {
            playWithInputStrings("L");
        } else if (letter == 'q' || letter == 'Q') {
            System.exit(0);
        } else {
            return;
        }
    }

    private String generateSeed() {
        String seed = "N";
        long num = 0;
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char n = StdDraw.nextKeyTyped();
                if (n == 'S' || n == 's') {
                    return seed + num + n;
                } else {
                    int i = (int) n;
                    num = 10 * num + i;
                }
            }
        }
    }

    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        boolean validInput = true;
        char firstLetter = input.toUpperCase().charAt(0);
        boolean quit = false;

        if (firstLetter == 'L') {
            // loads the last game
            Game g = (Game) loadGame();
            setGame(g);
            boolean colon = false;
            for (int index = 1; index < input.length(); index++) {
                char letter = input.toUpperCase().charAt(index);
                if (letter == ':') {
                    colon = true;
                } else if (letter == 'Q' || letter == 'q' && colon) {
                    saveGame((Object) this);
                } else {
                    act(letter);
                }
            }
            ter = new TERenderer();
            ter.initialize(WIDTH, HEIGHT + 2, 0, 2);
            ter.renderFrame(world);

        } else if (firstLetter == 'N') {
            //int sIndex = input.toUpperCase().indexOf('S');
            //long code = Long.parseLong(input.substring(1, sIndex));
            long code = Long.parseLong(input.replaceAll("[^0-9]", ""));
            //System.out.println(code);
            buildWalls();
            createWorld(code);
            boolean colon = false;
            for (int index = 1; index < input.length(); index++) {
                char letter = input.toUpperCase().charAt(index);
                //System.out.println(letter);
                if (letter == ':') {
                    colon = true;
                } else if (letter == 'Q' || letter == 'q' && colon) {
                    saveGame((Object) this);
                } else {
                    act(letter);
                }
            }
        } else {
            validInput = false;
        }

        if (validInput) {
            playGame();
            return world;
        }
        return null;
    }


    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputStrings(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        boolean validInput = true;
        char firstLetter = input.toUpperCase().charAt(0);
        boolean quit = false;

        if (firstLetter == 'L') {
            // loads the last game
            Game g = (Game) loadGame();
            setGame(g);
            boolean colon = false;
            for (int index = 1; index < input.length(); index++) {
                char letter = input.toUpperCase().charAt(index);
                if (letter == ':') {
                    colon = true;
                } else if (letter == 'Q' && colon) {
                    quitGame();
                } else {
                    act(letter);
                }
            }

            ter = new TERenderer();
            ter.initialize(WIDTH, HEIGHT + 2, 0, 2);
            ter.renderFrame(world); //prob gonna have to comment this out


        } else if (firstLetter == 'N') {
            int sIndex = input.toUpperCase().indexOf('S');
            long code = Long.parseLong(input.substring(1, sIndex));
            buildWalls();
            createWorld(code);
            boolean colon = false;
            for (int index = sIndex + 1; index < input.length(); index++) {
                char letter = input.toUpperCase().charAt(index);
                if (letter == ':') {
                    colon = true;
                } else if (letter == 'Q' && colon) {
                    quitGame();
                } else {
                    act(letter);
                }
            }
        } else {
            validInput = false;
        }
        if (validInput) {
            playGame();
            return world;
        }
        return null;
    }


    private void buildWalls() {
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT + 2, 0, 2);

        // initialize tiles
        world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.WALL;
            }
        }
    }

    private void createWorld(long code) {
        r = new Random(code);
        int direction = 1;

        if (code % 2 == 0) {
            direction = 0;
        }
        Rectangle rec = new Rectangle(WIDTH, HEIGHT, new Coordinate(0, 0), direction);
        BinaryTree t = new BinaryTree(rec);


        recursiveRectangles(t);
        buildHallway();

        connectRooms();
        createOutside();

        createCharacter();
        createWater();

        ter.renderFrame(world);
    }

    private void recursiveRectangles(BinaryTree t) {
        /**
         * direction is a variable that determines which way to cut
         * horizontal: 0, vertical: 1
         * PASS IN RECTANGLE
         */
        if (t.myValue().width <= 8 || t.myValue().length <= 8 || t.myValue().area <= 49) {
            // play with these values
            // base case
            RoomRectangle room = t.myValue().buildRoom(world, r);
            rooms.add(room);
        } else {
            t.createChildren(r);
            recursiveRectangles(t.getBrother());
            recursiveRectangles(t.getSister());

        }

    }

    private void buildHallway() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (x == WIDTH - 2 || x == 1 || y == HEIGHT - 2 || y == 1) {
                    if (x != 0 && y != 0 && x != WIDTH - 1 && y != HEIGHT - 1) {
                        world[x][y] = Tileset.FLOOR;
                    }
                }
            }
        }
    }

    private void connectRooms() {
        for (int i = 0; i < rooms.size(); i += 1) {
            RoomRectangle room = rooms.get(i);
            room.layHall(world, r);
        }
    }

    private void createOutside() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (x == WIDTH - 1 || x == 0 || y == HEIGHT - 1 || y == 0) {
                    world[x][y] = Tileset.WALL;
                }
            }
        }

        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (isOutside(x, y)) {
                    world[x][y] = Tileset.TREE;
                }
            }
        }
    }

    private boolean isOutside(int x, int y) {
        for (int i = -1; i <= 1; i += 1) {
            for (int j = -1; j <= 1; j += 1) {
                try {
                    if (world[x + i][y + j] == Tileset.FLOOR) {
                        return false;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    continue;
                }
            }
        }
        return true;
    }

    private void createCharacter() {
        // do something where asks for name?
        //josh = new Character(world, r);
        //josh.moveCharacter(new Coordinate(1, 1));
        // josh.moveCharacter(josh.getMyLocation());
        josh = new Character(world, r, Tileset.FLOWER);
        hug = new Character(world, r, Tileset.SAND);
        //josh.placeCharacter(new Coordinate(1, 1));
        //hug.placeCharacter(new Coordinate(WIDTH - 2, HEIGHT - 2));
        josh.placeCharacter(josh.getMyLocation());
        hug.placeCharacter(hug.getMyLocation());

    }

    private void createWater() {
        int x = r.nextInt(Game.WIDTH / 2);
        int y = r.nextInt(Game.HEIGHT / 2);
        while (!world[x][y].equals(Tileset.FLOOR)) {
            x = r.nextInt(Game.WIDTH / 2);
            y = r.nextInt(Game.HEIGHT / 2);
        }
        world[x][y] = Tileset.WATER;
    }

    private void playGame() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char letter = StdDraw.nextKeyTyped();
                boolean quit = !act(letter);
                if (quit) {
                    quitGame();
                }
            }
            int x = (int) StdDraw.mouseX();
            int y = (int) StdDraw.mouseY();
            if ((x > 0) && (x < WIDTH) && (y >= 2) && (y < HEIGHT)) {
                writeLocation(world[x][y - 2].description(), x, y - 2);
            }
            /***
            if (StdDraw.isMousePressed()) {
                int x = (int) StdDraw.mouseX();
                int y = (int) StdDraw.mouseY();
                if (y >= 2) {
                    writeLocation(world[x][y - 2].description(), x, y - 2);
                }
            }
             */

        }
    }

    private boolean act(char c) {
        int joshX = josh.getMyLocation().getX();
        int joshY = josh.getMyLocation().getY();
        int hugX = hug.getMyLocation().getX();
        int hugY = hug.getMyLocation().getY();
        if (c == ':') {
            char next = handleNext();
            if (next == 'Q' || next == 'q') {
                return false;
            } else {
                act(next);
            }
        } else if (c == 'w' || c == 'W') {
            joshY += 1;
        } else if (c == 'a' || c == 'A') {
            joshX -= 1;
        } else if (c == 'S' || c == 's') {
            joshY -= 1;
        } else if (c == 'd' || c == 'D') {
            joshX += 1;
        } else if (c == 'u' || c == 'U') {
            hugY += 1;
        } else if (c == 'h' || c == 'H') {
            hugX -= 1;
        } else if (c == 'j' || c == 'J') {
            hugY -= 1;
        } else if (c == 'k' || c == 'K') {
            hugX += 1;
        }
        Coordinate joshUpdatedLoc = new Coordinate(joshX, joshY);
        Coordinate hugUpdatedLoc = new Coordinate(hugX, hugY);
        josh.moveCharacter(joshUpdatedLoc);
        hug.moveCharacter(hugUpdatedLoc);
        ter.renderFrame(world);
        return true;
    }

    private char handleNext() {
        char next = 0;
        for (int i = 0; i < 1; ) {
            if (StdDraw.hasNextKeyTyped()) {
                next = StdDraw.nextKeyTyped();
                i++;
            }
        }
        return next;
    }

    private void writeLocation(String s, int x, int y) {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledRectangle(WIDTH / 2, 1, WIDTH / 2, 1);
        StdDraw.setPenColor(Color.WHITE);
        if (s.equals("floor")) {
            StdDraw.textLeft(1, 1, "this is floor. you can walk there. go forth!");
        }
        if (s.equals("wall")) {
            StdDraw.textLeft(1, 1, "this is wall. you can't walk there. don't go forth!");
        }
        if (s.equals("tree")) {
            StdDraw.textLeft(1, 1, "this is tree. josh and hug want to cut them down one day"
                    + " and replace the tree with a statue of them cutting down the tree. ");
        }
        if (s.equals("water")) {
            StdDraw.textLeft(1, 1, "this is water. "
                    + "may the thirstiest player win. (jk don't be gross)");
        }
        if (s.equals("flower")) {
            StdDraw.textLeft(1, 1, "this is josh! his skills include "
                    + "wrestling bears of the bronx zoo // score: " + josh.score);
        }
        if (s.equals("sand")) {
            StdDraw.textLeft(1, 1, "this is hug! his skills include dance dance revolution // "
                    + "score: " + hug.score);
        }

        StdDraw.show();

    }

    private void quitGame() {
        saveGame((Object) this);
        System.exit(0);
    }

    private void saveGame(Object g) {

        try {
            //Saving of object in a file??
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream output = new ObjectOutputStream(file);

            //Serialization of object
            output.writeObject(g);
            output.close();
            file.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("IOException for s is caught");
            throw new RuntimeException(ex.getMessage());

        }

    }

    private Object loadGame() {
        try {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            Object loaded = in.readObject(); //this errors

            in.close();
            file.close();
            return loaded;

        } catch (IOException ex) {
            System.out.println("IOException for ds is caught");
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
        return null;


    }

    private void setGame(Game g) {
        this.world = g.world;
        this.josh = g.josh;
        this.hug = g.hug;
        this.r = g.r;
        //this.ter = g.ter;
        this.rooms = g.rooms;
    }


}

package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

public class Character implements Serializable {

    protected String name;
    protected TETile[][] world;
    int score;
    Random r;
    private Coordinate myLocation;
    private TETile image;

    public Character(TETile[][] world, Random r, TETile i) {
        name = "josh.josh.hug.hug";
        //myLocation = new Coordinate(1, 1);
        this.world = world;
        this.r = r;
        myLocation = chooseLocation();
        score = 0;
        image = i;

    }

    public Character(TETile[][] world, String n) {
        name = n;
        myLocation = new Coordinate(1, 1);
        this.world = world;
    }

    public Coordinate chooseLocation() {
        int x = r.nextInt(Game.WIDTH / 2);
        int y = r.nextInt(Game.HEIGHT / 2);
        while (!world[x][y].equals(Tileset.FLOOR)) {
            x = r.nextInt(Game.WIDTH / 2);
            y = r.nextInt(Game.HEIGHT / 2);
        }
        return new Coordinate(x, y);
    }

    public void moveCharacter(Coordinate c) {
        if (canMove(c)) {
            world[myLocation.getX()][myLocation.getY()] = Tileset.FLOOR;
            myLocation = c;
            world[c.getX()][c.getY()] = image;
        } else {
            implementScore(c);
        }
    }

    private void implementScore(Coordinate c) {
        if (world[c.getX()][c.getY()].equals(Tileset.WATER)) {
            world[myLocation.getX()][myLocation.getY()] = Tileset.FLOOR;
            myLocation = c;
            world[c.getX()][c.getY()] = image;
            this.score += 1;
            placeWater();
        }
    }

    public boolean canMove(Coordinate c) {
        return world[c.getX()][c.getY()].equals(Tileset.FLOOR);
    }


    public Coordinate getMyLocation() {
        return myLocation;
    }

    public void placeCharacter(Coordinate c) {
        if (canMove(c)) {
            myLocation = c;
            world[c.getX()][c.getY()] = image;
        }
    }

    public void placeWater() {
        int x = r.nextInt(Game.WIDTH - 5);
        int y = r.nextInt(Game.HEIGHT - 5);
        while (!world[x][y].equals(Tileset.FLOOR)) {
            x = r.nextInt(Game.WIDTH -5);
            y = r.nextInt(Game.HEIGHT - 5);
        }
        world[x][y] = Tileset.WATER;
    }
}

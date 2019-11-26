package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.Random;

public class RoomRectangle extends Rectangle implements Serializable {

    public RoomRectangle(int w, int l, Coordinate c, int d) {
        super(w, l, c, d);
    }

    public void layFloor(TETile[][] world) {
        for (int x = location.getX(); x < location.getX() + width; x += 1) {
            for (int y = location.getY(); y < location.getY() + length; y += 1) {
                world[x][y] = Tileset.FLOOR;
            }
        }
    }

    public void layHall(TETile[][] world, Random r) {

        int side = r.nextInt(4) + 1;
        if (side == 1) { // moving left
            int currentY = location.getY() + length / 2;
            int currentX = location.getX() - 1;
            boolean hitEnd = false;
            while (!hitEnd && currentY > 0 && currentX > 0
                    && currentY < Game.HEIGHT && currentX < Game.WIDTH) {
                if (world[currentX][currentY] != Tileset.FLOOR) {
                    world[currentX][currentY] = Tileset.FLOOR;
                    currentX -= 1;
                } else {
                    hitEnd = true;
                }
            }
        } else if (side == 2) { // moving right
            int currentY = location.getY() + length / 2;
            int currentX = location.getX() + width;
            boolean hitEnd = false;
            while (!hitEnd && currentY > 0 && currentX > 0
                    && currentY < Game.HEIGHT && currentX < Game.WIDTH) {
                if (world[currentX][currentY] != Tileset.FLOOR) {
                    world[currentX][currentY] = Tileset.FLOOR;
                    currentX += 1;
                } else {
                    hitEnd = true;
                }
            }
        } else if (side == 3) { //moving down
            int currentX = location.getX() + width / 2;
            int currentY = location.getY() - 1;
            boolean hitEnd = false;
            while (!hitEnd && currentY > 0 && currentX > 0
                    && currentY < Game.HEIGHT && currentX < Game.WIDTH) {
                if (world[currentX][currentY] != Tileset.FLOOR) {
                    world[currentX][currentY] = Tileset.FLOOR;
                    currentY -= 1;
                } else {
                    hitEnd = true;
                }
            }
        } else if (side == 4) { //moving up
            int currentX = location.getX() + width / 2;
            int currentY = location.getY() + length;
            boolean hitEnd = false;
            while (!hitEnd && currentY > 0 && currentX > 0
                    && currentY < Game.HEIGHT && currentX < Game.WIDTH) {
                if (world[currentX][currentY] != Tileset.FLOOR) {
                    world[currentX][currentY] = Tileset.FLOOR;
                    currentY += 1;
                } else {
                    hitEnd = true;
                }
            }
        }

    }
}

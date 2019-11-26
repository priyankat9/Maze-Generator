package byog.Core;

import byog.TileEngine.TETile;

import java.io.Serializable;
import java.util.Random;

public class Rectangle implements Serializable {

    int width;
    int length;
    int direction;
    Coordinate location;
    int area;
    RoomRectangle room;

    public Rectangle(int w, int l, Coordinate c, int d) {
        width = w;
        length = l;
        location = c;
        area = width * length;
        room = null;
        direction = d;
    }

    public Coordinate getLocation() {
        return location;
    }

    public RoomRectangle buildRoom(TETile[][] world, Random r) {
        // w and l has to be AT LEAST four
        //just for my own visualization
        int w = 0;
        int l = 0;
        if (width > 5) {
            w = r.nextInt(width - 5) + 4;
        }
        if (length > 5) {
            l = r.nextInt(length - 5) + 4;
        }
        // int w = width - 1;
        //int l = length - 1;
        // Coordinate loc = new Coordinate(location.getX() + 1, location.getY() + 1);
        room = new RoomRectangle(w, l, location, direction);
        room.layFloor(world);
        // layBorder(world);
        return room;
    }

}

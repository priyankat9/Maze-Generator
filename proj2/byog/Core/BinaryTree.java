package byog.Core;

import java.io.Serializable;
import java.util.Random;

public class BinaryTree implements Serializable {
    private Rectangle value;
    private BinaryTree sister;
    private BinaryTree brother;
    private BinaryTree parent;


    public BinaryTree(Rectangle value) {
        this.value = value;
        this.parent = null;
        sister = null;
        brother = null;


    }

    public BinaryTree(Rectangle value, BinaryTree parent) {
        this.value = value;
        this.parent = parent;
        sister = null;
        brother = null;
    }

    public Rectangle myValue() {
        return value;
    }

    public BinaryTree getSister() {
        return sister;
    }

    public BinaryTree getBrother() {
        return brother;
    }


    public void createChildren(Random r) {
        int direction = myValue().direction;
        Coordinate myLocation = value.getLocation();
        if (direction == 0) {
            int w = myValue().width; // split horizontally
            int l = r.nextInt((int) (myValue().length * 0.6)) + (int) (myValue().length * 0.2);
            int otherL = myValue().length - l;
            Coordinate sisterPlacement = new Coordinate(myLocation.getX(), myLocation.getY());
            Coordinate brotherPlacement = new Coordinate(myLocation.getX(), myLocation.getY() + l);
            sister = new BinaryTree(new Rectangle(w, l, sisterPlacement, 1), this);
            brother = new BinaryTree(new Rectangle(w, otherL, brotherPlacement, 1), this);

        } else {
            int l = myValue().length; // split vertically
            int w = r.nextInt((int) (myValue().width * 0.6)) + (int) (myValue().length * 0.2);
            int otherW = myValue().width - w;
            Coordinate sisterPlacement = new Coordinate(myLocation.getX(), myLocation.getY());
            Coordinate brotherPlacement = new Coordinate(myLocation.getX() + w, myLocation.getY());
            sister = new BinaryTree(new Rectangle(w, l, sisterPlacement, 0), this);
            brother = new BinaryTree(new Rectangle(otherW, l, brotherPlacement, 0), this);

        }

    }

}



package labs.lab1ex2;

import java.util.*;

class ObjectCanNotBeMovedException extends Exception {
    public ObjectCanNotBeMovedException(int x, int y) {
        super("Point (" + x + "," + y + ") is out of bounds");
    }
}
class MovableObjectNotFittableException extends Exception {
    public MovableObjectNotFittableException(String message) {
        super(message);
    }
}

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

interface Movable{
    void moveUp() throws ObjectCanNotBeMovedException;
    void moveDown() throws ObjectCanNotBeMovedException;
    void moveRight() throws ObjectCanNotBeMovedException;
    void moveLeft() throws ObjectCanNotBeMovedException;
    int getCurrentXPosition();
    int getCurrentYPosition();
    TYPE getType();
}

class MovablePoint implements Movable{

    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;

    public MovablePoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if(y < 0 || y + ySpeed > MovablesCollection.Y) throw new ObjectCanNotBeMovedException(x, y + ySpeed);
        y += ySpeed;
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException{
        if(y - ySpeed < 0 || y > MovablesCollection.Y) throw new ObjectCanNotBeMovedException(x, y - ySpeed);
        y -= ySpeed;
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException{
        if(x < 0 || x + xSpeed > MovablesCollection.X) throw new ObjectCanNotBeMovedException(x + xSpeed, y);
        x += xSpeed;
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException{
        if(x - xSpeed < 0 || x > MovablesCollection.X) throw new ObjectCanNotBeMovedException(x - xSpeed, y);
        x -= xSpeed;
    }

    @Override
    public int getCurrentXPosition() {
        return x;
    }

    @Override
    public int getCurrentYPosition() {
        return y;
    }

    @Override
    public TYPE getType() {
        return TYPE.POINT;
    }

    @Override
    public String toString() {
        return String.format("Movable point with coordinates (%d,%d)\n", x, y);
    }
}

class MovableCircle implements Movable{

    private final int radius;
    private final MovablePoint center;

    public MovableCircle(int radius, MovablePoint center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        int x = getCurrentXPosition();
        int y = getCurrentYPosition();
        if(y < 0 || y + radius > MovablesCollection.Y) throw new ObjectCanNotBeMovedException(x, y);
        center.moveUp();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        int x = getCurrentXPosition();
        int y = getCurrentYPosition();
        if(y - radius < 0 || y > MovablesCollection.Y) throw new ObjectCanNotBeMovedException(x, y);
        center.moveDown();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        int x = getCurrentXPosition();
        int y = getCurrentYPosition();
        if(x < 0 || x + radius > MovablesCollection.X) throw new ObjectCanNotBeMovedException(x, y);
        center.moveRight();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        int x = getCurrentXPosition();
        int y = getCurrentYPosition();
        if(x - radius < 0 || x > MovablesCollection.X) throw new ObjectCanNotBeMovedException(x, y);
        center.moveLeft();
    }

    @Override
    public int getCurrentXPosition() {
        return center.getCurrentXPosition();
    }

    @Override
    public int getCurrentYPosition() {
        return center.getCurrentYPosition();
    }

    @Override
    public TYPE getType() {
        return TYPE.CIRCLE;
    }

    public int getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return String.format("Movable circle with center coordinates (%d,%d) and radius %d\n", center.getCurrentXPosition(), center.getCurrentYPosition(), radius);
    }
}

class MovablesCollection{

    private Movable [] movables;
    public static int X;
    public static int Y;

    MovablesCollection(int x_MAX, int y_MAX){
        X = x_MAX;
        Y = y_MAX;
        movables = new Movable[0];
    }


    void addMovableObject(Movable m) throws MovableObjectNotFittableException{
        if (m instanceof MovablePoint){
            int x = m.getCurrentXPosition();
            int y = m.getCurrentYPosition();
            if (x < 0 || x > X || y < 0 || y > Y){
                throw new MovableObjectNotFittableException(String.format("Movable point with center (%d,%d) can not be fitted into the collection", m.getCurrentXPosition(), m.getCurrentYPosition()));
            }
        }
        else if (m instanceof MovableCircle){
            MovableCircle c = (MovableCircle)m;
            int x = c.getCurrentXPosition();
            int y = c.getCurrentYPosition();
            if (x - c.getRadius() < 0 || x + c.getRadius() > X || y - c.getRadius() < 0 || y + c.getRadius() > Y){
                throw new MovableObjectNotFittableException(String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection", m.getCurrentXPosition(), m.getCurrentYPosition(), ((MovableCircle) m).getRadius()));
            }
        }

        Movable [] newMovable = Arrays.copyOf(movables, movables.length + 1);
        newMovable[newMovable.length - 1] = m;
        movables = newMovable;

    }

    void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) {
        for (Movable m : movables) {
            if (m.getType() != type) continue;

            try {
                switch (direction) {
                    case UP: m.moveUp(); break;
                    case DOWN: m.moveDown(); break;
                    case LEFT: m.moveLeft(); break;
                    case RIGHT: m.moveRight(); break;
                }

            } catch (ObjectCanNotBeMovedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void setxMax(int x) {
        X = x;
    }

    public static void setyMax(int y) {
        Y = y;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Collection of movable objects with size " + movables.length + ":\n");
        for (Movable m : movables) {
            if (m != null) {
                sb.append(m);
            }
            //sb.append(m.toString());
        }
        //sb.append("\n");
        return sb.toString();
    }
}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try {
                    collection.addMovableObject(new MovablePoint(x, y, xSpeed, ySpeed));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovableCircle(radius, new MovablePoint(x, y, xSpeed, ySpeed)));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);

        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);

        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);

        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);

        System.out.println(collection.toString());


    }

}


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOError;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-09-27
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public class Ball {
    private int size;
    private final int xDefaultSpeed = 2;
    private final int yDefaultSpeed = 2;
    private int xSpeed;
    private int ySpeed;
    private Coordinate ballCoordinate;
    private BufferedImage ballImage;


    public Ball() { //creates a ball with a default speed
        this.size = 15;
        this.xSpeed = xDefaultSpeed;
        this.ySpeed = yDefaultSpeed;
        ballCoordinate = new Coordinate(0, 0);
        try{
            ballImage = ImageIO.read(new File("images/ball.png"));
        }catch (IOException e){
        }
    }

    public void increaseSpeed(int xSpeed, int ySpeed){
        this.xSpeed += xSpeed;
        this.ySpeed += ySpeed;
    }

    public BufferedImage getBallImage(){
        return ballImage;
    }


    public void setSpeed(int xSpeed, int ySpeed){
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public int getSize() {
        return size;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public void move(){
        ballCoordinate.moveCoord(xSpeed, -ySpeed);
    }

    public Coordinate getCurrentCoordinate(){
        return ballCoordinate;
    }

    public Coordinate getNextCoordinate(){
        return new Coordinate(ballCoordinate.getX() + xSpeed,
                              ballCoordinate.getY() - ySpeed);
    }

    public void setCoordinate(Coordinate newCoord){
        ballCoordinate = newCoord;
    }
}

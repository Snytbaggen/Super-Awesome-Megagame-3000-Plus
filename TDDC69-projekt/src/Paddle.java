import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-09-27
 * Time: 17:03
 * To change this template use File | Settings | File Templates.
 */
public class Paddle {
    private int height;
    private int speed;
    private PaddleLengths length;
    private BufferedImage longPaddleImage, shortPaddleImage, normalPaddleImage;
    private Coordinate paddleCoordinate;

    public Paddle() {
        length = PaddleLengths.DEFAULT;
        height = 15;
        speed = 5;
        paddleCoordinate = new Coordinate(0, 0);
        try{
            normalPaddleImage = ImageIO.read(new File("images/paddle.png"));
            longPaddleImage = ImageIO.read(new File("images/longpaddle.png"));
            shortPaddleImage = ImageIO.read(new File("images/shortpaddle.png"));
        }catch (IOException e){
        }
    }

    public int getLength() {
        return length.getLength();
    }

    public int getHeight() {
        return height;
    }

    public int getSpeed() {
        return speed;
    }

    public void moveLeft(){
        paddleCoordinate.moveCoord(-speed, 0);
    }

    public void moveRight(){
        paddleCoordinate.moveCoord(speed, 0);
    }

    public Coordinate getCurrentCoordinate(){
        return paddleCoordinate;
    }

    public void setCoordinate(Coordinate newCoord){
        paddleCoordinate = newCoord;
    }

    public BufferedImage getPaddleImage(){
        switch (length){
            case DEFAULT:
                return normalPaddleImage;
            case LONG:
                return longPaddleImage;
            case SHORT:
                return shortPaddleImage;
            default:
                return null;
        }
    }

    public void makeLong(){
        length = PaddleLengths.LONG;
    }

    public void makeShort(){
        length = PaddleLengths.SHORT;
    }

    public void makeNormal(){
        length = PaddleLengths.DEFAULT;
    }
}

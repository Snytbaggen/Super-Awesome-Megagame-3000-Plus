import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-09-27
 * Time: 11:29
 * To change this template use File | Settings | File Templates.
 */
public class GameTest {
    public static void main(String[] args){
        GameBoard gameBoard = new GameBoard();
        GameFrame gameFrame = new GameFrame("Super Awesome Megagame 3000+ v1.0", gameBoard);
        gameFrame.setTimer(1000/60);
    }
}

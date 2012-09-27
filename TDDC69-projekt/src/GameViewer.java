import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-09-27
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 */
public class GameViewer extends JComponent {
    private GameBoard gameBoard;

    public GameViewer(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public Dimension getPreferredSize(){
        return new Dimension(gameBoard.getWindowWidth(), gameBoard.getWindowHeight());
    }

    public void paintComponent (final Graphics g){
        Coordinate ballCoord = gameBoard.getBallCoord();
        g.setColor(Color.black);
        g.fillRect(0, 0, gameBoard.getWindowWidth(), gameBoard.getWindowHeight());
        g.setColor(Color.white);
        g.fillOval(ballCoord.getX(), ballCoord.getY(), gameBoard.getBallSize(), gameBoard.getBallSize());



    }

}

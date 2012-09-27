/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-09-27
 * Time: 11:29
 * To change this template use File | Settings | File Templates.
 */
public class GameTest {
    public static void main(String[] args) {
        GameBoard gameBoard = new GameBoard(100+15*56, 500);

        GameFrame gameFrame = new GameFrame("Super Awesome Megagame 3000+ v0.2", gameBoard);
        gameBoard.startBall();
        gameFrame.setTimer(1000/60);
    }
}

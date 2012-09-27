import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-09-27
 * Time: 11:20
 * To change this template use File | Settings | File Templates.
 */
public class GameFrame extends JFrame {
    private GameBoard gameBoard;
    private GameViewer gameViewer;

    private Timer actionTimer;

    public void setTimer(int delay){
        actionTimer = new Timer(delay, doOneStep);
        actionTimer.start();
    }

    final Action doOneStep = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            gameBoard.moveBall();
            gameViewer.repaint();
        }
    };

    public GameFrame(String name, final GameBoard board){
        gameBoard = board;
        gameViewer = new GameViewer(gameBoard);
        Dimension dimension = gameViewer.getPreferredSize();

        super.setTitle(name);
        super.setLayout(new FlowLayout());
        super.add(gameViewer);
        super.setSize(dimension.width + 30, dimension.height + 70);
        super.setVisible(true);

        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

    }
}

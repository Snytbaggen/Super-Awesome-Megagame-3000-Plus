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
            gameBoard.movePaddle();
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
        super.setSize(200 + 20*51, dimension.height + 70);
        super.setVisible(true);

        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        gameViewer.getInputMap().put(KeyStroke.getKeyStroke("LEFT"), "move left");
        gameViewer.getActionMap().put("move left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.startMovePaddle('l');
            }
        });

        gameViewer.getInputMap().put(KeyStroke.getKeyStroke("released LEFT"), "stop moving left");
        gameViewer.getActionMap().put("stop moving left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.stopMovePaddle('l');
            }
        });

        gameViewer.getInputMap().put(KeyStroke.getKeyStroke("RIGHT"), "move right");
        gameViewer.getActionMap().put("move right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.startMovePaddle('r');
            }
        });

        gameViewer.getInputMap().put(KeyStroke.getKeyStroke("released RIGHT"), "stop moving right");
        gameViewer.getActionMap().put("stop moving right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.stopMovePaddle('r');
            }
        });


    }
}

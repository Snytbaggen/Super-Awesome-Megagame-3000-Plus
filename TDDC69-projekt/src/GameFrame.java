import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-09-27
 * Time: 11:20
 * To chanballCoord.moveCoord(ball.getxSpeed(), - ball.getySpeed());ge this template use File | Settings | File Templates.
 */
public class GameFrame extends JFrame {
    private GameBoard gameBoard;
    private GameViewer gameViewer;
    private JMenuBar menuBar = new JMenuBar();
    private Timer actionTimer;
    private JMenu fileMenu = new JMenu("File");
    private JMenu editorMenu = new JMenu("Editor");
    private JMenuItem startRestartMenuItem = new JMenuItem("Start/Restart",'S');
    private JMenuItem toggleBackgroundMenuItem = new JMenuItem("Turn Background On/Off", 'B');
    private JMenuItem toggleBackgroundMusic = new JMenuItem("Turn Music On/Off", 'M');
    private JMenuItem levelEditorMenuItem = new JMenuItem("Enter Level Editor",'E');
    private JMenuItem saveLevelMenuItem = new JMenuItem("Save Level",'S');
    private JMenuItem randomBoardmenuItem = new JMenuItem("Random Board",'R');
    private JMenuItem clearBoardMenuItem = new JMenuItem("Clear Board",'C');
    final GameSound bgmusic = new GameSound();
    final Runnable music = bgmusic;
    final Thread musicThread = new Thread(music, "Background Music");



    private JMenuItem exitMenuItem = new JMenuItem("Exit",'x');
    public void setTimer(int delay){
        actionTimer = new Timer(delay, doOneStep);
        gameBoard.unPause();
        actionTimer.start();
    }

    final Action doOneStep = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameBoard.isPaused())
                gameBoard.tick();
        }
    };

    public GameFrame(String name, final GameBoard board){
        musicThread.start();
        gameBoard = board;
        gameViewer = new GameViewer(gameBoard);
        Dimension dimension = gameViewer.getPreferredSize();
        gameBoard.addBoardListener(gameViewer);
        super.setLayout(new FlowLayout());

        fileMenu.add(startRestartMenuItem);
        fileMenu.add(toggleBackgroundMenuItem);
        fileMenu.add(toggleBackgroundMusic);
        fileMenu.add(exitMenuItem);

        editorMenu.add(levelEditorMenuItem);
        editorMenu.add(saveLevelMenuItem);
        editorMenu.add(randomBoardmenuItem);
        editorMenu.add(clearBoardMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(editorMenu);

        super.setJMenuBar(menuBar);
        super.setTitle(name);
        super.add(gameViewer);
        super.setSize(966, dimension.height + 70);
        super.setVisible(true);

        /***********
        *Menu Items*
        ************/
        startRestartMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameBoard.isInEditor()) {
                    bgmusic.startMusic();
                }
                if (gameBoard.isInEditor() || gameBoard.gameOver()) {
                    gameBoard.saveLevels();
                    gameBoard.newGame();
                }
            }
        });

        toggleBackgroundMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameViewer.toggleBackground();
            }
        });

        toggleBackgroundMusic.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (bgmusic.isRunning())
                    bgmusic.stopMusic();
                else
                    bgmusic.startMusic();
            }
        });

        exitMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        levelEditorMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameBoard.isInEditor()){
                    bgmusic.stopMusic();
                    gameBoard.enterEditor();
                }else{
                    bgmusic.startMusic();
                    gameBoard.saveLevels();
                    gameBoard.newGame();
                }
            }
        });

        saveLevelMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameBoard.isInEditor())
                    gameBoard.addBoard();
            }
        });

        randomBoardmenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameBoard.isInEditor()){
                    gameBoard.randomBoard();
                }
            }
        });

        clearBoardMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameBoard.isInEditor()){
                    gameBoard.clearBoard();
                }
            }
        });

        /******************
         *Window listeners*
         ******************/
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        /*****************
         *Mouse listeners*
         *****************/
        gameViewer.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (gameBoard.isInEditor()) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        gameBoard.insertCurrentCreatorBrickToCoordinate(new Coordinate(e.getX(), e.getY()));
                    }else if (e.getButton() == MouseEvent.BUTTON3){
                        gameBoard.removeCreatorBrickFromBoard(new Coordinate(e.getX(), e.getY()));
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        gameViewer.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if(e.getWheelRotation() < 0){
                    gameBoard.nextBrick();
                }else if (e.getWheelRotation() > 0){
                    gameBoard.previousBrick();
                }
            }
        });


        /********************
         *Keyboard listeners*
         ********************/
        gameViewer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("B"), "toggle background");
        gameViewer.getActionMap().put("toggle background", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameViewer.toggleBackground();
            }
        });

        gameViewer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("M"), "toggle music");
        gameViewer.getActionMap().put("toggle music", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (bgmusic.isRunning())
                    bgmusic.stopMusic();
                else
                    bgmusic.startMusic();
            }
        });

        gameViewer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "new ball");
        gameViewer.getActionMap().put("new ball", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.newBall();
            }
        });

        gameViewer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "move left");
        gameViewer.getActionMap().put("move left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gameBoard.isInEditor()){
                    gameBoard.editorPreviousLevel();
                }else{
                    gameBoard.startMovePaddle('l');
                }
            }
        });

        gameViewer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "restart game");
        gameViewer.getActionMap().put("restart game", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameBoard.isInEditor()) {
                    bgmusic.startMusic();
                    gameBoard.saveLevels();
                    gameBoard.newGame();
                }else if (gameBoard.gameOver()) {
                    gameBoard.newGame();
                }
            }
        });

        gameViewer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released LEFT"), "stop moving left");
        gameViewer.getActionMap().put("stop moving left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.stopMovePaddle('l');
            }
        });

        gameViewer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "move right");
        gameViewer.getActionMap().put("move right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(gameBoard.isInEditor()){
                    gameBoard.editorNextLevel();
                }else{
                    gameBoard.startMovePaddle('r');
                }
            }
        });

        gameViewer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released RIGHT"), "stop moving right");
        gameViewer.getActionMap().put("stop moving right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameBoard.stopMovePaddle('r');
            }
        });

        gameViewer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F2"), "start game");
        gameViewer.getActionMap().put("start game", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameBoard.isInEditor()) {
                    bgmusic.startMusic();
                    gameBoard.saveLevels();
                    gameBoard.newGame();
                }else if (gameBoard.gameOver()) {
                    gameBoard.newGame();
                }
            }
        });

        gameViewer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("P"), "pause");
        gameViewer.getActionMap().put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameBoard.isInEditor()){
                    if (gameBoard.isPaused()){
                        gameBoard.unPause();
                    }else {
                        gameBoard.pause();
                    }
                }
            }
            });

    }
}

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-09-27
 * Time: 11:12
 * To change this template use File | Settings | File Templates.
 */
public class GameViewer extends JComponent implements BoardListener {
    private GameBoard gameBoard;
    private BufferedImage background, oneHit, twoHit, threeHit, indestructible, powerup;
    private BufferedImage longPaddlePowerup, shortPaddlePowerup, pointsPowerup, lifePowerup;
    private boolean isBackgroundOn;

    public GameViewer(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        isBackgroundOn = true;

        try{
            this.background = ImageIO.read(new File("images/background.png"));
            this.oneHit = ImageIO.read(new File("images/onehit.png"));
            this.twoHit = ImageIO.read(new File("images/twohit.png"));
            this.threeHit = ImageIO.read(new File("images/threehit.png"));
            this.indestructible = ImageIO.read(new File("images/indestructible.png"));
            this.powerup = ImageIO.read(new File("images/powerup.png"));
            this.longPaddlePowerup = ImageIO.read(new File("images/longerpaddlepowerup.png"));
            this.shortPaddlePowerup = ImageIO.read(new File("images/shorterpaddlepowerup.png"));
            this.pointsPowerup = ImageIO.read(new File("images/pointspowerup.png"));
            this.lifePowerup = ImageIO.read(new File("images/extralifepowerup.png"));

        }catch (IOException e){
        }
    }

    public Dimension getPreferredSize(){
        return new Dimension(gameBoard.getWindowWidth(), gameBoard.getWindowHeight());
    }

    public void paintComponent(final Graphics g){

        if(!gameBoard.isInEditor()) {
            if (isBackgroundOn){
                g.drawImage(background, 0, 0, this);
            }else{
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, gameBoard.getWindowWidth(), gameBoard.getWindowHeight());
            }
        }

        Brick tempBrick;
        BufferedImage tempImage;
        for (int x = 0; x < gameBoard.getBrickBoardWidth(); x++){
            for(int y = 0; y < gameBoard.getBrickBoardHeight(); y++){
                tempBrick = gameBoard.getBrick(x, y);
                if (gameBoard.isInEditor()){
                    g.drawRect(gameBoard.getXPadding() + x * gameBoard.getBrickWidth(),
                            gameBoard.getYPadding() + y * gameBoard.getBrickHeight(),
                            gameBoard.getBrickWidth(), gameBoard.getBrickHeight());
                }
                if (tempBrick != null){
                    tempImage = getBrickImage(tempBrick.getCurrentType());
                    if (tempImage != null){
                        g.drawImage(getBrickImage(tempBrick.getCurrentType()), gameBoard.getXPadding() + x * gameBoard.getBrickWidth(),
                                    gameBoard.getYPadding() + y * gameBoard.getBrickHeight(), this);
                    }else{
                        g.setColor(tempBrick.getColor());
                        g.fillRect(gameBoard.getXPadding() + x * gameBoard.getBrickWidth(), gameBoard.getYPadding() + y * gameBoard.getBrickHeight(),
                                   gameBoard.getBrickWidth(), gameBoard.getBrickHeight());
                    }
                }
            }
        }

        if (gameBoard.isInEditor()){

            g.drawString("Current block:", 5, 20);
            g.drawImage(getBrickImage(gameBoard.getCurrentCreatorBrick().getCurrentType()), 90, 5, this);

            g.drawString("Left click: Place block", 8, gameBoard.getWindowHeight()-68);
            g.drawString("Right click: Remove block", 8, gameBoard.getWindowHeight()-53);
            g.drawString("Left/right arrows: Previous/next level", 8, gameBoard.getWindowHeight()-38);
            g.drawString("Scroll wheel: Change block", 8, gameBoard.getWindowHeight()-23);
            g.drawString("Enter: Start game", 8, gameBoard.getWindowHeight()-8);

            if(gameBoard.isLevelAdded())
                g.drawString("Level saved.", gameBoard.getWindowWidth()/2-35, gameBoard.getWindowHeight()/2+50);
        }else{
            if (gameBoard.ballExists()){
                Coordinate ballCoord = gameBoard.getBallCoord();
                tempImage = gameBoard.getBallImage();
                if (tempImage != null){
                    g.drawImage(gameBoard.getBallImage(), ballCoord.getX(), ballCoord.getY(), this);
                }else{
                    g.setColor(Color.WHITE);
                    g.fillOval(ballCoord.getX(), ballCoord.getY(), gameBoard.getBallSize(), gameBoard.getBallSize());
                }
            }

            if (gameBoard.powerupExists()){
                Coordinate powerupCoord = gameBoard.getPowerupCoord();
                tempImage = getPowerupImage(gameBoard.getPowerup());
                if (tempImage != null){
                    g.drawImage(getPowerupImage(gameBoard.getPowerup()),powerupCoord.getX(), powerupCoord.getY(), this);
                }else{
                    g.setColor(Color.BLUE);
                    g.fillRect(powerupCoord.getX(), powerupCoord.getY(), gameBoard.getPowerupSize(), gameBoard.getPowerupSize());
                }
            }

            Coordinate paddleCoord = gameBoard.getPaddleCoord();
            g.setColor(Color.WHITE);
            g.drawImage(gameBoard.getPaddleImage(), paddleCoord.getX(), paddleCoord.getY(), this);

            g.setColor(Color.WHITE);
            g.drawString("Level: " + gameBoard.getCurrentLevel(), 8, 15);
            g.drawString(gameBoard.getLives(), 8, gameBoard.getWindowHeight() - 23);
            g.drawString(gameBoard.getScore(), 8, gameBoard.getWindowHeight() - 8);

            if(gameBoard.gameOver()){
                g.drawString("GAME OVER", gameBoard.getWindowWidth()/2-30, gameBoard.getWindowHeight()/2+50);
                g.drawString("Press ENTER to try again", gameBoard.getWindowWidth()/2-80, gameBoard.getWindowHeight()/2+65);

            }else if (gameBoard.isPaused()){
                g.drawString("PAUSED", gameBoard.getWindowWidth()/2-30, gameBoard.getWindowHeight()/2+50);
            }
        }
    }


    private BufferedImage getBrickImage(Type type){
        switch (type){
            case POWERUP:
                return powerup;
            case ONE_HIT:
                return oneHit;
            case TWO_HIT:
                return twoHit;
            case THREE_HIT:
                return threeHit;
            case UNCHANGEABLE:
                return indestructible;
            case EMPTY:
                return null;
            default:
                return null;
        }
    }

    private BufferedImage getPowerupImage(Powerup p){
        switch (p.getPowerupType()){
            case LONGER_PADDLE:
                return longPaddlePowerup;
            case SHORTER_PADDLE:
                return shortPaddlePowerup;
            case EXTRA_LIFE:
                return lifePowerup;
            case EXTRA_POINTS:
                return pointsPowerup;
            default:
                return null;
        }
    }

    public void toggleBackground(){
        if (isBackgroundOn)
            isBackgroundOn = false;
        else
            isBackgroundOn = true;
        repaint();
    }

    @Override
    public void boardChanged() {
        repaint();
    }
}

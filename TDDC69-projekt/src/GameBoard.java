import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-09-27
 * Time: 10:57
 * To change this template use File | Settings | File Templates.
 */
public class GameBoard {
    private Ball ball;
    private Coordinate ballCoord;
    private int windowWidth, windowHeight;

    public GameBoard(int windowWidth, int windowHeight) {
        this.ball = new Ball();
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        ballCoord = new Coordinate((windowWidth-ball.getSize())/2, (windowHeight-ball.getSize())/2);
    }

    public void startBall(){
        ball.setSpeed(4, 2);
    }

    public void moveBall(){
        if (ball.getxSpeed() + ballCoord.getX() + ball.getSize() >= windowWidth){
            changeBallDirection('r');
        }else if (ballCoord.getX()+ball.getxSpeed() <= 0){ //in this case xSpeed will be negative
            changeBallDirection('l');
        }else if (ballCoord.getY() - ball.getySpeed()  + ball.getSize() >= windowHeight-1){
            changeBallDirection('d');
        } else  if (ballCoord.getY() - ball.getySpeed() <= 0){
            changeBallDirection('u');
        }
        ballCoord.moveCoord(ball.getxSpeed(), - ball.getySpeed());

    }

    private Boolean canBallMove(){
        if (ball.getxSpeed() + ballCoord.getX() + ball.getSize() >= windowWidth){
            System.out.println("right");
            return false;
        }else if (ballCoord.getX()+ball.getxSpeed() <= 0){ //in this case xSpeed will be negative
            System.out.println("left");
            return false;
        }else if (ballCoord.getY() - ball.getySpeed()  + ball.getSize() >= windowHeight-1){
            System.out.println("down");
            return false;
        } else  if (ballCoord.getY() - ball.getySpeed() <= 0){
            System.out.println("up");
            return false;
        }else{
            return true;
        }
    }

    private void changeBallDirection(Character wallDirection){
        switch (wallDirection){
            case 'u':
                ball.setSpeed(ball.getxSpeed(), -ball.getySpeed());
                break;
            case 'd':
                ball.setSpeed(ball.getxSpeed(), -ball.getySpeed());
                break;
            case 'l':
                ball.setSpeed(0-ball.getxSpeed(), ball.getySpeed());
                break;
            case 'r':
                ball.setSpeed(0-ball.getxSpeed(), ball.getySpeed());
                break;
            default:
                return;
        }
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getBallSize(){
        return ball.getSize();
    }

    public Coordinate getBallCoord(){
        return ballCoord;
    }

    public int getWindowHeight() {
        return windowHeight;
    }
}

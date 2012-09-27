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
    private BrickBoard brickBoard ;
    private Paddle paddle;
    private Coordinate paddleCoord;
    private Boolean paddleMoveRight, paddleMoveLeft;


    public GameBoard(int windowWidth, int windowHeight) {
        this.ball = new Ball();
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        ballCoord = new Coordinate((windowWidth-ball.getSize())/2, (windowHeight-ball.getSize())/2);
        brickBoard = new BrickBoard();
        brickBoard.randomBoard();
        paddle = new Paddle();
        paddleCoord = new Coordinate((windowWidth-ball.getSize())/2, windowHeight-50);
        paddleMoveRight = false;
        paddleMoveLeft = false;
    }

    public Color getBrickColor(int brickXCoord, int brickYCoord){
        return brickBoard.getColor(brickXCoord, brickYCoord);
    }

    public void startBall(){
        ball.setSpeed(4, 4);
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

    public void startMovePaddle(char dir){
        if (dir == 'r'){
            paddleMoveRight = true;
        }else if (dir == 'l'){
            paddleMoveLeft = true;
        }
    }

    public void stopMovePaddle(char dir){
        if (dir == 'r'){
            paddleMoveRight = false;
        }else if (dir == 'l'){
            paddleMoveLeft = false;
        }
    }

    public void movePaddle(){
        if (paddleMoveRight){
            if(paddleCoord.getX()+paddle.getLength()>=windowWidth){
                stopMovePaddle('r');
            }else{
                paddleCoord.moveCoord(5, 0);
            }
        }
        if (paddleMoveLeft){
            if(paddleCoord.getX()<=0){
                stopMovePaddle('l');
            }else{
                paddleCoord.moveCoord(-5, 0);
            }
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

    public Coordinate getPaddleCoord(){
        return paddleCoord;
    }

    public int getPaddleWidth(){
        return paddle.getLength();
    }

    public int getPaddleHeight(){
        return paddle.getHeight();
    }

    public int getWindowHeight() {
        return windowHeight;
    }
}

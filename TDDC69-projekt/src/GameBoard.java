import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-09-27
 * Time: 10:57
 * To change this template use File | Settings | File Templates.
 */
public class GameBoard {
    private List listeners = new ArrayList();
    private final int X_PADDING = 50;
    private final int Y_PADDING = 50;
    private int windowWidth, windowHeight;
    private int score;
    private int lives;

    private BrickBoard brickBoard ;
    private Levels levelArray;
    private Paddle paddle;
    private Ball ball;
    private Powerup powerup;
    private Brick currentCreatorBrick;

    private boolean paddleMoveRight, paddleMoveLeft;
    private boolean gameover;
    private boolean paused;
    private boolean levelAdded;
    private boolean hasAnythingMovedOrChanged;

    private boolean creatorPhase;
    private final Type[] typeArray = Type.values();
    private int typeArrayPosition;


    public GameBoard(){
        brickBoard = new BrickBoard();
        levelArray = new Levels();
        this.windowWidth = X_PADDING*2 + brickBoard.getBrickWidth()*brickBoard.getBoardWidth();
        this.windowHeight = Y_PADDING+250 + brickBoard.getBrickHeight()*brickBoard.getBoardHeight();
        creatorPhase = false;
        newGame();
        currentCreatorBrick = new OneHitBrick();
    }

    public void tick(){
        if (!creatorPhase){
            if (gameover)
                return;
            hasAnythingMovedOrChanged = false;
            movePaddle();
            moveBall();
            movePowerup();
            if (hasAnythingMovedOrChanged)
                notifyListeners();
        }
    }


    /*************
     *Coordinates*
     *************/
    public Coordinate getPowerupCoord(){
        return powerup.getCurrentCoordinate();
    }

    public Coordinate getPaddleCoord(){
        return paddle.getCurrentCoordinate();
    }

    public Coordinate getBallCoord(){
        return ball.getCurrentCoordinate();
    }

    //Converts any coordinate to a position in the brick array. Resulting position may be out of bounds.
    private Coordinate coordinateToArrayPosition(Coordinate c){
        return new Coordinate((c.getX()-X_PADDING)/brickBoard.getBrickWidth(), (c.getY()-Y_PADDING)/brickBoard.getBrickHeight());
    }


    /**********************
     *Functions for paddle*
     **********************/
    public void movePaddle(){
        Coordinate paddleCoord = paddle.getCurrentCoordinate();
        if (paddleMoveRight){
            if(paddleCoord.getX()+paddle.getLength()>=windowWidth){ //Checks if paddle is at right edge
                stopMovePaddle('r');
            }else{
                paddle.moveRight();
                hasAnythingMovedOrChanged = true;
            }
        }
        if (paddleMoveLeft){
            if(paddleCoord.getX()<=0){ //Checks if paddle is at left edge
                stopMovePaddle('l');
            }else{
                paddle.moveLeft();
                hasAnythingMovedOrChanged = true;
            }
        }
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

    private void restorePaddle(){ //Resets the paddle to its normal length and places it in the middle.
        paddle.makeNormal();
        if (paddle.getCurrentCoordinate().getX()+paddle.getLength()>windowWidth){
            paddle.getCurrentCoordinate().moveCoord(windowWidth-(paddle.getCurrentCoordinate().getX()+paddle.getLength()), 0);
            hasAnythingMovedOrChanged = true;
        }
    }

    public BufferedImage getPaddleImage(){
        return paddle.getPaddleImage();
    }


    /********************
     *Functions for ball*
     ********************/
    public void moveBall(){
        if (ball == null){
            return;
        }
        brickCollisions();
        if (ball != null){ //If the ball collides with the last brick it gets removed and a new level starts.
            paddleCollisions();
            edgeCollisions();
            if (ball != null){ //If the ball collides with the lower edge it gets killed.
                ball.move();
                hasAnythingMovedOrChanged = true;
            }
        }
    }

    private void brickCollisions(){
        if (ballCollidingWithBrickArray()){
            Coordinate arrayPos;
            Coordinate blockPos;
            ArrayList<Coordinate> arrayList = ballCoordsToBrickArrayCoords();
            for (int i = 0; i < arrayList.size(); i++){
                arrayPos = arrayList.get(i);
                if(ballCollidingWithBrick(arrayPos)){
                    blockPos = new Coordinate(X_PADDING + arrayPos.getX()*brickBoard.getBrickWidth(), Y_PADDING + arrayPos.getY()*brickBoard.getBrickHeight());
                    //Above line converts the array position of the block to a coordinate in the board.
                    if (ballCollidesWithBrickInDirection(blockPos, 'u')){
                        if(changeBallDirection('u')){
                           newPowerup(arrayPos);
                            if (downgradeBrick(arrayPos)){
                                increaseScore();
                                hasAnythingMovedOrChanged = true;
                                if (stageClear())
                                    return;
                            }
                        }
                    }
                    if (ballCollidesWithBrickInDirection(blockPos, 'r')){
                        if(changeBallDirection('r')){
                            newPowerup(arrayPos);
                            if (downgradeBrick(arrayPos)){
                                increaseScore();
                                hasAnythingMovedOrChanged = true;
                                if(stageClear())
                                    return;
                            }
                        }
                    }
                    if (ballCollidesWithBrickInDirection(blockPos, 'd')){
                        if(changeBallDirection('d')){
                            newPowerup(arrayPos);
                            if (downgradeBrick(arrayPos)){
                                increaseScore();
                                hasAnythingMovedOrChanged = true;
                                if(stageClear())
                                    return;
                            }
                        }
                    }
                    if (ballCollidesWithBrickInDirection(blockPos, 'l')){
                        if(changeBallDirection('l')){
                            newPowerup(arrayPos);
                            if (downgradeBrick(arrayPos)){
                                increaseScore();
                                hasAnythingMovedOrChanged = true;
                                if(stageClear())
                                    return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void paddleCollisions(){
        if (ballCollidingWithPaddle('u')){
            changeBallDirection('u');
            if (paddleMoveLeft){
                ball.increaseSpeed(-1, 0);
            }
            if (paddleMoveRight){
                ball.increaseSpeed(1, 0);
            }
        }else if (ballCollidingWithPaddle('d')){
            changeBallDirection('d');
            if (paddleMoveLeft && ball.getxSpeed() > -3){ //Curves the ball depending on the direction if the paddle is moving
                ball.increaseSpeed(-1, 0);
            }
            if (paddleMoveRight && ball.getxSpeed() < 3){
                ball.increaseSpeed(1, 0);
            }
        }else if (ballCollidingWithPaddle('r')){
            changeBallDirection('r');
            if (paddleMoveLeft){
                ball.setSpeed(-paddle.getSpeed() - 1, ball.getySpeed()); //Sets the speed slightly greater than the paddle
            }                                                            //to avoid collisions.
        }else if (ballCollidingWithPaddle('l')){
            changeBallDirection('l');
            if (paddleMoveRight){
                ball.setSpeed(paddle.getSpeed() + 1, ball.getySpeed());
            }
        }
    }

    public void edgeCollisions(){
        Coordinate ballCoord = ball.getCurrentCoordinate();
        if (ball.getxSpeed() + ballCoord.getX() + ball.getSize() >= windowWidth){ //Checks if ball hits right edge
            changeBallDirection('r');
        }else if (ballCoord.getX()+ball.getxSpeed() <= 0){ //Checks if ball hits left edge
            changeBallDirection('l');
        }else if (ballCoord.getY() - ball.getySpeed()  + ball.getSize() > windowHeight+ball.getSize()){ //Checks if ball hitslower edge
            killBall();
            restorePaddle();
            lives--;
            if (lives <= 0)
                gameover = true;
        } else  if (ballCoord.getY() - ball.getySpeed() <= 0){ //Checks if ball hits upper edge
            changeBallDirection('u');
        }
    }

    private boolean ballCollidingWithBrickArray(){ //Checks if the ball currently intersects with the position of the brick array
        Coordinate ballCoord = ball.getNextCoordinate();
        if (ballCoord.getX()+ball.getSize()>X_PADDING && X_PADDING + brickBoard.getBoardWidth()*brickBoard.getBrickWidth() > ballCoord.getX() &&
                ballCoord.getY()+ball.getSize()>Y_PADDING && Y_PADDING+brickBoard.getBoardHeight()*brickBoard.getBrickHeight() > ballCoord.getY()){
            return true;
        }
        return false;
    }

    private boolean ballCollidingWithBrick(Coordinate arrayPos){
        if (!brickBoardPosOutOfBounds(arrayPos) && brickBoard.getType(arrayPos.getX(), arrayPos.getY()) != null){
            return true;
        }
        return false;
    }

    //Loops through the edges of the (square) ball and gets the positions for every (real) position in the brick
    //array it is colliding with
    public ArrayList<Coordinate> ballCoordsToBrickArrayCoords(){
        Coordinate arrayPos;
        ArrayList<Coordinate> arrayList = new ArrayList<Coordinate>();
        Coordinate ballCoord = ball.getNextCoordinate();
        for(int x=0; x<ball.getSize(); x++){
            arrayPos = coordinateToArrayPosition(new Coordinate(ballCoord.getX()+x, ballCoord.getY()));
            if(!arrayList.contains(arrayPos) && !brickBoardPosOutOfBounds(arrayPos)){ //Checks position on upper edge
                arrayList.add(arrayPos);
            }
            arrayPos = coordinateToArrayPosition(new Coordinate(ballCoord.getX()+x, ballCoord.getY()+ball.getSize()-1));
            if(!arrayList.contains(arrayPos) && !brickBoardPosOutOfBounds(arrayPos)){ //Checks position on lower edge
                arrayList.add(arrayPos);
            }
        }
        for(int y=0; y<ball.getSize(); y++){
            arrayPos = coordinateToArrayPosition(new Coordinate(ballCoord.getX(), ballCoord.getY()+y));
            if(!arrayList.contains(arrayPos) && !brickBoardPosOutOfBounds(arrayPos)){ //Checks position on left edge
                arrayList.add(arrayPos);
            }
            arrayPos = coordinateToArrayPosition(new Coordinate(ballCoord.getX()+ball.getSize()-1, ballCoord.getY()+ball.getSize()+y));
            if(!arrayList.contains(arrayPos) && !brickBoardPosOutOfBounds(arrayPos)){ //checks position on right edge.
                arrayList.add(arrayPos);
            }
        }
        return arrayList;
    }


   /**********************************************************************************************************************
    * For a hit to be detected four conditions must be fulfilled, one for each side of the ball. All we know is that we  *
    * have a hit, the function below detects on which side the hit was depending on these conditions. For example, if the*
    * ball hits a block above it (case 'u' in the function) the constraints are as follows:                              *
    *                                                                                                                    *
    * 1. The upper side of the ball must be inside the block. This is because we look ahead at the ball's next position; *
    *    if the ball is inside a block we will clearly get a hit the next time we move.
    *
    * 2. The lower side of the ball must be below the block. This is pretty self-explanatory, but is necessary to avoid  *
    *    false positives when we search for a hit with a block below.
    *
    * 3. The right side of the ball must be to the right of the block's left side.
    *
    * 4. In a similar way, the left side of the ball must be to the left of the block's right side.                *
    *                                                                                                                    *
    * 3 and 4 are there to avoid false hits when the ball is passing to the right or left of the block. There is also a  *
    * margin added to each side of the X axis which is equal to the ball's X speed. This is because we're always looking *
    * at the ball's next position, and if we got a 'left' or 'right' hit the ball would be inside the block. If we don't *
    * add this margin we would get a false 'up' or 'down' hit since all conditions would be fulfilled.                   *
    *                                                                                                                    *
    * All other directions are checked according to the same principle, including the margin. 'up' and 'down' have       *
    * margins in the X axis and 'left' and 'right' have their margins in the Y axis.                                     *
    * ********************************************************************************************************************/
    private boolean ballCollidesWithBrickInDirection(Coordinate blockPos, char dir){
        int ballXSpeed = abs(ball.getxSpeed()*ball.getxSpeed());
        int ballYSpeed = abs(ball.getySpeed()*ball.getySpeed());
        Coordinate ballCoord = ball.getNextCoordinate();
        switch (dir){
            case 'u':
                //In this case the orders of the conditions are (1 && 2 && 3 && 4) where 1-4 are as in the comment above.
                if (blockPos.getY()+brickBoard.getBrickHeight() > ballCoord.getY() && ballCoord.getY() + ball.getSize() > blockPos.getY()+brickBoard.getBrickHeight() &&
                        ballCoord.getX()+ballXSpeed < blockPos.getX()+brickBoard.getBrickWidth() && ballCoord.getX()+ball.getSize()-ballXSpeed > blockPos.getX()){
                    return true;
                }
                break;
            case 'd':
                //Order here is (lower edge inside block && upper edge above block && left edge left of brick's right && right edge right of brick's left)
                if (ballCoord.getY()+ball.getSize() > blockPos.getY() && ballCoord.getY() < blockPos.getY() &&
                        ballCoord.getX()+ballXSpeed < blockPos.getX()+brickBoard.getBrickWidth() && ballCoord.getX()+ball.getSize()-ballXSpeed > blockPos.getX()){
                    return true;
                }
                break;
            case 'r':
                //Order here is (right edge inside brick && left side left of brick && upper edge above brick's lower && lower edge below brick's upper)
                if (ballCoord.getX()+ball.getSize() > blockPos.getX() && ballCoord.getX() < blockPos.getX() &&
                        ballCoord.getY()+ballYSpeed < blockPos.getY() + brickBoard.getBrickHeight() && ballCoord.getY()+ball.getSize()-ballYSpeed > blockPos.getY()){
                    return true;
                }
                break;
            case 'l':
                //Order here is (left edge inside block && right edge right of brick && upper edge above brick's lower && lower edge below brick's upper)
                if (ballCoord.getX() < blockPos.getX() + brickBoard.getBrickWidth() && ballCoord.getX()+ball.getSize() > blockPos.getX()+brickBoard.getBrickWidth() &&
                        ballCoord.getY()+ballYSpeed < blockPos.getY() + brickBoard.getBrickHeight() && ballCoord.getY()+ball.getSize()-ballYSpeed > blockPos.getY()){
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }


    /*There's a possibility we get several hits in the same direction, but we only want to change the ball's direction
    * once. Therefore a check of the ball's direction is done every time, to see if it's already travelling in the
    * direction we want. If it is we do not do anything and return false, since we did not change the direction of
    * the ball. If we do change it we also return true.*/
    private boolean changeBallDirection(Character wallDirection){
        switch (wallDirection){
            case 'u':
                if (ball.getySpeed() > 0){
                    ball.setSpeed(ball.getxSpeed(), -ball.getySpeed());
                    return true;
                }
                break;
            case 'd':
                if (ball.getySpeed() < 0){
                    ball.setSpeed(ball.getxSpeed(), -ball.getySpeed());
                    return true;
                }
                break;
            case 'l':
                if (ball.getxSpeed() < 0){
                    ball.setSpeed(-ball.getxSpeed(), ball.getySpeed());
                    return true;
                }
                break;
            case 'r':
                if(ball.getxSpeed() > 0){
                    ball.setSpeed(-ball.getxSpeed(), ball.getySpeed());
                    return true;
                }
                break;
            case 'c': //corner
                ball.setSpeed(-ball.getxSpeed(), -ball.getySpeed());
                return true;
            default:
                break;
        }
        return false;
    }

    /***********************************************************************************************************************
     * For a hit to be detected four conditions must be fulfilled, one for each side of the ball. All we know is that we   *
     * have a hit, the function below detects on which side the hit was depending on these conditions. For example, if the *
     * ball hits the paddle from above (case 'd' in the function) the constraints are as follows:                          *
     *                                                                                                                     *
     * 1. The lower side of the ball must be inside the paddle.
     *
     * 2. The upper side of the ball must be above the block. This is pretty self-explanatory, but is necessary to avoid   *
     *    false positives when we search for a hit with the paddle from below.
     *
     * 3. The right side of the ball must be to the right of the paddle's left side.
     *
     * 4. In a similar way, the left side of the ball must be to the left of the paddles's right side.               *
     *                                                                                                                     *
     * 3 and 4 are there to avoid false hits when the ball is passing to the right or left of the paddle. There is also a  *
     * margin added to each side of the X axis which is equal to the ball's X speed. This is because we're always looking  *
     * at the ball's next position, and if we got a 'left' or 'right' hit the ball would be inside the paddle. If we don't *
     * add this margin we would get a false 'up' or 'down' hit since all conditions would be fulfilled.                    *
     *                                                                                                                     *
     * All other directions are checked according to the same principle, including the margin. 'up' and 'down' have        *
     * margins in the X axis and 'left' and 'right' have their margins in the Y axis.                                      *
     * *********************************************************************************************************************/
    private boolean ballCollidingWithPaddle(char dir){
        int ballXSpeed = abs(ball.getxSpeed()*ball.getxSpeed());
        int ballYSpeed = abs(ball.getySpeed()*ball.getySpeed());
        Coordinate ballCoord = ball.getCurrentCoordinate();
        Coordinate paddleCoord = paddle.getCurrentCoordinate();
        switch (dir){
            case 'u':
                //Conditions are (upper edge inside paddle && lower edge below paddle && left edge left of paddle's right && right edge right of paddle's right)
                if (paddleCoord.getY()+paddle.getHeight() > ballCoord.getY() && ballCoord.getY() + ball.getSize() > paddleCoord.getY()+paddle.getLength() &&
                        ballCoord.getX()+ballXSpeed < paddleCoord.getX()+paddle.getLength() && ballCoord.getX()+ball.getSize()-ballXSpeed > paddleCoord.getX()){
                    return true;
                }
                break;
            case 'd':
                //Conditions are (lower edge inside paddle && upper edge above paddle && left edge left of paddle's right && right edge right of paddle's right)
                if (ballCoord.getY()+ball.getSize() > paddleCoord.getY() && ballCoord.getY() < paddleCoord.getY() &&
                        ballCoord.getX()+ballXSpeed < paddleCoord.getX()+paddle.getLength() && ballCoord.getX()+ball.getSize()-ballXSpeed > paddleCoord.getX()){
                    return true;
                }
                break;
            case 'r':
                //Conditions are (right edge inside paddle && left edge left of paddle && upper edge above paddle's lower && lower edge beneath paddle's upper)
                if (ballCoord.getX()+ball.getSize() > paddleCoord.getX() && ballCoord.getX() < paddleCoord.getX() &&
                        ballCoord.getY()+ballYSpeed < paddleCoord.getY() + paddle.getHeight() && ballCoord.getY()+ball.getSize()-ballYSpeed > paddleCoord.getY()){
                    return true;
                }
                break;
            case 'l':
                //Conditions are (left edge inside paddle && right edge right of paddle && upper edge above paddle's lower && lower edge beneath paddle's upper)
                if (ballCoord.getX() < paddleCoord.getX() + paddle.getLength() && ballCoord.getX()+ball.getSize() > paddleCoord.getX()+paddle.getLength() &&
                        ballCoord.getY()+ballYSpeed < paddleCoord.getY() + paddle.getHeight() && ballCoord.getY()+ball.getSize()-ballYSpeed > paddleCoord.getY()){
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    public  void killBall(){
        ball = null;
        hasAnythingMovedOrChanged = true;
    }

    public void newBall(){
        if (ball == null && lives > 0){
            ball = new Ball();
            ball.setCoordinate(new Coordinate(paddle.getCurrentCoordinate().getX()+paddle.getLength()/2-ball.getSize()/2,
                               paddle.getCurrentCoordinate().getY()-ball.getSize()-1));
           // ball.start();
            hasAnythingMovedOrChanged = true;
        }
    }

    public BufferedImage getBallImage(){
        return ball.getBallImage();
    }

    public boolean ballExists(){
        return ball != null;
    }

    public int getBallSize(){
        return ball.getSize();
    }


    /************************
     *Functions for powerups*
     ************************/
    private void movePowerup(){
        if (powerup == null)
            return;
        if (powerupCollidesWithPaddle()){
            executePowerup();
            powerup = null;
        }else if (powerup.getCurrentCoordinate().getY() > windowHeight){
            powerup = null;
        }else{
            powerup.move();
            hasAnythingMovedOrChanged = true;
        }
    }


    /***********************************************************************************************************************
     * For a hit to be detected four conditions must be fulfilled, one for each side of the powerup. All we know is that we*
     * have a hit, the function below detects on which side the hit was depending on these conditions. For example, if the *
     * powerup hits the paddle from above the constraints are as follows:                       *
     *
     * 1. The lower side of the powerup must be inside the paddle.
     *
     * 2. The upper side of the powerup must be above the block. This is pretty self-explanatory, but is necessary to avoid*
     *    false positives when we search for a hit with the paddle from below.
     *
     * 3. The right side of the powerup must be to the right of the paddle's left side.
     *
     * 4. In a similar way, the left side of the powerup must be to the left of the paddles's right side.                  *
     *                                                                                                                     *
     * 3 and 4 are there to avoid false hits when the powerup is passing to the right or left of the paddle. All other
     * directions are checked according to the same principle. Since the direction of the hit doesn't matter in this case  *
     * we simply try every case right after another to see if anything gives a hit.                                        *
     * *********************************************************************************************************************/
    private boolean  powerupCollidesWithPaddle(){
        Coordinate powerupCoord = powerup.getNextCoordinate();
        Coordinate paddleCoord = paddle.getCurrentCoordinate();
        //Conditions are (upper edge inside paddle && lower edge below paddle && left edge left of paddle's right && right edge right of paddle's right)
        if (paddleCoord.getY()+paddle.getHeight() > powerupCoord.getY() && powerupCoord.getY() + powerup.getSize() > paddleCoord.getY()+paddle.getLength() &&
                powerupCoord.getX() < paddleCoord.getX()+paddle.getLength() && powerupCoord.getX()+powerup.getSize()> paddleCoord.getX()){
            return true;
        }
        //Conditions are (lower edge inside paddle && upper edge above paddle && left edge left of paddle's right && right edge right of paddle's right)
        if (powerupCoord.getY()+powerup.getSize() > paddleCoord.getY() && powerupCoord.getY() < paddleCoord.getY() &&
                powerupCoord.getX() < paddleCoord.getX()+paddle.getLength() && powerupCoord.getX()+powerup.getSize() > paddleCoord.getX()){
            return true;
        }
        //Conditions are (right edge inside paddle && left edge left of paddle && upper edge above paddle's lower && lower edge below paddle's upper)
        if (powerupCoord.getX()+powerup.getSize() > paddleCoord.getX() && powerupCoord.getX() < paddleCoord.getX() &&
                powerupCoord.getY()+1 < paddleCoord.getY() + paddle.getHeight() && powerupCoord.getY()+powerup.getSize()-1 > paddleCoord.getY()){
            return true;
        }
        //Conditions are (left edge inside paddle && right edge right of paddle && upper edge above paddle's lower && lower edge below paddle's upper)

        if (powerupCoord.getX() < paddleCoord.getX() + paddle.getLength() && powerupCoord.getX()+powerup.getSize() > paddleCoord.getX()+paddle.getLength() &&
                powerupCoord.getY()+1 < paddleCoord.getY() + paddle.getHeight() && powerupCoord.getY()+powerup.getSize()-1 > paddleCoord.getY()){
            return true;
        }
        return false;
    }

    private void executePowerup(){
        switch (powerup.getPowerupType()){
            case LONGER_PADDLE:
                paddle.makeLong();
                if (paddle.getCurrentCoordinate().getX()+paddle.getLength()>windowWidth){
                    //If the paddle moves out of the window when it extends it gets moved back until it fits.
                    paddle.getCurrentCoordinate().moveCoord(windowWidth-(paddle.getCurrentCoordinate().getX()+paddle.getLength()), 0);
                }
                break;
            case SHORTER_PADDLE:
                paddle.makeShort();
                break;
            case EXTRA_LIFE:
                lives++;
                break;
            case EXTRA_POINTS:
                score += 1000;
                break;
            default:
                break;
        }
    }

    public int getPowerupSize(){
        return powerup.getSize();
    }

    public Boolean powerupExists(){
        return powerup != null;
    }

    /*If there's currently no powerup on the board it will be assigned to the powerup the current block has. This may
    * be null if the block doesn't contain a powerup. Therefor another check is made immediately after, and if there is
    * a powerup it will set the starting coordinates to be centered right below the block.*/
    private void newPowerup(Coordinate arrayPos){
        Powerup newPowerup = brickBoard.getPowerup(arrayPos.getX(), arrayPos.getY());
        if (powerup == null && newPowerup != null){
            powerup = newPowerup;
            powerup.setCoordinate(new Coordinate(X_PADDING+brickBoard.getBrickWidth()*arrayPos.getX()+(brickBoard.getBrickWidth()-powerup.getSize())/2,
                                                 Y_PADDING+brickBoard.getBrickHeight()*(arrayPos.getY()+1)));
            hasAnythingMovedOrChanged = true;
        }
    }

    public Powerup getPowerup(){
        return powerup;
    }


    /*************************************
     *Functions for brickboard and bricks*
     *************************************/

    public void randomBoard(){
        brickBoard.randomBoard();
        notifyListeners();
    }

    public int getBrickBoardWidth(){
        return brickBoard.getBoardWidth();
    }

    public int getBrickBoardHeight(){
        return brickBoard.getBoardHeight();
    }

    private boolean brickBoardPosOutOfBounds(Coordinate c){
        return (c.getX()<0 || c.getX()>=brickBoard.getBoardWidth() || c.getY()<0 || c.getY()>=brickBoard.getBoardHeight());
    }


    /*********************
    *Functions for bricks*
    **********************/
    public int getBrickHeight(){
        return brickBoard.getBrickHeight();
    }

    public int getBrickWidth(){
        return brickBoard.getBrickWidth();
    }

    public Brick getBrick (int x, int y){
        return brickBoard.getBrick(x, y);
    }

    /*If this function is called we already know there's a brick in this spot, so we simply fetch the (possible) powerup
    * and tries to downgrade the block. If we're successful we return true, if we don't we return false.*/
    private Boolean downgradeBrick(Coordinate arrayPos){
        newPowerup(arrayPos);
        if (brickBoard.downgradeBrick(arrayPos.getX(), arrayPos.getY())){
            return true;
        }
        return false;
    }


    /**********************
     *Functions for editor*
     **********************/
    public void enterEditor(){
        loadLevels();
        brickBoard = levelArray.getNextLevel();
        creatorPhase = true;
        levelAdded = false;
        typeArrayPosition = 0;
        notifyListeners();
    }

    public void editorNextLevel(){
        if (levelArray.nextLevelExists()){
            brickBoard = levelArray.getNextLevel();
        }else{
            brickBoard = new BrickBoard();
        }
        notifyListeners();
    }

    public void editorPreviousLevel(){
        brickBoard = levelArray.getPreviousLevel();
        notifyListeners();

    }

    public void addBoard(){
        levelArray.addLevel(brickBoard);
        levelAdded = true;
        notifyListeners();
    }

    public void clearBoard(){
        brickBoard.clearBoard();
        if(levelAdded)
            levelAdded=false;
        notifyListeners();
    }

    public Boolean isInEditor(){
        return creatorPhase;
    }

    public boolean isLevelAdded(){
        return levelAdded;
    }

    public Brick getCurrentCreatorBrick(){
        return currentCreatorBrick;
    }

    public void nextBrick(){
        increaseArrayPosition();
        if(typeArray[typeArrayPosition] == Type.EMPTY){
            nextBrick();
        }else{
            currentCreatorBrick = brickBoard.newBrick(typeArray[typeArrayPosition]);
            notifyListeners();
        }
    }

    private void increaseArrayPosition(){
        if (typeArrayPosition < typeArray.length-1) //If we're not at the end of the array we increase the position,
            typeArrayPosition++;                    //otherwise set the position to the beginning of the array.
        else
            typeArrayPosition = 0;
    }

    private void decreaseArrayPosition(){
        if (typeArrayPosition > 0) //If we're not at the beginning of the array we decrease the position, otherwise
            typeArrayPosition--;   //we set the position to the end of the array.
        else
            typeArrayPosition = typeArray.length-1;
    }

    public void previousBrick(){
        decreaseArrayPosition();
        if(typeArray[typeArrayPosition] == Type.EMPTY){
            previousBrick();
        }else{
            currentCreatorBrick = brickBoard.newBrick(typeArray[typeArrayPosition]);
            notifyListeners();
        }
    }

    public void insertCurrentCreatorBrickToCoordinate(Coordinate coordinate){
        Coordinate arrayPos = coordinateToArrayPosition(coordinate);
        if (!brickBoardPosOutOfBounds(arrayPos)){
            brickBoard.insertBrick(brickBoard.newBrick(currentCreatorBrick.getCurrentType()), arrayPos.getX(), arrayPos.getY());
            if(levelAdded)
                levelAdded = false;
            notifyListeners();
        }
    }

    public void removeCreatorBrickFromBoard(Coordinate coordinate){
        Coordinate arrayPos = coordinateToArrayPosition(coordinate);
        if (!brickBoardPosOutOfBounds(arrayPos)){
            brickBoard.removeBrick(arrayPos.getX(), arrayPos.getY());
            if (levelAdded)
                levelAdded = false;
            notifyListeners();
        }
    }


    /*************************
     *Functions for gameboard*
     *************************/
    public void newGame(){
        paused= false;
        creatorPhase = false;
        gameover = false;
        paddle = new Paddle();
        if (ball != null)
            killBall();
        if (powerupExists())
            powerup = null;
        paddle.setCoordinate(new Coordinate((windowWidth-paddle.getLength())/2, windowHeight-50));
        paddleMoveRight = false;
        paddleMoveLeft = false;
        loadLevels();
        brickBoard = levelArray.getNextLevel();
        score = 0;
        lives = 3;
        notifyListeners();
    }

    public boolean isPaused(){
       return paused;
    }

    public void pause(){
        notifyListeners();
        paused = true;
    }

    public void unPause(){
        paused = false;
    }

    public void saveLevels(){
        levelArray.saveLevels();
    }

    public void loadLevels(){
       levelArray.loadLevels();
   }

    public int getCurrentLevel(){
        return levelArray.getCurrentLevel();
    }

    public boolean gameOver(){
        return gameover;
    }

    public String getLives(){
        return "Lives: " + Integer.toString(lives);
    }

    public String getScore(){
        return "Score: " + Integer.toString(score);
    }

    public int getXPadding(){
        return X_PADDING;
    }

    public int getYPadding(){
        return Y_PADDING;
    }

    public void increaseScore(){
        score += 100;
    }

    private boolean stageClear(){
        if (brickBoard.isEmpty()){
            killBall();
            brickBoard = levelArray.getNextLevel();
            return true;
        }
        return false;
    }

    public int getWindowWidth(){
        return windowWidth;
    }

    public int getWindowHeight(){
        return windowHeight;
    }


    /*************************
     *Functions for listeners*
     *************************/
    public void addBoardListener(BoardListener bl){
        listeners.add(bl);
    }

    private void notifyListeners(){
        BoardListener temp;
        if (listeners == null){
            return;
        }
        for(int i = 0; i < listeners.size(); i++){
            temp = (BoardListener) listeners.get(i);
            temp.boardChanged();
        }
    }
}

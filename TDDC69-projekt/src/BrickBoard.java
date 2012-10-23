import java.awt.*;
import java.io.Serializable;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-09-27
 * Time: 16:04
 * To change this template use File | Settings | File Templates.
 */
public class BrickBoard implements Serializable {
    private int brickWidth = 55;
    private int brickHeight = 20;
    private int boardWidth = 15;
    private int boardHeight = 10;
    private Brick[][] brickArray;
    private Type[] typeArray = Type.values();


    public BrickBoard() {
        this.brickArray = new Brick[boardHeight][boardWidth];
    }

    public int getBrickWidth() {
        return brickWidth;
    }

    public void clearBoard(){
        for(int y=0; y<this.boardHeight; y++){
            for(int x=0; x<this.boardWidth;x++){
                removeBrick(x, y);
            }
        }
    }

    public boolean isEmpty(){
        for(int y=0; y<this.boardHeight; y++){
            for(int x=0; x<this.boardWidth;x++){
                if(getType(x,y) != null){
                    if(getType(x,y) != Type.UNCHANGEABLE)
                        return false ;
                }
            }
        }
        return true;
    }

    public boolean downgradeBrick(int x, int y){
        Type tempType = brickArray[y][x].downgradedBrickType();
        insertBrick(newBrick(tempType), x, y);
        return (tempType != Type.UNCHANGEABLE);
    }

    public Powerup getPowerup(int x, int y){
       return brickArray[y][x].getPowerup();
    }

    public Brick newBrick(Type type){
        if (type == null)
            return null;
        switch (type){
            case POWERUP:
                return new PowerupBrick();
            case ONE_HIT:
                return new OneHitBrick();
            case TWO_HIT:
                return new TwoHitBrick();
            case THREE_HIT:
                return new ThreeHitBrick();
            case UNCHANGEABLE:
                return new IndesctructibleBrick();
            case EMPTY:
                return null;
            default:
                return null;
        }
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public int getBrickHeight() {
        return brickHeight;
    }

    public Type getType(int x, int y){
        if (brickArray[y][x] == null)
            return null;
        return brickArray[y][x].getCurrentType();
    }

    public Brick getBrick(int x, int y){
        return brickArray[y][x];
    }
    public void insertBrick(Brick brick ,int x, int y){
       brickArray[y][x] = brick;
   }

    public void removeBrick (int x, int y){
        brickArray[y][x] = null;
    }

    public void randomBoard(){
        Random random = new Random();
        int randomint;
        Type tempType;
        for(int y=0; y<this.boardHeight; y++){
           for(int x=0; x<this.boardWidth;x++){
               randomint = random.nextInt(typeArray.length);
               tempType = typeArray[randomint];
               if (tempType == Type.POWERUP){ //If there's a powerup it will randomize a new type, making powerups less likely to appear
                   randomint = random.nextInt(typeArray.length);
                   tempType = typeArray[randomint];
               }
               insertBrick(newBrick(tempType), x, y);
           }
        }
    }

    public Brick copyBrick(Type type){
        return newBrick(type);
    }

    public void copyBoard(BrickBoard board){
        for(int y=0; y<this.boardHeight; y++){
            for(int x=0; x<this.boardWidth;x++){
                this.insertBrick(copyBrick(board.getType(x, y)), x, y);
            }
        }
    }

}

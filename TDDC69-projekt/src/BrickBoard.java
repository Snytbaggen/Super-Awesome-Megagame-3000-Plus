import java.awt.*;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-09-27
 * Time: 16:04
 * To change this template use File | Settings | File Templates.
 */
public class BrickBoard {
    private int brickWidth = 55;
    private int brickHeight = 20;
    private int boardWidth = 15;
    private int boardHeight = 10;
    private Brick[][] brickArray;
    private Type[] typeArray = Type.values();


    public BrickBoard() {
        this.brickArray = new Brick[boardHeight][boardWidth];
    }

    public Color getColor(int x, int y){
        if (brickArray[y][x] == null){
            return Color.BLACK;
        }
      return brickArray[y][x].getColor();
    }

    public Type getType(int x, int y){
        return brickArray[y][x].getType();
    }

   public void insertBrick(Brick brick,int x, int y){
       brickArray[y][x] = brick;
   }
    public void removeBrick (int x, int y){
        brickArray[y][x] = null;
    }

    public void randomBoard(){
        Random random = new Random();
        int randomint;
        for(int y=0; y<this.boardHeight; y++){
           for(int x=0; x<this.boardWidth;x++){
               randomint = random.nextInt(typeArray.length+1);
               if (randomint == typeArray.length){
                   insertBrick(null, x, y);
               } else{
                   insertBrick(new Brick(typeArray[randomint]), x, y);
               }
           }
        }

    }
}

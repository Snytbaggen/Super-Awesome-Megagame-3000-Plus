import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-09-27
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */
public class Brick {
  //  private final int WIDTH=30;
  //  private final int HEIGHT= 10;
    private Type type;
public Brick(Type type){
 this.type = type;
}

  public Type getType(){
      return type;
  }

    public Color getColor(){
        return type.getColor();
    }

   /* public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
    }*/

}

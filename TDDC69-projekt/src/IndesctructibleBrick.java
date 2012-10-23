import java.awt.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-10-17
 * Time: 12:04
 * To change this template use File | Settings | File Templates.
 */
public class IndesctructibleBrick implements Brick, Serializable {
    private Color brickColor;
    private Type type;

    public Type getCurrentType(){
        return type;
    }

    public IndesctructibleBrick(){
        type = Type.UNCHANGEABLE;
        brickColor = Color.GRAY;
    }

    public Color getColor() {
        return brickColor;
    }

    public Powerup getPowerup() {
        return null;
    }

    @Override
    public Type downgradedBrickType() {
        return Type.UNCHANGEABLE;
    }
}

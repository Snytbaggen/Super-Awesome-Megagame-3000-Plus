import java.awt.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-10-17
 * Time: 11:30
 * To change this template use File | Settings | File Templates.
 */
public class OneHitBrick implements Brick, Serializable {
    private Color brickColor;
    private Type type;

    public Type getCurrentType(){
        return type;
    }

    public OneHitBrick(){
        brickColor = Color.GREEN;
        type = Type.ONE_HIT;
    }

    public Color getColor() {
        return brickColor;
    }

    public Powerup getPowerup() {
        return null;
    }

    @Override
    public Type downgradedBrickType() {
        return Type.EMPTY;
    }
}

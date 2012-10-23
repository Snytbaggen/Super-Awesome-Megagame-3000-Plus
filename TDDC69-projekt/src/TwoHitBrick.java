import java.awt.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-10-17
 * Time: 11:38
 * To change this template use File | Settings | File Templates.
 */
public class TwoHitBrick implements Brick, Serializable {
    private Color brickColor;
    private Type type;

    public Type getCurrentType(){
        return type;
    }

    public TwoHitBrick(){
        type = Type.TWO_HIT;
        brickColor = Color.YELLOW;
    }

    public Color getColor() {
        return brickColor;
    }

    public Powerup getPowerup() {
        return null;
    }

    @Override
    public Type downgradedBrickType() {
        return Type.ONE_HIT;
    }
}

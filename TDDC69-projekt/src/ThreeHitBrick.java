import java.awt.*;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-10-17
 * Time: 11:41
 * To change this template use File | Settings | File Templates.
 */
public class ThreeHitBrick implements Brick, Serializable {
    private Color brickColor;
    private Type type;

    public Type getCurrentType(){
        return type;
    }

    public ThreeHitBrick(){
        type = Type.THREE_HIT;
        brickColor = Color.RED;
    }

    public Color getColor() {
        return brickColor;
    }

    public Powerup getPowerup() {
        return null;
    }

    @Override
    public Type downgradedBrickType() {
        return Type.TWO_HIT;
    }
}

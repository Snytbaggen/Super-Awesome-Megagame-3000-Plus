import java.awt.*;
import java.io.Serializable;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-10-17
 * Time: 12:04
 * To change this template use File | Settings | File Templates.
 */
public class PowerupBrick implements Brick, Serializable {
    private Color brickColor;

    private Powerup powerup;
    private Type type;

    public Type getCurrentType(){
        return type;
    }

    public PowerupBrick(){
        type = Type.POWERUP;
        brickColor = Color.BLUE;
        powerup = new Powerup();
    }

    public Color getColor() {
        return brickColor;
    }

    public Powerup getPowerup() {
        return powerup;
    }

    @Override
    public Type downgradedBrickType() {
        return Type.EMPTY;
    }
}

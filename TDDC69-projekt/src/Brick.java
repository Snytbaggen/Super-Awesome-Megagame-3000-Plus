import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-09-27
 * Time: 15:29
 * To change this template use File | Settings | File Templates.
 */
public interface Brick {
    public Color getColor();
    public Type getCurrentType();
    public Powerup getPowerup();
    public Type downgradedBrickType();
}

import com.sun.javafx.scene.text.HitInfo;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-09-27
 * Time: 15:45
 * To change this template use File | Settings | File Templates.
 */
public enum Type {
    ONE_HIT (Color.GREEN),
    TWO_HIT (Color.YELLOW),
    THREE_HIT (Color.RED),
    INDESTRUCTIBLE(Color.GRAY);

    private final Color color;
    Type(Color color){
        this.color = color;
    }

    Color getColor(){
        return color;
    }

}
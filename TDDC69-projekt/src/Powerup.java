import java.io.Serializable;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-10-18
 * Time: 23:02
 * To change this template use File | Settings | File Templates.
 */
public class Powerup implements Serializable {
    private final int size = 25;
    private final int xSpeed = 0;
    private final int ySpeed = 4;
    private Coordinate currentCoordinate;
    private PowerupTypes powerupType;

    public Powerup(){
        currentCoordinate = new Coordinate(0, 0);
        Random random = new Random();
        PowerupTypes[] powerupList = PowerupTypes.values();
        int powerupNumber = random.nextInt(powerupList.length);
        if (powerupList[powerupNumber] == PowerupTypes.EXTRA_LIFE){
            powerupNumber = random.nextInt(powerupList.length);
        }
        powerupType = powerupList[powerupNumber];
    }

    public void move(){
        currentCoordinate.moveCoord(xSpeed, ySpeed);
    }

    public void setCoordinate(Coordinate newCoordinate){
        currentCoordinate = newCoordinate;
    }

    public Coordinate getCurrentCoordinate(){
        return currentCoordinate;
    }

    public Coordinate getNextCoordinate(){
        return new Coordinate(currentCoordinate.getX() + xSpeed,
                              currentCoordinate.getY() + ySpeed);
    }

    public PowerupTypes getPowerupType(){
        return powerupType;
    }

    public int getSize(){
        return size;
    }

}

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-09-27
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public class Ball {
    private int size;
    private int xSpeed;
    private int ySpeed;

    public Ball() { //creates a ball that stands still
        this.size = 15;
        this.xSpeed = 0;
        this.ySpeed = 0;
    }

    public void setSpeed(int xSpeed, int ySpeed){
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public int getSize() {
        return size;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }
}

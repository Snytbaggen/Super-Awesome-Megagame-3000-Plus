/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-10-16
 * Time: 12:00
 * To change this template use File | Settings | File Templates.
 */
public enum PaddleLengths{
    LONG(160),
    SHORT(60),
    DEFAULT(110);

    private final int length;

    PaddleLengths(int l){
        length = l;
    }

    int getLength(){
        return length;
    }
}

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-10-15
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */
public class GameSound implements Runnable {
    Sequence sequence;
    Sequencer sequencer;
    boolean isRunning;


    public void run(){
        try{
            sequence = MidiSystem.getSequence(new File("sound/bgmusic.mid"));
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.setSequence(sequence);
            sequencer.setLoopCount(999);
            startMusic();
            isRunning = true;
        }catch (IOException e){
        }catch (InvalidMidiDataException e){
        }catch (MidiUnavailableException e){
        }
    }

    public void startMusic(){
        sequencer.start();
        isRunning = true;
    }

    public void stopMusic(){
        sequencer.stop();
        isRunning = false;
    }

    public boolean isRunning(){
        return isRunning;
    }

}

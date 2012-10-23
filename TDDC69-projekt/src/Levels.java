import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel
 * Date: 2012-10-08
 * Time: 11:21
 * To change this template use File | Settings | File Templates.
 */
public class Levels {
    ArrayList<BrickBoard> levels;
    int currentLevel;

    public Levels(){
        levels = new ArrayList<BrickBoard>();
        }

    public void addLevel(BrickBoard board){
        BrickBoard newBoard = new BrickBoard();
        newBoard.copyBoard(board);
        levels.add(newBoard);
    }

    public BrickBoard getNextLevel(){
        if (nextLevelExists()){
            currentLevel++;
            return levels.get(currentLevel-1);
        }else{
            BrickBoard brickBoard = new BrickBoard();
            brickBoard.randomBoard();
            return brickBoard;
        }
    }

    public int getCurrentLevel(){
        return currentLevel;
    }

    public boolean nextLevelExists(){
        return currentLevel<levels.size();
    }

    public boolean previousLevelExists(){
        return currentLevel>0;
    }

    public BrickBoard getPreviousLevel(){
        if(previousLevelExists())
            currentLevel--;
        return levels.get(currentLevel);
    }

    public void saveLevels(){
        try{
            final ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream("levels.dat"));
            fos.writeObject(levels);
            fos.close();
            }catch (IOException e){
            e.printStackTrace();
        }
    }


    public void loadLevels(){
        try{
            final ObjectInputStream ois = new ObjectInputStream(new FileInputStream("levels.dat"));
            Object obj = ois.readObject();
            if (obj instanceof ArrayList){
                levels = (ArrayList) obj;
                currentLevel = 0;
            }
            ois.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
        }
    }
}

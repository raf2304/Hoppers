package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.File;
import java.io.IOException;
import java.util.*;
/**
 * The representation of a hopper model
 *
 * @author Ryleigh Fuller
 */
public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;
    /** row, col to move from*/
    private int[] selectFrom;
    /** row, col to move to*/
    private int[] selectTo;
    private final String filename;


    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    /**
     *
     * @return the current configuration
     */
    public HoppersConfig getCurrentConfig(){
        return this.currentConfig;
    }

    /**
     * Give a hint to the solution
     * If a solution at the current configuration is possible,
     * advance to the next step as indicated from solver.
     * If not, let the player know there is no solution possible.
     */
    public void hint()
    {
        //check if this current configuration is a solution
        if(this.currentConfig.isSolution()){
            System.out.println("\n");
            this.alertObservers("Puzzle already solved!");
        }else{
            //create a solver object
            Solver solver = new Solver();
            //get path from solver from current config, this will find a path or tell us it is not solvable
            Collection<Configuration> path = solver.solve(this.currentConfig);
            //if there is a path, advance to the next step after current
            if(path != null){
                Object[] arrPath = path.toArray();
                this.currentConfig = (HoppersConfig)arrPath[arrPath.length - 2];
                this.alertObservers( "Next step!");

            }else{
                //no path, indicate there is no solution
                System.out.println("\n");
                this.alertObservers("No solution");
            }
        }

    }
    /**
     * Load a new HoppersModel
     * @param filename the model to load
     */
    public void load(String filename){
        try {
            this.currentConfig = new HoppersConfig(filename);
            File f = new File(filename);
            this.alertObservers("Loaded: " + f.getName());
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }

    }

    /**
     * select a cell at r, c
     * @param r the row selected
     * @param c the col selected
     */
    public void select(int r, int c){
        boolean valid = true;
        if(!this.currentConfig.isSolution()){
            //if r and c are on the board
            if(r >= 0 && c >= 0 && r < this.currentConfig.getRow() && c < this.currentConfig.getCol()){
                String value = this.currentConfig.getBoard()[r][c];
                //if there is a frog at boards row, col and select from is not set
                if(!Objects.equals(value, ".") && !Objects.equals(value, "*") && selectFrom == null){
                    selectFrom = new int[2];
                    selectFrom[0] = r;
                    selectFrom[1] = c;
                    this.alertObservers("Selected (" + r + ", " + c + ").");

                }else if((Objects.equals(value, ".") || Objects.equals(value, "*")) && selectFrom == null){
                    //if the first selection is a "." or "*", inform player there is no frog at that location
                    valid = false;
                    this.alertObservers("No frog at (" + r + ", " + c + ").");
                }else if(Objects.equals(value, ".") && selectFrom != null){
                    //if select from is set and the selection is a valid move spot
                    selectTo = new int[2];
                    selectTo[0] = r;
                    selectTo[1] = c;
                    //check if the move is a valid move, is the difference two
                    if(Math.abs(selectTo[0]-selectFrom[0]) == 2){
                        if(Math.abs(selectTo[1]-selectFrom[1]) == 2){
                            //make the move and update the board
                            makeMove();
                            this.alertObservers("Jumped from (" + selectFrom[0] + ", " + selectFrom[1] + ") to (" + r + ", " + c + ").");

                            selectFrom = null;
                            selectTo = null;
                        }
                    }else{
                        valid = false;
                        this.alertObservers("The spot at (" + r + ", " + c + ") is not two away from original selection.");
                    }
                }else if(!Objects.equals(value, ".") && selectFrom != null){
                    //if the next selection is not a valid moving spot, inform the player
                    valid = false;
                    this.alertObservers("The spot at (" + r + ", " + c + ") is not a valid move. Either there is a frog, or the spot is invalid.");
                }
            }else{
                valid = false;
                this.alertObservers("The coordinates chosen are off the board. Selection cancelled.");
            }
            if(!valid){
                selectFrom = null;
                selectTo = null;
                this.alertObservers("Not a valid move. Selection cancelled.");
            }
        }else{
            this.alertObservers("Current board is a solution!");
        }


    }

    /**
     * Adjust the board for the move being make in select()
     */
    public void makeMove(){
        //get the frog (r or g) at the original selected spot
        String frog = this.currentConfig.getBoard()[selectFrom[0]][selectFrom[1]];
        //the starting selection is now empty
        this.currentConfig.getBoard()[selectFrom[0]][selectFrom[1]] = ".";
        //the spot the frog is moved to is now set to frog
        this.currentConfig.getBoard()[selectTo[0]][selectTo[1]] = frog;
        //the spot skipped over is now empty
        this.currentConfig.getBoard()[((selectTo[0] + selectFrom[0])/2)][(selectTo[1] + selectFrom[1]) / 2] = ".";
    }

    /**
     * reset the current game
     */
    public void reset() throws IOException {
        this.currentConfig = new HoppersConfig(this.filename);
        File f = new File(filename);
        this.alertObservers("Loaded: " + f.getName());
        this.alertObservers( "Puzzle reset!");
    }

    /**
     * create a new HoppersModel
     * @param filename the model to load
     * @throws IOException due to file
     */
    public HoppersModel(String filename) throws IOException {
        this.currentConfig = new HoppersConfig(filename);
        this.filename = filename;
    }
}

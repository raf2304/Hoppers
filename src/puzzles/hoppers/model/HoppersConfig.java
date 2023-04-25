package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * The representation of a hopper configuration
 *
 * @author Ryleigh Fuller
 */

public class HoppersConfig implements Configuration{
    private final int cursorRow;
    private  final int cursorCol;
    private final int row;
    private final int col;
    private final String[][] board;
    /**
     Create the initial Hoppers configuration
     @param filename the file to get information from
     */
    public HoppersConfig(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        //the first line will contrain the rows/cols of the grid
        String[] rowsCols = br.readLine().split(" ");
        this.row = Integer.parseInt(rowsCols[0]);
        this.col = Integer.parseInt(rowsCols[1]);
        //create the board with row and col
        this.board = new String[this.row][this.col];
        //populate the board
        for(int i = 0; i < this.row; i++){
            String[] rowString = br.readLine().split(" ");
            for(int j = 0; j < this.col; j++){
                this.board[i][j] = rowString[j];
            }
        }
        //cursor off the board
        this.cursorRow = 0;
        this.cursorCol = -1;
    }

    /**
     * Create a copy config
     * @param other configuration to copy from
     * @param row row of cursor
     * @param col col of cursor
     */
    public HoppersConfig(HoppersConfig other, int row, int col){
        //copy over size
        this.row = other.row;
        this.col = other.col;
        //set cursor loc
        this.cursorRow = row;
        this.cursorCol = col;
        //create the new board with row and col
        this.board = new String[this.row][this.col];
        //copy the information
        for(int i = 0; i < this.row; i++){
            for(int j = 0; j < this.col; j++){
                if(other.board[i][j] == "R"){
                    this.board[i][j] = ".";
                }else{
                    this.board[i][j] = other.board[i][j];
                }

            }
        }
    }

    /**
     *
     * @return list of successors, valid and not valid
     */
    public Collection<Configuration> getSuccessors(){
        //create a list of successors
        List<Configuration> successors = new LinkedList<Configuration>();
        HoppersConfig child = new HoppersConfig(this, this.cursorRow, this.cursorCol);
        /*
        * * spots can be either R or *
        * - spots can be R, G, -
        * G spots can be R, G, -
         */
        //if child is a "*"
        if(!child.isSolution() && Objects.equals(child.board[this.cursorRow][this.cursorCol], "*")){
            child.board[child.cursorRow][child.cursorCol] = "*";
            successors.add(child);
            //add two configurations. One is a "*", one is R
            HoppersConfig child2 = new HoppersConfig(this, this.cursorRow, this.cursorCol);
            child2.board[child.cursorRow][child.cursorCol] = "R";
            successors.add(child2);

        }

        return successors;
    }
    /**
     is this value equal to the goal value? Are we at the last possible value?
     @return if this value equals the ending value
     */
    @Override
    public boolean isSolution() {
        if((this.cursorRow == this.row) && (this.cursorCol == this.col)){
            return true;
        }
        return false;
    }

    /**
     @return the list of the HoppersConfig neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        return null;
    }
}

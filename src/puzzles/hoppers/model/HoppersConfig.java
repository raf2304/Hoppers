package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
/**
 * create each move for each frog
 * choose which ones are valid
 * based on which R/G is two away from
 * original R/G
 * when moving R/G, change row/col-1 to a -
 */

/**
 * The representation of a hopper configuration
 *
 * @author Ryleigh Fuller
 */

public class HoppersConfig implements Configuration{
    //the row dimension
    private final int row;
    //the col dimension
    private final int col;
    //the game board
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
        System.out.println(this.toString());
    }

    /**
     * Create a copy config
     * @param other configuration to copy from
     * @param row row of cursor
     * @param col col of cursor
     */
    public HoppersConfig(HoppersConfig other, int row, int col){
        //copy over size
        this.col = other.col;
        this.row = other.row;
        //create the new board with row and col
        this.board = new String[this.row][this.col];
        //copy the information
        for(int i = 0; i < this.row; i++){
            for(int j = 0; j < this.col; j++){
                this.board[i][j] = other.board[i][j];
            }
        }
    }

    /**
     * Check if the configuration is a valid configuration when compared to this
     * @param config the configuration to check
     * @return true if valid config, false otherwise
     */
    public boolean isValid(HoppersConfig config){
        //is R two away from the original location?
        //is there one R on the board
        //is a green frog removed
        //has one green frog been removed
        return false;
    }

    /**
     is this value equal to the goal value? Are we at the last possible value?
     @return if this value equals the ending value
     */
    @Override
    public boolean isSolution() {
        return false;
    }
    /**
     * @return list of successors, valid and not valid
     */
    public Collection<Configuration> getSuccessors(){
        Collection<Configuration> successors = new ArrayList<>();
        for(int r = 0; r < this.row; r++){
            for(int c = 0; c < this.col; c++){
                //if board loc at row, col is a frog
                if(Objects.equals(this.board[r][c], "G") || Objects.equals(this.board[r][c], "R")){
                    //if the coordinates are even, call the even method and add results to  successors
                    if(r % 2 == 0 && c % 2 == 0){
                        successors.addAll(getEvenConfig(this, r, c));
                    }else{
                        //if coordinates are odd, call the odd method
                        successors.addAll(getOddConfig(this, r, c));
                    }
                }
            }
        }
        return successors;
    }

    /**
     * Generates the successors for even coordinates on the board
     * checks the 8 possible configurations at an even loc
     * @param config the current config
     * @param r the row with the item that will be moved
     * @param c the col with the item that will be moved
     * @return collection of valid configurations
     */
    public Collection<Configuration> getEvenConfig(HoppersConfig config, int r, int c){
        //hold successors generated
        Collection<Configuration> successors = new ArrayList<>();
        //get frog value at location
        String frog = this.board[r][c];
        //for each possible location, check if it is valid and if there is a frog that
        //is being passed over to get to that location
        //lists contain all possible row/col values to check
        List<Integer> isValidRowCheck = new ArrayList<>(Arrays.asList(4, -4, 0, 0, 2, -2, -2, 2));
        List<Integer> isValidColCheck = new ArrayList<>(Arrays.asList(0, 0, -4, 4, 2, 2, -2, -2));
        //lists contain all possible row/col values that will be passed over
        List<Integer> frogRowCheck = new ArrayList<>(Arrays.asList(2,-2,0,0,1,-1,-1,1));
        List<Integer> frogColCheck = new ArrayList<>(Arrays.asList(0,0,-2,2,1,1,-1,-1));
        //since row/col is even, there are eight possibilities
        for(int i = 0; i < 8; i++){
            int rowNumFrog = isValidRowCheck.remove(0);
            int rowNumEmpty = frogRowCheck.remove(0);
            int colNumFrog = isValidColCheck.remove(0);
            int colNumEmpty = frogColCheck.remove(0);
            if(isValid(r+rowNumFrog,c+colNumFrog) && frogCheck(r+rowNumEmpty,c+colNumEmpty)) {
                //create a new HoppersConfig
                HoppersConfig successor= new HoppersConfig(this, this.row, this.col);
                successor.board[r + rowNumEmpty][c+colNumEmpty] = ".";
                successor.board[r + rowNumFrog][c + colNumFrog] = frog;
                successor.board[r][c]=".";
                successors.add(successor);
            }
        }
        return successors;
    }

    /**
     * check if the row, col value that is being passed over is a green frog
     * @param r the row to check
     * @param c the value to check
     * @return true if there is a green frog there, false otherwise
     */
    private boolean frogCheck(int r, int c) {
        /*
        if row and col are greater than zero and less than their max values
        AND the object at row,col is a green frog
         */
        return r >= 0 && c >= 0 && r < this.row && c < this.col && Objects.equals(this.board[r][c], "G");
    }

    /**
     * check if the spot the frog is moving to is a valid place to move, indicated by a "-"
     * @param r the row to check
     * @param c the col to check
     * @return true if valid to move, false otherwise
     */
    private boolean isValid(int r, int c) {
        /*
        if row and col are greater than zero and less than their max values
        AND the object at row,col is a valid moving spot aka "-"
         */
        return r >= 0 && c >= 0 && r < this.row && c < this.col && Objects.equals(this.board[r][c], ".");
    }

    public Collection<Configuration> getOddConfig(HoppersConfig config, int r, int c){
        //hold successors generated
        Collection<Configuration> successors = new ArrayList<>();
        //get frog value at location
        String frog = this.board[r][c];
        //for each possible location, check if it is valid and if there is a frog that
        //is being passed over to get to that location
        //lists contain all possible row/col values to check
        List<Integer> isValidRowCheck = new ArrayList<>(Arrays.asList(2, -2, -2, 2));
        List<Integer> isValidColCheck = new ArrayList<>(Arrays.asList(2, 2, -2, -2));
        //lists contain all possible row/col values that will be passed over
        List<Integer> frogRowCheck = new ArrayList<>(Arrays.asList(1,-1,-1,1));
        List<Integer> frogColCheck = new ArrayList<>(Arrays.asList(1,1,-1,-1));
        //since row/col is odd, there are four possibilities
        for(int i = 0; i < 4; i++){
            int rowNumFrog = isValidRowCheck.remove(0);
            int rowNumEmpty = frogRowCheck.remove(0);
            int colNumFrog = isValidColCheck.remove(0);
            int colNumEmpty = frogColCheck.remove(0);
            if(isValid(r+rowNumFrog,c+ colNumFrog) && frogCheck(r+rowNumEmpty,c+colNumEmpty)) {
                //create a new HoppersConfig
                HoppersConfig successor= new HoppersConfig(this, this.row, this.col);
                successor.board[r + rowNumEmpty][c + colNumEmpty] = ".";
                successor.board[r + rowNumFrog][c + colNumFrog] = frog;
                successor.board[r][c]=".";
                successors.add(successor);
            }
        }
        return successors;

    }

    /**
     @return the list of the HoppersConfig neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        //list to hold each neighbor
        Collection<Configuration> successors = new ArrayList<>();
        //get all options
        //for each row, col if it is a R or G check every move
        //even spot, there are 8 moves
        //odd spots there are 4 moves
        //add each to list of successors
        //if the successor is valid, add it to the list of neighbors
        Collection<Configuration> neighbors = new ArrayList<>();
        return neighbors;
    }

    public String toString(){
        String boardStr = "";
        for(int i = 0; i < this.row; i++){
            for(int j = 0; j < this.col; j++){
                boardStr += this.board[i][j] + " ";
            }
            boardStr += "\n";
        }
        return boardStr;
    }

}

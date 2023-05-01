package puzzles.hoppers.gui;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;
/**
 * The representation of the hopper game as a GUI
 *
 * @author Ryleigh Fuller
 */
public class HoppersGUI extends Application implements Observer<HoppersModel, String> {
    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    /** the font size for labels and buttons */
    private final static int FONT_SIZE = 12;

    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // represents the red frog, R
    private final Image redFrog = new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "red_frog.png")));
    //represents the green frog, G
    private final Image greenFrog = new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "green_frog.png")));
    //represents a spot that can be moved to
    private final Image lillyPad = new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "lily_pad.png")));
    //represents a spot that cannot be moved to, *
    private final Image water = new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "water.png")));

    private HoppersModel model;
    //the stage
    private Stage stage;
    //the label for the state of the game
    private Label label;
    //holds the buttons at their row, col locations
    private Node[][] boardArr;
    //holds the current filename
    private String filename;


    /**
     * create the model, add ourselves as an observer of model
     */
    public void init() {
        String filename = getParameters().getRaw().get(0);
        try {
            this.model = new HoppersModel(filename);
            this.model.addObserver(this);
            this.filename = filename;
        } catch (IOException e) {
            System.out.println("Unable to locate file.");
        }

    }

    /**
     * Start the GUI
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception due to file
     */
    @Override
    public void start(Stage stage) throws Exception {
        //the stage
        this.stage = stage;

        //holds label, board, and other buttons
        VBox screen = new VBox();
        //initialize the label of the state of the game
        this.label = new Label("Loaded " + this.filename);
        this.label.setAlignment(Pos.CENTER);
        this.label.setFont(new Font(FONT_SIZE));
        //the playing board
        GridPane board = makeBoard();
        //the buttons load, reset, hint
        HBox btns = new HBox();
        Button load = new Button("Load");
        load.setOnAction(event -> {
            try {
                loadFile();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Button reset = new Button("Reset");
        reset.setOnAction(event -> {
            try {
                this.model.reset();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Button hint = new Button("Hint");
        btns.setAlignment(Pos.CENTER);
        hint.setOnAction(event -> this.model.hint());
        btns.getChildren().addAll(load, reset, hint);
        //add to VBox
        screen.getChildren().addAll(this.label, board, btns);
        //add VBox to scene
        Scene scene = new Scene(screen);
        stage.setScene(scene);
        //the title area shows the game name
        stage.setTitle("Hoppers GUI");
        stage.sizeToScene();
        stage.show();
    }

    /**
     * Load a new file using FileChooser
     */
    private void loadFile() {
        FileChooser chooser = new FileChooser();
        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
        currentPath += File.separator + "data" + File.separator + "hoppers";
        chooser.setInitialDirectory(new File(currentPath));
        this.model.load(currentPath);
    }

    /**
     * Make the initial board GUI based off
     * items at the models current configuration
     * @return GridPane of buttons representing each item on the board
     */
    private GridPane makeBoard() {
        GridPane grid = new GridPane();
        //size of the board
        int row = this.model.getCurrentConfig().getRow();
        int col = this.model.getCurrentConfig().getCol();
        //the game board of the current configuration
        String[][] board = this.model.getCurrentConfig().getBoard();
        this.boardArr = new Node[row][col];

        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                Button button = new Button();
                //if the board of current config is a *, the spot is not a valid moving spot
                //represented by water
                if(Objects.equals(board[r][c], "*")){
                    button.setGraphic(new ImageView(this.water));
                    //set the property type to the symbol associated with the image for later access
                    button.getProperties().put("TYPE","*");
                }else if(Objects.equals(board[r][c], ".")){
                    button.setGraphic(new ImageView(this.lillyPad));
                    button.getProperties().put("TYPE",".");
                }else if(Objects.equals(board[r][c], "G")){
                    button.setGraphic(new ImageView(this.greenFrog));
                    button.getProperties().put("TYPE","G");
                }else if(Objects.equals(board[r][c], "R")){
                    button.setGraphic(new ImageView(this.redFrog));
                    button.getProperties().put("TYPE","R");
                }
                button.setFont(new Font(FONT_SIZE));
                //add button to row, col of the arr, for later updating
                this.boardArr[r][c] = button;
                //set size of the buttons
                button.setMinSize(ICON_SIZE, ICON_SIZE);
                button.setMaxSize(ICON_SIZE, ICON_SIZE);
                //when button is pressed, select it for a move
                int finalR = r;
                int finalC = c;
                button.setOnAction(event -> this.model.select(finalR, finalC));
                grid.add(button, c, r);
            }
        }
        return grid;
    }

    /**
     * Update the screen based on changes in the model
     * @param hoppersModel the object that wishes to inform this object
     *                about something that has happened.
     * @param msg optional data the server. Model can send to the observer
     *
     */
    @Override
    public void update(HoppersModel hoppersModel, String msg){
        //if a new file has been loaded, start again
        if(msg.contains("Loaded:")){
            try{
                start(this.stage);
            } catch (Exception e) {
                System.out.println("Unable to locate file.");
            }

        }else{
            //set label text to the message
            label.setText(msg);
        }
        //set the label to the message sent, indicating state of the board
        //if a new file has been loaded
        if(msg.contains("Jumped")|| msg.equals("Puzzle reset!") || msg.contains("Next")){
            updateBoard();

        }
        this.stage.sizeToScene();  // when a different sized puzzle is loaded
    }

    /**
     * Update the board
     * Iterates through the models current configs row, col
     * and compares to buttonArr row, col.
     * A difference in the two changes the button image, type to represent the same character
     * as the String in the models current config row, col
     */
    private void updateBoard() {
        //size of the board
        int row = this.model.getCurrentConfig().getRow();
        int col = this.model.getCurrentConfig().getCol();
        //the game board of the current configuration
        String[][] board = this.model.getCurrentConfig().getBoard();

        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                Button button = (Button) boardArr[r][c];
                //if the button property at r, c type is not equal to a symbol
                //but the boards r, c is, change the button to the correct graphic
                if(!button.getProperties().get("TYPE").equals("*") && Objects.equals(board[r][c], "*")){
                    button.setGraphic(new ImageView(this.water));
                    button.getProperties().put("TYPE","*");
                }else if(!button.getProperties().get("TYPE").equals(".") && Objects.equals(board[r][c], ".")){
                    button.setGraphic(new ImageView(this.lillyPad));
                    button.getProperties().put("TYPE",".");
                }else if(!button.getProperties().get("TYPE").equals("G") && Objects.equals(board[r][c], "G")){
                    button.setGraphic(new ImageView(this.greenFrog));
                    button.getProperties().put("TYPE","G");
                }else if(!button.getProperties().get("TYPE").equals("R") && Objects.equals(board[r][c], "R")){
                    button.setGraphic(new ImageView(this.redFrog));
                    button.getProperties().put("TYPE","R");
                }
            }
        }
    }

    /**
     * Main program
     * @param args arguments passed through
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}

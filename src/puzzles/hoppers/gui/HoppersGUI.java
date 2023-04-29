package puzzles.hoppers.gui;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

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
    private Image water = new Image(Objects.requireNonNull(getClass().getResourceAsStream(RESOURCES_DIR + "water.png")));

    private HoppersModel model;
    //the stage
    private Stage stage;
    //the label for the state of the game
    private Label label;
    //the display for buttons on the board
    private GridPane board;
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

    @Override
    public void start(Stage stage) throws Exception {
        //the stage
        this.stage = stage;

        //holds label, board, and other buttons
        VBox screen = new VBox();
        //initialize the label of the state of the game
        this.label = new Label("Loaded " + this.filename);
        this.label.setAlignment(Pos.CENTER);
        //the playing board
        this.board = makeBoard();
        //the buttons
        HBox btns = new HBox();
        Button load = new Button("Load");
        load.setOnAction(event -> loadFile());
        Button reset = new Button("Reset");
        load.setOnAction(event -> resetBoard());
        Button hint = new Button("Hint");
        btns.setAlignment(Pos.CENTER);
        load.setOnAction(event -> hint());
        btns.getChildren().addAll(load, reset, hint);
        //add to VBox
        screen.getChildren().addAll(this.label, this.board, btns);
        //add VBox to scene
        Scene scene = new Scene(screen);
        stage.setScene(scene);
        //the title area shows the game name
        stage.setTitle("Hoppers GUI");
        stage.sizeToScene();
        stage.show();
    }

    private void hint() {
    }

    private void resetBoard() {
    }

    private void loadFile() {
    }

    private GridPane makeBoard() {
        GridPane grid = new GridPane();
        //size of the board
        int row = this.model.getCurrentConfig().getRow();
        int col = this.model.getCurrentConfig().getCol();
        //the game board of the current configuration
        String[][] board = this.model.getCurrentConfig().getBoard();

        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                Button button = new Button();
                //if the board of current config is a *, the spot is not a valid moving spot
                //represented by water
                if(Objects.equals(board[r][c], "*")){
                    button.setGraphic(new ImageView(this.water));
                }else if(Objects.equals(board[r][c], ".")){
                    button.setGraphic(new ImageView(this.lillyPad));
                }else if(Objects.equals(board[r][c], "G")){
                    button.setGraphic(new ImageView(this.greenFrog));
                }else if(Objects.equals(board[r][c], "R")){
                    button.setGraphic(new ImageView(this.redFrog));
                }
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

    @Override
    public void update(HoppersModel hoppersModel, String msg) {

        this.stage.sizeToScene();  // when a different sized puzzle is loaded
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}

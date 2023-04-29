package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import java.io.IOException;
import java.util.Scanner;
/**
 * The representation of the hopper PTUI
 *
 * @author Ryleigh Fuller
 */
public class HoppersPTUI implements Observer<HoppersModel, String> {
    private HoppersModel model;

    /**
     * Initialize the PTUI
     * @param filename the file to create a model with
     * @throws IOException due to file
     */
    public void init(String filename) throws IOException {
        this.model = new HoppersModel(filename);
        this.model.addObserver(this);
        displayHelp();
    }

    /**
     * Print out the model, data for viewing purposes
     * @param model the object that wishes to inform this object
     *                about something that has happened.
     * @param data optional data the server.model can send to the observer
     *
     */
    @Override
    public void update(HoppersModel model, String data) {
        // for demonstration purposes
        System.out.println(data);
        System.out.println(model);
    }

    /**
     * Display messages the computer is able to understand
     * and exactly what they mean
     */
    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    /**
     * the representation of the game.
     * reponds to used input based on the response outlined in displayHelp()
     * @throws IOException due to this.model.reset
     */
    public void run() throws IOException {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if (words.length > 0) {
                if(words[0].startsWith( "q" )) {
                    //quit the game
                    break;
                }else if(words[0].startsWith( "h" )) {
                    //hint next move
                    this.model.hint();
                }else if(words[0].startsWith( "l" ) && words.length == 2) {
                    //load new puzzle file
                    this.model.load(words[1]);
                }else if(words[0].startsWith( "r" )) {
                    //reset the game
                    this.model.reset();
                }else if(words[0].startsWith( "s" ) && words.length == 3){
                    this.model.select(Integer.parseInt(words[1]), Integer.parseInt(words[2]));
                }
                else {
                    displayHelp();
                }
            }
        }
    }

    /**
     * The main function for HoppersPTUI
     * @param args arguments passed through
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            try {
                HoppersPTUI ptui = new HoppersPTUI();
                System.out.println("Loaded: " + args[0]);
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}

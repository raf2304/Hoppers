package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.*;
import java.util.Collection;

/**
 the main class for the hoppers puzzle
 @author Ryleigh Fuller
 */
public class Hoppers {
    /**
     check args, if valid then:
     - print arg values
     - create a starting Hoppers config
     - create a solver object
     - start solving the puzzle
     - print total, unique configs
     - print path or no solution (whichever is applicable)
     */
    public static void main(String[] args) throws IOException {
        //run with one command line argument
        //if the argument is there, it is assumed to be valid
        //otherwise, error message and terminated
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
            System.exit(0);
        }
        //print the arg values
        System.out.println("File: " + args[0]);
        //create a hoppers config
        HoppersConfig hoppersConfig = new HoppersConfig(args[0]);
        //create a solver object
        //Solver solver = new Solver();
        //start solving
        Collection<Configuration> path = hoppersConfig.getSuccessors();
        for (Configuration config: path
        ) {
            System.out.println(config.toString() + "\n");
        }

        /*
        System.out.println("Total Configs: " + solver.getTotalConfigs());
        System.out.print("Unique Configs: " + solver.getUniqueConfigs());
        if(path != null){
            int step = path.size() - 1;
            String pathStr = "";
            for (Configuration config:path
            ) {
                pathStr = "\nStep " + step + ": " + config.toString() + pathStr;
                step--;
            }
            System.out.println(pathStr);
        }else{
            System.out.println("\nNo solution");
        }*/
    }
}

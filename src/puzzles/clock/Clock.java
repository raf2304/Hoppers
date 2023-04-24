package puzzles.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.Collection;
/**
 * The representation of a clock puzzle, using solver and clock config
 *
 * @author Ryleigh Fuller
 */

public class Clock {
    /**
        check args, if valid then:
            - print arg values
            - create a starting clock config
            - create a solver object
            - start solving the puzzle
            - print total, unique configs
            - print path or no solution (whichever is applicable)
     */
    public static void main(String[] args) {
        //requires three command line arguments
        if (args.length < 2) {
            System.out.println(("Usage: java Clock start stop"));
        } else {
            //hours, start, end
            System.out.println("Hours: " + args[0] +", Start: " + args[1] +", End: " + args[2]);
            //create a new configuration with the hours, start, end
            ClockConfig start = new ClockConfig(args[0],args[1],args[2]);
            //create a new solver
            Solver solver = new Solver();
            //start solver
            Collection<Configuration> path = solver.solve(start);

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
            }

        }
    }
}

package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.Collection;
/**
 * The representation of a string puzzle, using solver and string config
 *
 * @author Ryleigh Fuller
 */
public class Strings {
    /**
        check args, if valid then:
            - print arg values
            - create a starting string config
            - create a solver object
            - start solving the puzzle
            - print total, unique configs
            - print path or no solution (whichever is applicable)
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            //hours, start, end
            System.out.println("Start: " + args[0] +", End: " + args[1]);
            //create a new configuration with the hours, start, end
            StringsConfig start = new StringsConfig(args[0],args[1]);
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

package puzzles.common.solver;
import java.util.*;
import java.util.Queue;

public class Solver {
    private final Queue<Configuration> queue;
    private final HashMap<Configuration, Configuration> map;

    private int totalConfigs;
    private int uniqueConfigs;
    /**
     * create a new solver object
     * initiates a queue and a map
     * as well as two counters for the configurations
     */
    public Solver(){
        this.queue = new LinkedList<>();
        this.map = new HashMap<>();
        this.totalConfigs = 0;
        this.uniqueConfigs = 0;
    }
    /**
        start solving the puzzle with the initial value
        add value to the queue and map
        then call the iterative helper function
        @param value the overall starting configuration
     */
    public Collection<Configuration> solve(Configuration value){
        this.totalConfigs++;
        this.uniqueConfigs++;
        //add the conifguration to the queue
        this.queue.add(value);
        //add the configuration to the maop
        this.map.put(value, null);
        return helper();
    }
    /**
        the iterative helper function for the puzzle
        gets the first value off the queue and adds its neighbors to the queue (if it is not a solution)
        @return the path from start to finish or null if no path is available
     */
    public Collection<Configuration> helper(){
        //get the top off the queue
        Configuration value = this.queue.remove();
        Configuration start = value;
        //while the value is not the solution
        while(!value.isSolution()){
            //add the neighbors to the queue
            for (Configuration config:
                value.getNeighbors()) {
                this.totalConfigs++;
                if(!map.containsKey(config)){
                    this.uniqueConfigs++;
                    this.queue.add(config);
                    map.put(config, value);
                }

            }
            try {
                value = this.queue.remove();
            }catch (NoSuchElementException e){
                return null;
            }

        }
        return constructPath(start, value);
    }

    /**
     * Construct the shortest path from the starting configuration to the ending configuration
     * @param startNode the starting configuration
     * @param finishNode the ending configuration
     * @return the shortest path found from the start to finish (in reverse)
     */
    public Collection<Configuration> constructPath(Configuration startNode, Configuration finishNode){
        Collection<Configuration> path = new LinkedList<>();
        if(map.containsKey(finishNode)) {
            Configuration currNode = finishNode;
            while (currNode != startNode) {
                path.add(currNode);
                currNode = map.get(currNode);
            }
            path.add(startNode);
        }
        return path;
    }

    /**
     *
     * @return the total configurations created
     */
    public int getTotalConfigs(){
        return this.totalConfigs;
    }

    /**
     *
     * @return the total ynique configurations
     */
    public int getUniqueConfigs(){
        return this.uniqueConfigs;
    }
}

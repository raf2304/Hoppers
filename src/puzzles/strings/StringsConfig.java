package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
/**
 * The representation of a string configuration
 *
 * @author Ryleigh Fuller
 */
public class StringsConfig implements Configuration {
    private final String start;
    private final String end;
    /**
        Create a new StringsConfig using start/end
        @param start the starting string value
        @param end the ending and goal string value
     */
    public StringsConfig(String start, String end){
        this.start = start;
        this.end = end;
    }

    /**
        Get the starting value of the StringConfig
     */
    public String getStart(){
        return this.start;
    }
    /**
        Get the end of the StringConfig
     */
    public String getEnd(){
        return this.end;
    }
    /**
        Is the solution when start equals the end
     */
    @Override
    public boolean isSolution() {
        return this.start.equals(this.end);
    }
    /**
        Get each possible neighbor combination by changing each char by +/-1
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> neighbors = new LinkedList<>();
        //add neighbors
        String n1;
        String n2;
        for(int i = 0; i < this.start.length(); i++){
            int asciiStart = this.start.charAt(i);
            if(asciiStart-1 < 65){
                n1 = this.start.substring(0, i) + "Z" + this.start.substring(i + 1);
            }else{
                n1 = this.start.substring(0, i) + Character.toString(asciiStart-1) + this.start.substring(i + 1);
            }
            //if the configuration value plus one is more than the clock allows
            //then the value of neighbor one should be 1
            if(asciiStart + 1 > 90){
                n2 =  this.start.substring(0, i) + "A" + this.start.substring(i + 1);
            }else{
                n2 = this.start.substring(0, i) + Character.toString(asciiStart+1) + this.start.substring(i + 1);;
            }
            neighbors.add(new StringsConfig(n1, this.end));
            neighbors.add(new StringsConfig(n2, this.end));
        }
        return neighbors;
    }
    /**
        do these objects have the same values
        @param other object this is being compared to
     */
    @Override
    public boolean equals(Object other) {
        //if other is a StringConfig
        if(other instanceof puzzles.strings.StringsConfig){
            //if values are the same
            return Objects.equals(((StringsConfig) other).getStart(), this.start) && Objects.equals(((StringsConfig) other).getEnd(), this.end);
        }
        //else false
        return false;
    }
    /**
        hashcode of the start
     */
    @Override
    public int hashCode() {
        return this.start.hashCode();
    }
    /**
        get the start value
     */
    @Override
    public String toString() {
        return this.start;
    }
}

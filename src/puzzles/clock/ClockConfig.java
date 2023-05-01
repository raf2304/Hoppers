package puzzles.clock;

import puzzles.common.solver.Configuration;

import java.util.Collection;

import java.util.*;
/**
 * The representation of a clock configuration
 *
 * @author Ryleigh Fuller
 */
public class ClockConfig implements Configuration {
    private final int start;
    private final int hours;
    private final int end;

    /**
        Create a clock configuration
        @param hours the amount of hours on the clock
        @param start the value starting at
        @param end the goal value
     */
    public ClockConfig(String hours, String start, String end){
        this.hours = Integer.parseInt(hours);
        this.start = Integer.parseInt(start);
        this.end = Integer.parseInt(end);;
    }
    /**
        @return the starting value of the ClockConfig
     */
    public int getStart(){
        return this.start;
    }
    /**
        @return the hours of the ClockConfig
     */
    public int getHours(){
        return this.hours;
    }
    /**
        @return the ending values of the ClockConfig
     */
    public int getEnd(){
        return this.end;
    }
    /**
        is this value equal to the goal value?
        @return if this value equals the ending value
     */
    @Override
    public boolean isSolution() {
        return this.start == this.end;
    }

    /**
        @return the list of the ClockConfig neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> neighbors = new LinkedList<>();
        //add neighbors
        int n1;
        int n2;
        //if the configuration value minus one is less than the clock allows
        //then the value of neighbor one should be the highest value of the clock
        if(this.start - 1 <= 0){
            n1 = this.hours;
        }else{
            n1 = this.start - 1;
        }
        //if the configuration value plus one is more than the clock allows
        //then the value of neighbor one should be 1
        if(this.start + 1 > this.hours){
            n2 = 1;
        }else{
            n2 = this.start + 1;
        }
        neighbors.add(new ClockConfig(String.valueOf(hours), String.valueOf(n1), String.valueOf(end)));
        neighbors.add(new ClockConfig(String.valueOf(hours), String.valueOf(n2), String.valueOf(end)));
        return neighbors;
    }
    /**
        Is this object the same as this ClockConfig
        @param other the object this is being compared to
        @return if this and other are equal
     */
    @Override
    public boolean equals(Object other) {
        //if other is a ClockConfig
        if(other instanceof ClockConfig){
            //if values are the same
            if(((ClockConfig) other).getEnd() == this.end && ((ClockConfig) other).getHours() == this.hours){
                return ((ClockConfig) other).getStart() == this.start;
            }
        }
        //else false
        return false;
    }

    /**
        @return value hashcode
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }
    /**
        @return start, as a string
     */
    @Override
    public String toString() {
        return String.valueOf(this.start);
    }
}

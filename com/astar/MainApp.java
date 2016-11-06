package com.astar;

import com.astar.impl.AStarSearchProcessor;
import com.astar.exception.AlgorithmException;

import java.io.IOException;

/**
 * Run a* search here.
 * Once search is complete and successful - The best path will be written to the file sample-output.txt.
 * <p/>
 * The search adheres to the below...
 * Branches from path nearest to the starting point '@'.
 * Favours direction according to the order of @class PossibleMoveEnum
 * Removes garbage data from the text file (sample.txt). File should be UTF-8. Output for #-Best path is saved to the file sample-output.txt.
 * Can be optimised with hints if a location that diverges into multiple paths leads further away from the goal 'X'. (If time permits)
 * If search is stuck in a loop, it will go back to the first occurrence of when we had to choose a specific path.
 * If there are no more possible paths to choose from, then the goal cannot be reached.
 * Has a ragged ArrayList implementation so that each line can be of any length and random access to each variable when calculating the cost of the heuristic.
 */
public class MainApp {

    final static String INPUT_FILE_NAME = "src/com/resources/large_map.txt";
    final static String OUTPUT_FILE_NAME = "src/com/resources/output.txt";


    public static void main(String[] args) throws IOException {
        AStarSearchProcessor aStarSearchProcessor = new AStarSearchProcessor(INPUT_FILE_NAME, OUTPUT_FILE_NAME);
        try {
            aStarSearchProcessor.processSearchAlgorithm();
          
            
        } catch (AlgorithmException ex) {
            System.err.println("Unable to fully process search algorithm with the given input.");
        }

    }
}

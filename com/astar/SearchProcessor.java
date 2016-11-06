package com.astar;

import com.astar.exception.AlgorithmException;

import java.io.IOException;

/**
 * Defines the basic methods required for a search alorithm.
 */
public interface SearchProcessor {

    /**
     * Starts the search process according to the rules and heuristic of the algorithm.
     */
    void processSearchAlgorithm() throws AlgorithmException, IOException;
}

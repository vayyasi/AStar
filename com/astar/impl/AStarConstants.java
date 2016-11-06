package com.astar.impl;

/**
 * Constants related to the AStar implementation.
 */
public class AStarConstants {



    private AStarConstants() {
    }

    public static final char GOAL_POINT_CHAR;
    public static final char FLATLAND;
    public static final char STARTING_POINT_CHAR;
    public static final char FORREST;
    public static final char MOUNTAIN;
    public static final char WATER;
    public static final char PATH_TRAVELLED;
    public static final int ALREADY_TRAVELLED_TO_LOCATION;
    public static final int NOT_ALLOWED;
    public static final String NOT_FOUND;
    public static final String NO_POSSIBLE_PATHS_TO_REACH_THE_GOAL;
    public static final String NO_STARTING_LOCATION_EXISTS;
    public static final String NO_GOAL_EXISTS;
    public static final String SEPARATOR;
    //Regex for characters not allowed.
    public static final String REGEX;

    static {
        GOAL_POINT_CHAR = 'X';
        FLATLAND = '.';
        STARTING_POINT_CHAR = '@';
        FORREST = '*';
        MOUNTAIN = '^';
        WATER = '~';
        PATH_TRAVELLED = '#';
        ALREADY_TRAVELLED_TO_LOCATION = 999;
        NOT_ALLOWED = 999;
        NOT_FOUND = "NotFound";
        NO_POSSIBLE_PATHS_TO_REACH_THE_GOAL = "No possible paths to reach the Goal tile! Search exhausted.";
        NO_STARTING_LOCATION_EXISTS = "No starting location exists!!!";
        NO_GOAL_EXISTS = "No goal exists!!!";
        SEPARATOR = "-";
        REGEX = "[^\\*\\~\\.\\@X\\^\\#]";
    }
}

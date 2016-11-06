package com.astar.impl;

import com.astar.SearchProcessor;
import com.astar.exception.AlgorithmException;
import com.astar.utils.FileHandlingUtility;
import com.astar.utils.PossibleMoveEnum;
import com.astar.utils.SearchUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AStarSearchProcessor implements SearchProcessor {

    private String fileName;
    private String outputFileName;

    //2-D array for storing the valid text from the input file.
    ArrayList<ArrayList<Character>> inputTextList = new ArrayList<>();
    //2-D array representing the path taken to reach the goal.
    ArrayList<ArrayList<Character>> outputTextList = new ArrayList<>();
    //Holds the location of what has been travelled to.
    private Map<String, Boolean> locationTravelledMap = new HashMap<>();
    //Stores a list of possible tile locations that could have been branched to.
    private List<String> locationsStillToBeTraversed = new ArrayList<>();
    //Store the location of the current path that was travelled.
    private List<String> locationListOfCurrentPathTravelled = new ArrayList<>();
    //Stores the location for where multiple paths can be branched from this location.
    private List<String> locationsWithMultiplePaths = new ArrayList<>();

    private String startingLocation;
    private String currentLocation;
    private String goalLocation;
    private String newLocation;

    public AStarSearchProcessor(String fileName, String outputFileName) {
        this.fileName = fileName;
        this.outputFileName = outputFileName;
    }

    /**
     * Method used as an orchestrator to delegate the search algorithm processing.
     *
     * @throws IOException
     */
    public void processSearchAlgorithm() throws AlgorithmException, IOException {

        FileHandlingUtility.readTextFileAndSaveToList(fileName, inputTextList, AStarConstants.REGEX);
        if (startAndGoalLocationFound()) {
            updateStartingPoint();
            //Start search until the goal location is reached.
            while (!currentLocation.equals(goalLocation)) {
                moveToTileWithLowestCost(currentLocation);
            }
            createPathTravelled();
            FileHandlingUtility.writeToOutputFile(outputFileName, outputTextList);
        } else {
            throw new AlgorithmException("Starting or Goal location not found.");
        }
    }

    private void updateStartingPoint() {
        currentLocation = startingLocation;
        updatePathTravelled(currentLocation);
        locationListOfCurrentPathTravelled.add(currentLocation);
    }

    private boolean startAndGoalLocationFound() throws IOException {
        //Get starting location
        startingLocation = SearchUtility.findCharacterInList(inputTextList, AStarConstants.STARTING_POINT_CHAR);
        if (AStarConstants.NOT_FOUND.equals(startingLocation)) {
            System.err.println(AStarConstants.NO_STARTING_LOCATION_EXISTS);
            FileHandlingUtility.writeToOutputFile(outputFileName, AStarConstants.NO_STARTING_LOCATION_EXISTS);
            return false;
        }
        goalLocation = SearchUtility.findCharacterInList(inputTextList, AStarConstants.GOAL_POINT_CHAR);
        if (AStarConstants.NOT_FOUND.equals(goalLocation)) {
            System.err.println(AStarConstants.NO_GOAL_EXISTS);
            FileHandlingUtility.writeToOutputFile(outputFileName, AStarConstants.NO_GOAL_EXISTS);
            return false;
        }
        return true;
    }

    private void createPathTravelled() throws IOException {
        //Using buffering
        FileHandlingUtility.readTextFileAndSaveToList(fileName, outputTextList, AStarConstants.REGEX);
        for (String location : locationListOfCurrentPathTravelled) {
            int currentXLocation = getXCoordinate(location);
            int currentYLocation = getYCoordinate(location);
            outputTextList.get(currentYLocation).set(currentXLocation, AStarConstants.PATH_TRAVELLED);
        }

    }

    private void updatePathTravelled(String location) {
        locationTravelledMap.put(location, true);
        int yCoordinate = getYCoordinate(location);
        int xCoordinate = getXCoordinate(location);
        inputTextList.get(yCoordinate).set(xCoordinate, AStarConstants.PATH_TRAVELLED);
    }

    private int getXCoordinate(final String location) {
        int indexOfSeparator = location.indexOf(AStarConstants.SEPARATOR);
        String xStringValue = location.substring(indexOfSeparator + 1);
        return Integer.parseInt(xStringValue);
    }

    private int getYCoordinate(final String location) {
        int indexOfSeparator = location.indexOf(AStarConstants.SEPARATOR);
        String yStringValue = location.substring(0, indexOfSeparator);
        return Integer.parseInt(yStringValue);
    }

    public int getCostToMoveFromCurrentLocation(PossibleMoveEnum aPossibleMoveEnum) {
        int currentXLocation = getXCoordinate(currentLocation);
        int currentYLocation = getYCoordinate(currentLocation);
        int possibleMoveXlocation;
        int possibleMoveYLocation;
        if (aPossibleMoveEnum.equals(PossibleMoveEnum.EAST)) {
            possibleMoveXlocation = currentXLocation + 1;
            possibleMoveYLocation = currentYLocation;
        } else if (aPossibleMoveEnum.equals(PossibleMoveEnum.SOUTH_EAST)) {
            possibleMoveXlocation = currentXLocation + 1;
            possibleMoveYLocation = currentYLocation + 1;
        } else if (aPossibleMoveEnum.equals(PossibleMoveEnum.SOUTH)) {
            possibleMoveXlocation = currentXLocation;
            possibleMoveYLocation = currentYLocation + 1;
        } else if (aPossibleMoveEnum.equals(PossibleMoveEnum.SOUTH_WEST)) {
            possibleMoveXlocation = currentXLocation - 1;
            possibleMoveYLocation = currentYLocation + 1;
        } else if (aPossibleMoveEnum.equals(PossibleMoveEnum.WEST)) {
            possibleMoveXlocation = currentXLocation - 1;
            possibleMoveYLocation = currentYLocation;
        } else if (aPossibleMoveEnum.equals(PossibleMoveEnum.NORTH_WEST)) {
            possibleMoveXlocation = currentXLocation - 1;
            possibleMoveYLocation = currentYLocation - 1;
        } else if (aPossibleMoveEnum.equals(PossibleMoveEnum.NORTH)) {
            possibleMoveXlocation = currentXLocation;
            possibleMoveYLocation = currentYLocation - 1;
        } else {//NORTH_EAST
            possibleMoveXlocation = currentXLocation + 1;
            possibleMoveYLocation = currentYLocation + 1;
        }
        return getTotalCostFromThisLocation(possibleMoveXlocation, possibleMoveYLocation);
    }

    private int getTotalCostFromThisLocation(final int aPossibleMoveXlocation, final int aPossibleMoveYLocation) {
        int pathCost = getPathCost(aPossibleMoveXlocation, aPossibleMoveYLocation);
        if (pathCost != AStarConstants.NOT_ALLOWED || pathCost != AStarConstants.ALREADY_TRAVELLED_TO_LOCATION) {
            newLocation = getNewTileLocation(aPossibleMoveXlocation, aPossibleMoveYLocation);
            return pathCost + getDistanceToGoalFromLocation(newLocation);
        } else
            return pathCost;
    }

    private String getNewTileLocation(final int aPossibleMoveXlocation, final int aPossibleMoveYLocation) {
        return aPossibleMoveYLocation + "-" + aPossibleMoveXlocation;
    }

    private int getPathCost(final int aPossibleMoveXlocation, final int aPossibleMoveYLocation) {
        try {
            char characterAtPossibleMoveLocation = inputTextList.get(aPossibleMoveYLocation).get(aPossibleMoveXlocation);
            return getPathCostToThisLocation(characterAtPossibleMoveLocation);
        } catch (IndexOutOfBoundsException ex) {
            //Unable to go in this direction
            return AStarConstants.NOT_ALLOWED;
        }
    }

    private int getPathCostToThisLocation(final char aCharacterAtPossibleMoveLocation) {
        //Flatlands check
        if (aCharacterAtPossibleMoveLocation == AStarConstants.FLATLAND || aCharacterAtPossibleMoveLocation == AStarConstants.STARTING_POINT_CHAR || aCharacterAtPossibleMoveLocation == AStarConstants.GOAL_POINT_CHAR) {
            return 1;
        }
        //Forrest check
        else if (aCharacterAtPossibleMoveLocation == AStarConstants.FORREST) {
            return 2;
        }
        //Mountain
        else if (aCharacterAtPossibleMoveLocation == AStarConstants.MOUNTAIN) {
            return 3;
        }
        //Water
        else if (aCharacterAtPossibleMoveLocation == AStarConstants.WATER) {
            return AStarConstants.NOT_ALLOWED;
        } else //Already travelled to this tile (#)
            return AStarConstants.ALREADY_TRAVELLED_TO_LOCATION;
    }

    private int getDistanceToGoalFromLocation(String location) {
        int x1 = getXCoordinate(location);
        int x2 = getXCoordinate(goalLocation);
        int y1 = getYCoordinate(location);
        int y2 = getYCoordinate(goalLocation);
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    /**
     * Moves to tile with the lowest cost as per the Algorithm.
     *
     * @param currentLocation
     */
    private void moveToTileWithLowestCost(String currentLocation) throws IOException, AlgorithmException {
        String locationOfBestTileToMoveTo = getLocationOfBestTileToMoveTo(currentLocation);
        if (!stuckInLoop(locationOfBestTileToMoveTo)) {
            moveToBestTileLocation(locationOfBestTileToMoveTo);
        }
    }

    private void moveToBestTileLocation(final String aLocationOfBestTileToMoveTo) {
        currentLocation = aLocationOfBestTileToMoveTo;
        locationListOfCurrentPathTravelled.add(currentLocation);
        updatePathTravelled(currentLocation);
    }

    private boolean stuckInLoop(final String locationOfBestTileToMoveTo) throws IOException, AlgorithmException {
        //Check if we already travelled to this tile.
        if (locationTravelledMap.containsKey(locationOfBestTileToMoveTo)) {
            //Stuck in loop. Check if there were other paths to branch to.
            if (moveToANewPathIfPossible()) {
                return true;

            } else {
                //No more possible paths to go to - Search is exhausted. Exit program.
                System.err.println(AStarConstants.NO_POSSIBLE_PATHS_TO_REACH_THE_GOAL);
                FileHandlingUtility.writeToOutputFile(outputFileName, AStarConstants.NO_POSSIBLE_PATHS_TO_REACH_THE_GOAL);
                throw new AlgorithmException("Unable to reach goal. The search is exhausted.");
            }
        }
        return false;
    }

    private boolean moveToANewPathIfPossible() {
        if (locationsStillToBeTraversed.size() > 0) {
            updateOptimumPath();
            takeAnotherPath();
            return true;
        }
        //No more possible paths to reach the Goal tile.
        return false;

    }

    private void updateOptimumPath() {
        //Get the location of the FIRST place we could have branched from.
        String lastBranchingLocation = locationsWithMultiplePaths.get(0);
        //Remove everything from after the lastBranchingLocation as this was found to end in stuck loop.
        int indexToRemoveFrom = locationListOfCurrentPathTravelled.indexOf(lastBranchingLocation);
        //Remove the first location as we are now checking it
        locationsWithMultiplePaths.remove(0);
        //Location where we last made a choice to branch from - Remove the sub list from after this location.
        locationListOfCurrentPathTravelled.subList(indexToRemoveFrom + 1, locationListOfCurrentPathTravelled.size()).clear();

    }

    private void takeAnotherPath() {
        //Go back to the point where we FIRST decided to branch and take the other path. ie. branch from point closest to starting point.
        moveToBestTileLocation(locationsStillToBeTraversed.get(0));
        //Now remove from the list as we have traversed to this location now.
        locationsStillToBeTraversed.remove(0);
    }

    private String getLocationOfBestTileToMoveTo(final String currentLocation) {
        int lowestCostToMove = AStarConstants.ALREADY_TRAVELLED_TO_LOCATION;
        String mostProbableTileToMoveToLocation = currentLocation;
        for (PossibleMoveEnum possibleMoveEnum : PossibleMoveEnum.values()) {
            int costToMoveToThisLocation = getCostToMoveFromCurrentLocation(possibleMoveEnum);
            if (costToMoveToThisLocation < lowestCostToMove) {
                lowestCostToMove = costToMoveToThisLocation;
                mostProbableTileToMoveToLocation = newLocation;
            }
            //if costs are equal then this is possibly another optimum path; cost will gradually get lower so the possible tile to move to will be towards the end of the list.
            else if (costToMoveToThisLocation == lowestCostToMove && costToMoveToThisLocation != AStarConstants.NOT_ALLOWED) {
                addThisLocationAsALocationWithMultiplePaths();
                addNewLocationForFuturePossibleTraversal();
            }

        }
        return mostProbableTileToMoveToLocation;
    }

    private void addNewLocationForFuturePossibleTraversal() {
        if (!locationsStillToBeTraversed.contains(newLocation)) {
            locationsStillToBeTraversed.add(newLocation);
        }
    }

    private void addThisLocationAsALocationWithMultiplePaths() {
        locationsWithMultiplePaths.add(this.currentLocation);
    }
}

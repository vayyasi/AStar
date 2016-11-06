package com.astar.utils;

import java.util.ArrayList;

/**
 * Utility class to process movement from tile to tile.
 */
public class SearchUtility {

    private SearchUtility() {
    }

    /**
     * @param characterList   - 2-D list of characters.
     * @param characterToFind - character to search for in the 2-D list.
     * @return - (y) - (x) index location of the first occurrence of @characterToFind (logically reads top down from the file).
     */
    public static String findCharacterInList(final ArrayList<ArrayList<Character>> characterList, char characterToFind) {
        int y = 0;
        for (ArrayList<Character> characters : characterList) {
            int x = 0;
            for (Character character : characters) {
                if (character.equals(characterToFind)) {
                    return y + "-" + x;
                }
                x++;
            }
            y++;
        }
        return "NotFound";
    }
}

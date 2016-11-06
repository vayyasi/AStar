package com.astar.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;  
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class for reading/writing/processing of files.
 */
public final class FileHandlingUtility {

    //Using a best assumption encoding scheme as UTF-8 is recommended for text files.
    public static final Charset ENCODING_SCHEME = StandardCharsets.UTF_8;

    private FileHandlingUtility() {
    }

    public static void readTextFileAndSaveToList(String fileName, ArrayList<ArrayList<Character>> list, String regex) throws IOException {
        Path path = Paths.get(fileName);
        try (BufferedReader bufferedReader = Files.newBufferedReader(path, ENCODING_SCHEME)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                processLine(line, list, regex);
            }
        }
    }

    public static void writeToTextFile(String fileName, List<String> lines) throws IOException {
        Path path = Paths.get(fileName);
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, ENCODING_SCHEME)) {
            for (String line : lines) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

        }
    }

    /**
     * Processes each line by removing 'garbage' variables and add this to the list.
     *
     * @param line - String of characters to be processed.
     * @param list - 2-D list containing valid characters.
     */
    private static void processLine(String line, ArrayList<ArrayList<Character>> list, String regex) {
        String processedLine = removeGarbageCharacters(line, regex);
        if (!processedLine.isEmpty()) {//Check if the entire line was not garbage data.
            char[] charArray = processedLine.toCharArray();
            //Converting to wrapper to use convenience methods...eg (toUpper)
            Character[] charObjectArray = ArrayUtils.toObject(charArray);
            list.add(new ArrayList<>(Arrays.asList(charObjectArray)));
        }
    }

    /**
     * @param line to be processed.
     * @return only the characters needed for this search scenario by using a regex.
     */
    private static String removeGarbageCharacters(final String line, final String regex) {
        return line.replaceAll(regex, "");

    }

    private static String getStringRepresentation(ArrayList<Character> list) {
        StringBuilder builder = new StringBuilder(list.size());
        for (Character ch : list) {
            builder.append(ch);
        }
        return builder.toString();
    }

    public static void writeToOutputFile(String outputFileName, ArrayList<ArrayList<Character>> outputTextList) throws IOException {
        List<String> lines = new ArrayList<>();
        for (ArrayList<Character> characters : outputTextList) {
            lines.add(getStringRepresentation(characters));
        }
        FileHandlingUtility.writeToTextFile(outputFileName, lines);
    }

    public static void writeToOutputFile(String outputFileName, String message) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(message);
        FileHandlingUtility.writeToTextFile(outputFileName, lines);
    }

    public static void printTwoDimensionalList(final ArrayList<ArrayList<Character>> characterList) {
        System.out.println("Printing out list");
        for (ArrayList<Character> characters : characterList) {
            for (Character character : characters) {
                System.out.print(character);
            }
            System.out.print("\n");
        }
    }

    private static void log(Object aMsg) {
        System.out.println(String.valueOf(aMsg));
    }
}

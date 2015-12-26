package com.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args)
    {
	    String parseThis = "[dubstep] - Awesomeness - (secret sauce remix) by (the legend) me, Axel [arena mix] Kennedal [hacker] (feat. skimask)";

        System.out.println(cleanupFilename(parseThis));
    }

    public static String cleanupFilename(String filename)
    {
        // remove all parentheses except those that contain some keywords
        String parenthesesPattern =
                "(" + "\\(" + "|" + "\\[" + ")" +
                        "[^mix]*" +"?" + "[^feat]" +
                        "(" + "\\)" + "|" + "\\]" + ")";
        String whitespacePattern = "\\s+";

        String result = filename.replaceAll(parenthesesPattern, "");

        // move all keyword-containing parentheses to the end of the result string
        Pattern parenthesesKeywordPattern = Pattern.compile(
                "(" + "\\(" + "|" + "\\[" + ")" +
                        "(.*?)" +
                        "(" + "\\)" + "|" + "\\]" + ")"
                , Pattern.DOTALL);
        ArrayList<String> parenthesesWithKeywords = new ArrayList<String>();
        ArrayList<Integer> startIndexes = new ArrayList<Integer>();
        ArrayList<Integer> endIndexes = new ArrayList<Integer>();
        Matcher matcher = parenthesesKeywordPattern.matcher(result);
        while (matcher.find())
        {
            String parentheses = result.substring(matcher.start(), matcher.end());
            parenthesesWithKeywords.add(parentheses);
            startIndexes.add(matcher.start()); endIndexes.add(matcher.end());
        }

        // get the parts in between the parentheses
        ArrayList<String> otherParts = new ArrayList<String>();
        otherParts.add(result.substring(0, startIndexes.get(0)));
        for (int i = 0; i < parenthesesWithKeywords.size()-1; i++)
        {
            otherParts.add(result.substring(endIndexes.get(i), startIndexes.get(i+1)));
        }
        otherParts.add(result.substring(endIndexes.get(parenthesesWithKeywords.size()-1), result.length()));

        // put it all together
        result = "";
        for (String otherPart : otherParts)
        {
            result += otherPart;
        }
        for (String parentheses : parenthesesWithKeywords)
        {
            result += parentheses;
        }

        // make sure filename starts with an alphanumeric
        char c;
        for (int i = 0; i < result.length(); i++)
        {
            c = result.charAt(i);
            if (Character.isLetterOrDigit(c))
            {
                result = result.substring(i, result.length());
                break;
            }
        }

        result = result.replaceAll(whitespacePattern, " ").trim();
        return result;
    }

    public static String cleanupFilenameAlternative(String filename)
    {
        filename.replace("_", " ");
        filename.replace("\"", "");

        // first check '(', then '['
        ArrayList<String> openingDelimiters = new ArrayList<>();
        ArrayList<String> closingDelimiters = new ArrayList<>();
        openingDelimiters.add("("); closingDelimiters.add(")");
        openingDelimiters.add("["); closingDelimiters.add("]");

        ArrayList<String> keywordsForKeeping = new ArrayList<>();
        keywordsForKeeping.add("feat");
        keywordsForKeeping.add("mix");

        int nextIndex;
        Iterator<String> openingDelimiterIterator = openingDelimiters.iterator();
        Iterator<String> closingDelimiterIterator = closingDelimiters.iterator();
        Iterator<String> keywordsForKeepingIterator = keywordsForKeeping.iterator();
        while (openingDelimiterIterator.hasNext() && closingDelimiterIterator.hasNext())
        {
            String openingDelimiter = openingDelimiterIterator.next();
            String closingDelimiter = closingDelimiterIterator.next();

            while (keywordsForKeepingIterator.hasNext())
            {
                String keywordForKeeping = keywordsForKeepingIterator.next();
                while ((nextIndex = filename.indexOf(openingDelimiter)) != -1)
                {
                    int secondIndex = filename.indexOf(closingDelimiter, nextIndex);

                    // remove whatever is inside the parentheses, except for if it says its a remix or mix
                    if (secondIndex != -1)
                    {
                        int keywordIndex = filename.toLowerCase().indexOf(keywordForKeeping); // covers both "remix" and "mix"

                        // "mix" is nowhere in the filename OR
                        // "mix" is outside these parentheses
                        if ((keywordIndex == -1) || (keywordIndex < nextIndex || secondIndex < keywordIndex))
                        {
                            filename = filename.substring(0, nextIndex) + filename.substring(secondIndex + 1, filename.length()).trim();
                        }

                        // "mix" is inside these parentheses
                        else if (nextIndex < keywordIndex && keywordIndex < secondIndex)
                        {
                            // all parentheses containing "mix" have been moved to the end of the filename
                            if (keywordIndex > filename.lastIndexOf("-"))
                            {
                                break;
                            }

                            // move mix-parentheses to end of filename
                            filename =
                                    filename.substring(0, nextIndex) +
                                            filename.substring(secondIndex + 1, filename.length()).trim() + " " +
                                            filename.substring(nextIndex, secondIndex + 1);
                        }
                    }
                }
            }
        }

        // make sure filename starts with an alphanumeric
        char c;
        for (int i = 0; i < filename.length(); i++)
        {
            c = filename.charAt(i);
            if (Character.isLetterOrDigit(c))
            {
                filename = filename.substring(i, filename.length());
                break;
            }
        }
    }
}

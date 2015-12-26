package com.company;

import com.mpatric.mp3agic.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Handles various tasks related to ID3-tags of mp3s.
 * @author Axel Kennedal
 * @version 1.0
 * Created on 2015-12-23.
 */
public class ID3TagManager
{
    /**
     * Perform setID3TagsAndNewFilenameFromFilename() on all mp3s in a directory.
     * @param directory
     * @param recursive
     */
    public static void processAllInDirectory(File directory, boolean recursive)
    {
        Arrays.stream(directory.listFiles(
                pathname -> {
                    String pathString = pathname.toString();
                    if (!pathString.startsWith(".") && pathname.isDirectory() ||
                            pathString.endsWith(".mp3")) return true;
                    else return false;
                }
        )).forEach(file -> {
            // go deeper
            if (file.isDirectory() && recursive)
            {
                processAllInDirectory(file, true);
            }

            else
            {
                // do something with each file

            }
        });
    }

    /**
     * Read the filename of an mp3, extract the title and artist from it and clean it up (renames the file).
     * @param file the file to process
     */
    public static void setID3TagsAndNewFilenameFromFilename(File file)
    {
        Mp3File mp3File = null;
        try
        {
            mp3File = new Mp3File(file.getAbsolutePath());
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (UnsupportedTagException e)
        {
            e.printStackTrace();
        } catch (InvalidDataException e)
        {
            e.printStackTrace();
        }
        if (mp3File.hasId3v2Tag())
        {
            ID3v2 id3v2Tag = mp3File.getId3v2Tag();

            String filename = file.getName();
            String filePath = file.getParent() + "/";

            filename = removeFileEnding(filename);
            filename = cleanUpFilename(filename);

            // extract title and artist from filename assuming they are separated by a "-"
            int separatorLocation = filename.lastIndexOf("-");
            if (separatorLocation != -1)
            {
                String artistFromFilename = filename.substring(0, separatorLocation).trim();
                String titleFromFileName = filename.substring(separatorLocation + 1, filename.length()).trim();

                id3v2Tag.setArtist(artistFromFilename);
                id3v2Tag.setTitle(titleFromFileName);
            }

            try
            {
                // save new tags and new filename
                mp3File.save(filePath + filename + ".mp3");

                // remove old version of file
                file.delete();
            } catch (IOException e)
            {
                e.printStackTrace();
            } catch (NotSupportedException e)
            {
                e.printStackTrace();
            }
            /*
            System.out.println("Filename: " + mp3File.getFilename());
            System.out.println("Track name: " + id3v2Tag.getTitle());
            System.out.println("Artist: " + id3v2Tag.getArtist());
            System.out.println("Length of this mp3 is: " + mp3File.getLengthInSeconds() + " seconds");
            System.out.println("Bitrate: " + mp3File.getBitrate() + " kbps " + (mp3File.isVbr() ? "(VBR)" : "(CBR)"));
            System.out.println("Sample rate: " + mp3File.getSampleRate() + " Hz");
            System.out.println("Has ID3v1 tag?: " + (mp3File.hasId3v1Tag() ? "YES" : "NO"));
            System.out.println("Has ID3v2 tag?: " + (mp3File.hasId3v2Tag() ? "YES" : "NO"));
            System.out.println("Has custom tag?: " + (mp3File.hasCustomTag() ? "YES" : "NO"));
            */
        }
    }

    /**
     * Removes ".mp3" from the filename.
     * @param filename the filename to process
     * @return the filename without it's ending
     */
    private static String removeFileEnding(String filename)
    {
        return filename.substring(0, filename.lastIndexOf("."));
    }

    /**
     * Make filename slightly prettier.
     * @param filename the filename to clean up
     * @return the filename, less messy!
     */
    private static String cleanUpFilename(String filename)
    {
        String result = filename;

        String whitespacePattern = "\\s+";
        result = result.replaceAll(whitespacePattern, " ").trim();
        result = result.replaceAll("_", " ");
        result = result.replaceAll("\"", "");

        // make sure filename starts with an alphanumeric
        char c;
        for (int i = 0; i < filename.length(); i++)
        {
            c = filename.charAt(i);
            if (Character.isLetterOrDigit(c))
            {
                result = filename.substring(i, filename.length());
                break;
            }
        }

        return result;
    }
}

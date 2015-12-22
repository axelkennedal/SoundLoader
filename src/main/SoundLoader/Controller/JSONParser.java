package SoundLoader.Controller;

import org.apache.commons.io.IOUtils;

import java.net.URL;

/**
 * @author Axel Kennedal
 * @version 1.0
 * Created on 2015-12-18.
 */
public class JSONParser
{
    /**
     * Attempts to get the download link from the youtube to mp3 API.
     * @param youtubeConverterLink
     * @return the download link if successful, null otherwise
     */
    public static String getDownloadLink(String youtubeConverterLink)
    {
        try
        {
            String JSONResponse = IOUtils.toString(new URL(youtubeConverterLink));

            if (!isJSON(JSONResponse))
            {
                return null;
            }

            return value("link", JSONResponse);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Super naive check to see if a String is JSON.
     * @param possibleJSON a String that might be JSON
     * @return true if JSON, false otherwise
     */
    private static boolean isJSON(String possibleJSON)
    {
        if (possibleJSON.trim().startsWith("{") && possibleJSON.trim().endsWith("}")) { return true; }
        else return false;
    }

    /**
     * Get the value of a key in a JSON object.
     * @param key the key who's value is looked for
     * @param JSON the JSON to look in
     * @return the value if found, null otherwise
     */
    private static String value(String key, String JSON)
    {
        String value = null;
        int startIndex = JSON.indexOf("\"" + key + "\"") + key.length() + 4;
        int endIndex = JSON.indexOf("\",\"", startIndex);
        if (endIndex == -1) endIndex = JSON.indexOf('}', startIndex);
        value = JSON.substring(startIndex, endIndex);
        value = value.replace("\\", "");
        return value;
    }
}
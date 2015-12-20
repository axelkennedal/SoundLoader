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
    public static String getDownloadLink(String youtubeConverterLink)
    {
        String link = null;
        try
        {
            String JSONResponse = IOUtils.toString(new URL(youtubeConverterLink));
            String key = "link";
            int startIndex = JSONResponse.indexOf("\"" + key + "\"") + key.length() + 4;
            int endIndex = JSONResponse.indexOf("\",\"", startIndex);
            if (endIndex == -1) endIndex = JSONResponse.indexOf('}', startIndex);
            link = JSONResponse.substring(startIndex, endIndex);
            link = link.replace("\\", "");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return link;
    }
}
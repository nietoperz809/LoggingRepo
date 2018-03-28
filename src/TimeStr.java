/**
 * Filespec is nnnnnnnnnnnnn~name...
 * First 13 chars are thisTime stamp
 */

public class TimeStr
{
    private long thisTime;

    /**
     * Constructor: set thisTime value.
     * So all subsequent calls to getWithTime has the same prefix
     */
    public TimeStr()
    {
        thisTime = System.currentTimeMillis();
    }

    public String getWithTime(String in)
    {
        return String.format ("%013d", thisTime) +"~"+in;
    }

    public long getInstanceTime()
    {
        return thisTime;
    }

    /**
     * Get the thisTime stamp from string
     * @param in String conforming to file spec
     * @return thisTime stamp as long value
     */
    public static long getTimestamp (String in)
    {
        return Long.parseLong(in.substring(0,13));
    }

    public static String getFilename (String in)
    {
        return in.substring (14);
    }
}

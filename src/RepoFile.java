import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.TreeSet;

/**
 * Class to represent a repo file and all versions
 */
public class RepoFile
{
    private final String projectPath;
    private final String filePath;
    private final String fname;
    private long[] allVersions;

    /**
     * Constructor
     * @param project project name
     * @param filename file name
     */
    public RepoFile (String project, String filename)
    {
        fname = filename;
        projectPath = System.getProperty("user.home") + File.separator +
                "~" + project + File.separator;
        filePath = projectPath+fname;
        refreshVersionList();
    }

    /**
     * Find nearest version given a timestamp
     * @param timestamp the ts to be searched vor
     * @return the nearest version (timestamp)
     * This function never fails
     */
    public long findNearest (long timestamp)
    {
        int pos = Arrays.binarySearch(allVersions, timestamp);
        if (pos == -1)
            return allVersions[0];
        if (pos > 0)
            return allVersions[pos];
        pos = -pos-1;
        if (pos == allVersions.length)
            return allVersions[allVersions.length-1];
        int pos2 = pos-1;
        long diff1 = Math.abs (timestamp-allVersions[pos]);
        long diff2 = Math.abs (timestamp-allVersions[pos2]);
        if (diff1 < diff2)
            return allVersions[pos];
        return allVersions[pos2];
    }

    /**
     * refresh the version list
     */
    public void refreshVersionList ()
    {
        TreeSet<Long> results = new TreeSet<>();
        String dir = filePath;
        File[] files = new File(dir).listFiles();
        if (files != null)
        {
            for (File file : files)
            {
                if (file.isFile())
                {
                    try
                    {
                        Long l = Long.parseLong(file.getName());
                        results.add(l);
                    }
                    catch (NumberFormatException e)
                    {
                        //e.printStackTrace();
                    }
                }
            }
        }
        Long[] array = results.stream().toArray(Long[]::new);
        allVersions = new long[results.size()];
        for (int s=0; s<results.size(); s++)
        {
            allVersions[s] = array[s];
        }
    }

    /**
     * Load a file given time stamp
     * @param timestamp the time stamp
     * @return file content
     * @throws IOException if smth went wrong / file does not exist
     */
    public byte[] load (long timestamp) throws IOException
    {
        String path = filePath+File.separator+timestamp;
        return Files.readAllBytes(Paths.get(path));
    }

    /**
     * Load version description given version number
     * @param timestamp version number
     * @return file description
     * @throws IOException if smth went wrong or file does not exist
     */
    public String loadDescr (long timestamp) throws IOException
    {
        String path = filePath+File.separator+"~"+timestamp;
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        return new String (bytes, "UTF-8");
    }

    /**
     * Save file and description
     * @param content file content
     * @param descr description
     * @throws IOException if smth went wrong
     */
    public void save (byte[] content, String descr) throws IOException
    {
        String dir = filePath;
        Files.createDirectories(Paths.get(dir));
        String nname = String.format ("%013d", System.currentTimeMillis());
        Files.write( Paths.get(dir+File.separator+nname),
                content, StandardOpenOption.CREATE);
        Files.write( Paths.get(dir+File.separator+"~"+nname),
                descr.getBytes("UTF-8"), StandardOpenOption.CREATE);
        refreshVersionList ();
    }

//    public static void main (String[] args) throws IOException
//    {
//        RepoFile f = new RepoFile("repoTest");
//        f.save ("hello doof düöß fick".getBytes(), "laladumm", "lalablah test123");
//
//        // print all versions
//        Long[] vs = f.getVersions("laladumm");
//        for (Long l : vs)
//        {
//            Date dat = new Date(l);
//            System.out.println(dat);
//        }
//        // load recent version
//        String cont = new String(f.load ("laladumm", vs[0]));
//        System.out.println(cont);
//        String desc = f.loadDescr ("laladumm", vs[0]);
//        System.out.println(desc);
//    }

    static void testSeek(RepoFile f, long val)
    {
        long res = f.findNearest( val);
        System.out.println("Seek: "+val+" --> "+res);
    }

    public static void main (String[] args) throws IOException
    {
        RepoFile f = new RepoFile("repoTest", "laladumm");
//        f.save ("hello doof düöß fick".getBytes(), "laladumm", "lalablah test123");
//        f.save ("mama fick".getBytes(), "laladumm", "mma test123");

        // print all versions
        for (int s=0; s<f.allVersions.length; s++)
        {
            System.out.println(s+" : "+f.allVersions[s]);
        }
        testSeek (f, 0L);
        testSeek (f, 1522411402238L);
        testSeek (f, 1522411402237L);
        testSeek (f, 1522411468585L);
        testSeek (f, 1522411468583L);
        testSeek (f, 1522411468584L);
        testSeek (f, 1522417053424L);
        testSeek (f, 1522417053524L);
        testSeek (f, 9922417053524L);
//        f.seek ("laladumm", 0L);
//        f.seek ("laladumm", 0L);
//        f.seek ("laladumm", 1522411402238L);
//
//        f.seek ("laladumm", 1522411402237L);
//        f.seek ("laladumm", 1522411468583L);
//        f.seek ("laladumm", 1522411468585L);
    }

}


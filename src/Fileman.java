import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeSet;

public class Fileman
{
    static private final String dirPath = System.getProperty("user.home")+ File.separator+"~";

    /**
     * Get List of versions of a file
     * @param fname the file name
     * @return all version numbers (time stamps)
     */
    public TreeSet<Long> getVersions (String fname)
    {
        TreeSet<Long> results = new TreeSet<>(Comparator.reverseOrder());
        String dir = dirPath+fname;
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
        return results;
    }

    /**
     * Load a file given name an version number
     * @param fname the file name
     * @param timestamp the time stamp
     * @return file content
     * @throws IOException if smth went wrong
     */
    public String load (String fname, long timestamp) throws IOException
    {
        String path = dirPath+fname+File.separator+timestamp;
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        return new String (bytes, "UTF-8");
    }

    /**
     * Load version description given file name and version number
     * @param fname The file name
     * @param timestamp version number
     * @return file description
     * @throws IOException if smth went wrong
     */
    public String loadDescr (String fname, long timestamp) throws IOException
    {
        String path = dirPath+fname+File.separator+"~"+timestamp;
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        return new String (bytes, "UTF-8");
    }

    /**
     * Save file and description
     * @param content file content
     * @param fname file name
     * @param descr description
     * @throws IOException if smth went wrong
     */
    public void save (String content, String fname, String descr) throws IOException
    {
        String dir = dirPath+fname;
        Files.createDirectories(Paths.get(dir));
        String nname = String.format ("%013d", System.currentTimeMillis());
        Files.write( Paths.get(dir+File.separator+nname),
                content.getBytes("UTF-8"), StandardOpenOption.CREATE);
        Files.write( Paths.get(dir+File.separator+"~"+nname),
                descr.getBytes("UTF-8"), StandardOpenOption.CREATE);
    }

    /**
     * Test
     * @param args
     * @throws IOException
     */
    public static void main (String[] args) throws IOException
    {
        Fileman f = new Fileman();
        f.save ("hello doof düöß fick", "laladumm", "lalablah test123");

        // print all versions
        TreeSet<Long> vs = f.getVersions("laladumm");
        for (Long l : vs)
        {
            Date dat = new Date(l);
            System.out.println(dat);
        }
        // load recent version
        String cont = f.load ("laladumm", vs.first());
        System.out.println(cont);
        String desc = f.loadDescr ("laladumm", vs.first());
        System.out.println(desc);
    }
}


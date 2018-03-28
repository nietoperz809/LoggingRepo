import org.junit.Assert;
import org.junit.Test;

public class Tester
{
    @Test
    public void TestTimeStr() throws InterruptedException
    {
        TimeStr ts = new TimeStr();
        String s1 = ts.getWithTime("Hallo");
        Thread.sleep(1000);
        String s2 = ts.getWithTime("Doof");
        Thread.sleep(1000);
        String s3 = ts.getWithTime("Dumm");
        long t1 = TimeStr.getTimestamp(s1);
        long t2 = TimeStr.getTimestamp(s2);
        long t3 = TimeStr.getTimestamp(s3);
        Assert.assertEquals(t1, t2, t3);
    }

    @Test
    public void TestTimeStr2()
    {
        TimeStr ts = new TimeStr();
        String s1 = ts.getWithTime("Hallo");
        Assert.assertEquals(ts.getInstanceTime(), TimeStr.getTimestamp(s1));
    }
}

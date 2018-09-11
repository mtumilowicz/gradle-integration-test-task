import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by mtumilowicz on 2018-09-11.
 */
public class FirstIT {
    @Test
    public void initTest() {
        System.out.println("FirstIT");
        assertTrue(First.truth());
    }
}

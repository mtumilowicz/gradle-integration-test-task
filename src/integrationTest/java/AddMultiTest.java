import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class AddMultiTest {
    @Test
    public void initTest() {
        System.out.println("Adder Multiplier Test");
        Adder a = new Adder();
        Multiplier m = new Multiplier();
        assertTrue(m.multiply(a.add(10, 15), 2) == 50);
    }
}

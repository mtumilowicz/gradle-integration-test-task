import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class MultiplierTest {
    @Test
    public void test() {
        System.out.println("Multiplier Test");
        Multiplier m = new Multiplier();
        assertTrue(m.multiply(2, 5) == 10);
    }
}
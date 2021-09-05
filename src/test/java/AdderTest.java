import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class AdderTest {
    @Test
    public void test(){
        System.out.println("Adder Test");
        Adder c = new Adder();
        assertTrue(c.add(10, 15)==25);
    }
}

package software_testing.function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Random;

public class ArccosTest {

    @ParameterizedTest
    @CsvSource({
            "0.0, 1.57079632679",
            "0.5, 1.0471975512",
            "1.0, 0.0",
            "-1.0, 3.14159265359",
            "0.25, 1.31811607165"
    })
    public void testFixedPoints(double x, double expected) {
        double actual = Arccos.arccos(x);
        Assertions.assertEquals(expected, actual, 1e-3, "arccos accuracy for " + x);
    }

    @Test
    public void testMonotonicity() {
        double prev = Arccos.arccos(-1.0);

        for (double x = -0.99; x <= 1.0; x += 0.01) {
            double current = Arccos.arccos(x);
            Assertions.assertTrue(current <= prev + 1e-6);
            prev = current;
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = { -1.1, 1.1, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN })
    public void testOutOfDomain(double x) {
        double res = Arccos.arccos(x);
        Assertions.assertTrue(Double.isNaN(res), "Out of domain should be NaN for x=" + x);
    }

    @Test
    public void testRandomCompareToMathAcos() {
        Random r = new Random(1);
        for (int i = 0; i < 500; i++) {
            double x = -1.0 + 2.0 * r.nextDouble();
            double expected = Math.acos(x);
            double actual = Arccos.arccos(x);
            // у нас ограниченная точность ряда, допустим 1e-6..1e-3 в зависимости от x
            Assertions.assertEquals(expected, actual, 1e-3, "random compare for x=" + x);
        }
    }
}
package software_testing.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import software_testing.text.domain.*;

public class TextDomainTest {

    @Test
    public void testFordLightsMatch() {
        Cabin cabin = new Cabin("Dark Cabin");
        FordPrefect ford = new FordPrefect("Ford Prefect", false, "Knows Vogons");
        Matchstick match = new Matchstick("Ford");

        ford.enterCabin(cabin);
        cabin.addObject(match);

        ford.lightMatch(match);

        Assertions.assertTrue(match.isLit());
        Assertions.assertEquals(1, ford.getNervousLevel());
    }

    @Test
    public void testCabinContainsObjects() {
        Cabin cabin = new Cabin("Cabin");
        FordPrefect ford = new FordPrefect("Ford Prefect", false, "Info");

        ford.enterCabin(cabin);

        Assertions.assertTrue(cabin.contains(ford));
    }

    @Test
    public void testInvalidPersonName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new FordPrefect("", false, "info");
        });
    }
}
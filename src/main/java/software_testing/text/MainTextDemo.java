package software_testing.text;

import software_testing.text.domain.*;

public class MainTextDemo {

    public static void main(String[] args) {

        VogonShip ship = new VogonShip("Vogon Flagship");
        Cabin cabin = new Cabin("Dark Cabin");
        ship.addCabin(cabin);

        FordPrefect ford = new FordPrefect(
                "Ford Prefect",
                false,
                "Knows everything about Vogons"
        );

        ford.enterCabin(cabin);

        Matchstick match = new Matchstick("Ford Prefect");
        cabin.addObject(match);

        ford.lightMatch(match);

        System.out.println("Match lit: " + match.isLit());
        System.out.println("Ford nervous level: " + ford.getNervousLevel());
    }
}
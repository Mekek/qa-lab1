package software_testing.text.domain;

import software_testing.text.abstract_classes.Space;

import java.util.ArrayList;
import java.util.List;

public class VogonShip extends Space {

    private final List<Cabin> cabins = new ArrayList<>();

    public VogonShip(String name) {
        super(name);
    }

    public void addCabin(Cabin cabin) {
        cabins.add(cabin);
    }

    public List<Cabin> getCabins() {
        return List.copyOf(cabins);
    }
}
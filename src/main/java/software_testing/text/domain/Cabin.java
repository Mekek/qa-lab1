package software_testing.text.domain;

import software_testing.text.abstract_classes.Space;

import java.util.ArrayList;
import java.util.List;

public class Cabin extends Space {

    private final List<Object> objects = new ArrayList<>();

    public Cabin(String name) {
        super(name);
    }

    public void addObject(Object obj) {
        objects.add(obj);
    }

    public List<Object> getObjects() {
        return List.copyOf(objects);
    }

    public boolean contains(Object obj) {
        return objects.contains(obj);
    }
}
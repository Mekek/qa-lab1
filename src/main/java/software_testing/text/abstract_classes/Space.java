package software_testing.text.abstract_classes;

public abstract class Space {

    protected String name;

    public Space(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Space name cannot be empty");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
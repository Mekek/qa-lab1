package software_testing.text.abstract_classes;

public abstract class Person {

    protected String name;
    protected int nervousLevel;

    public Person(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name;
        this.nervousLevel = 0;
    }

    public String getName() {
        return name;
    }

    public int getNervousLevel() {
        return nervousLevel;
    }

    public abstract void act();
}
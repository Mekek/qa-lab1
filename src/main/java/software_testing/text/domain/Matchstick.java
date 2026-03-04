package software_testing.text.domain;

import software_testing.text.interfaces.Lightable;

public class Matchstick implements Lightable {

    private boolean lit = false;
    private final String owner;

    public Matchstick(String owner) {
        this.owner = owner;
    }

    @Override
    public void ignite() {
        this.lit = true;
    }

    @Override
    public boolean isLit() {
        return lit;
    }

    public String getOwner() {
        return owner;
    }
}
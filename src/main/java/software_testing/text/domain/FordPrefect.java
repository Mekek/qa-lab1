package software_testing.text.domain;

import software_testing.text.abstract_classes.Person;
import software_testing.text.interfaces.Nervous;

public class FordPrefect extends Person implements Nervous {

    private final boolean isVogon;
    private final String knowledgeAboutVogons;
    private Cabin currentCabin;

    public FordPrefect(String name, boolean isVogon, String knowledge) {
        super(name);
        this.isVogon = isVogon;
        this.knowledgeAboutVogons = knowledge;
    }

    public void enterCabin(Cabin cabin) {
        this.currentCabin = cabin;
        cabin.addObject(this);
    }

    public void lightMatch(Matchstick match) {
        match.ignite();
        increaseNervousness();
    }

    @Override
    public void increaseNervousness() {
        nervousLevel++;
    }

    @Override
    public void act() {
        increaseNervousness();
    }

    public boolean isVogon() {
        return isVogon;
    }

    public String getKnowledgeAboutVogons() {
        return knowledgeAboutVogons;
    }

    public Cabin getCurrentCabin() {
        return currentCabin;
    }
}
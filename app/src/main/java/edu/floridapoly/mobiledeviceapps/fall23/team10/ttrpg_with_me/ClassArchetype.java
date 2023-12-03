package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

public class ClassArchetype extends ClassManager {
    long pk;

    String name;
    int level;

    public ClassArchetype(String name) {
        this.name = name;
        this.level = 0;
    }
    public ClassArchetype(String name, int level) {
        this.name = name;
        this.level = level;
    }

    // Getter Methods
    public String getName() { return name; }
    public int getLevel() {
        return level;
    }
}

package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import java.util.List;

public class Character extends ClassManager {

    String name;
    String race;
    String image_url;

    // ClassArchetype object holds level
    // Future-proofing the app, allows for multi-class characters in future
    ClassArchetype class_arc;
    List<Note> notes;

    public Character(String name, String race, ClassArchetype class_arc) {
        this.name = name;
        this.race = race;
        this.class_arc = class_arc;
        trackObject(this);
    }

    // Getter Methods
    public String getName() {
        return name;
    }
    public String getDescription() {
        // Example:         Level 5 | Half-Orc | Bard
        return String.format("Level %d | %s | %s", class_arc.getLevel(), race, class_arc.getName());
    }
    public String getImageURL() {
        return image_url;
    }
    public ClassArchetype getClassArc() { return class_arc; }
    public List<Note> getNotes() { return notes; }

    // Setter Methods
    public void setImageUrl(String url) {
        image_url = url;
    }
}

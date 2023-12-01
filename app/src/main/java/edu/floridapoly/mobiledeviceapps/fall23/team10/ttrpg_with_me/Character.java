package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import androidx.databinding.ObservableField;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
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

    int hp;
    int hitDice;
    ObservableField<Integer> pBonus = new ObservableField<Integer>(2);
    int exp;
    int level;

    JSONObject cclass;


    DisplayObject [] Abilities;

    public Item [] Weapons;
    public Item [] Spells;
    public Item [] Armor;
    public Item [] Items;
    public Item [] Extras;
    public Item [] Notes;

    int currentHp;
    int totalHp;

    Hashtable<String, ObservableField<Integer>> stats = new Hashtable<String, ObservableField<Integer>>() {{
        put("STR", new ObservableField<>(10)); put("CON", new ObservableField<>(10)); put("DEX", new ObservableField<>(10));
        put("INT", new ObservableField<>(10)); put("WIS", new ObservableField<>(10)); put("CHA", new ObservableField<>(10));
    }};

    Hashtable<String, ObservableField<Integer>> savethrow = new Hashtable<String, ObservableField<Integer>>() {{
        put("STR", new ObservableField<>(10)); put("CON", new ObservableField<>(10)); put("DEX", new ObservableField<>(10));
        put("INT", new ObservableField<>(10)); put("WIS", new ObservableField<>(10)); put("CHA", new ObservableField<>(10));
    }};

    Hashtable<String, ObservableField<Boolean>> saveBools = new Hashtable<String, ObservableField<Boolean>>() {{
        put("STR", new ObservableField<>(false)); put("CON", new ObservableField<>(false)); put("DEX", new ObservableField<>(false));
        put("INT", new ObservableField<>(false)); put("WIS", new ObservableField<>(false)); put("CHA", new ObservableField<>(false));
    }};

    Hashtable<String, Integer> skills = new Hashtable<String, Integer>() {{
        put("Athletics", 0); put("Acrobatics", 0); put("Sleight of Hand", 0);
        put("Stealth", 0); put("Arcana", 0); put("History", 0);
        put("Investigation", 0); put("Nature", 0); put("Religion", 0);
        put("Animal Handling", 0); put("Insight", 0); put("Medicine", 0);
        put("Perception", 0); put("Survival", 0); put("Deception", 0);
        put("Intimidation", 0); put("Performance", 0); put("Persuation", 0);
    }};

    public void levelUp() {
        level += 1;
        int [] p = {5, 9, 13, 17,};
        for (int i: p) {
            if (p[i] == level) { pBonus.set(pBonus.get() + 1); }
        }
    }
}

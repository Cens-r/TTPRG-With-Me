package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.content.Context;

import androidx.databinding.ObservableField;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Character extends ClassManager {
    long pk = -1;

    String name;
    String race;
    String image_url;

    // ClassArchetype object holds level
    // Future-proofing the app, allows for multi-class characters in future
    String class_uuid;
    transient ClassArchetype classArc;

    public Character(String name, String race, ClassArchetype classArc) {
        this.name = name;
        this.race = race;
        this.classArc = classArc;

        IntializeItems(this);
        trackObject(this);
    }

    public static void IntializeItems(Character object) {
        object.Notes = new ArrayList<>();
        object.Abilities = new ArrayList<>();
        object.Backpack = new Hashtable<String, List<Item>>() {{
            put("Weapons", new ArrayList<>()); put("Spells", new ArrayList<>()); put("Armor", new ArrayList<>());
            put("Items", new ArrayList<>()); put("Extras", new ArrayList<>());
        }};
    }

    // Getter Methods
    public String getName() {
        return name;
    }
    public String getDescription() {
        // Example:         Level 5 | Half-Orc | Bard
        return String.format("Level %d | %s | %s", level, race, classArc.name);
    }
    public String getImageURL() {
        return image_url;
    }
    public ClassArchetype getClassArc() { return classArc; }

    // Setter Methods
    public void setImageUrl(String url) {
        image_url = url;
    }

    int hp;
    int hitDice;
    int pBonus = 2;
    int exp;
    int level = 1;


    public transient List<Item> Abilities;
    public transient List<Item> Notes;
    public transient Hashtable<String, List<Item>> Backpack;

    int currentHp;
    int totalHp;

    Hashtable<String, Integer> stats = new Hashtable<String, Integer>() {{
        put("STR", 10); put("CON", 10); put("DEX", 10);
        put("INT", 10); put("WIS", 10); put("CHA", 10);
    }};

    Hashtable<String, Integer> savethrow = new Hashtable<String, Integer>() {{
        put("STR", 10); put("CON", 10); put("DEX", 10);
        put("INT", 10); put("WIS", 10); put("CHA", 10);
    }};

    Hashtable<String, Boolean> saveBools = new Hashtable<String, Boolean>() {{
        put("STR", false); put("CON", false); put("DEX", false);
        put("INT", false); put("WIS", false); put("CHA", false);
    }};

    Hashtable<String, Integer> skills = new Hashtable<String, Integer>() {{
        put("Athletics", 0); put("Acrobatics", 0); put("Sleight of Hand", 0);
        put("Stealth", 0); put("Arcana", 0); put("History", 0);
        put("Investigation", 0); put("Nature", 0); put("Religion", 0);
        put("Animal Handling", 0); put("Insight", 0); put("Medicine", 0);
        put("Perception", 0); put("Survival", 0); put("Deception", 0);
        put("Intimidation", 0); put("Performance", 0); put("Persuation", 0);
    }};

    public void setHP(int HP, Context context)
    {
        hp = HP;
        DatabaseManager db = new DatabaseManager(context);
        db.update(id, "CHARACTERS", this.toJson().toString());
        db.close();
    }

    public void levelUp() {
        level += 1;
        int [] p = {5, 9, 13, 17,};
        for (int i: p) {
            if (p[i] == level) { pBonus++; }
        }
    }
}

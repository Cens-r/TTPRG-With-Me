package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

import android.content.Context;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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

    static final Hashtable<String, String> profMap = new Hashtable<String, String>(){{
        put("Athletics", "STR");
        put("Acrobatics", "DEX"); put("Sleight of Hand", "DEX"); put("Stealth", "DEX");
        put("Arcana", "INT"); put("History", "INT"); put("Investigation", "INT"); put("Nature", "INT"); put("Religion", "INT");
        put("Animal Handling", "WIS"); put("Insight", "WIS"); put("Medicine", "WIS");put("Perception", "WIS"); put("Survival", "WIS");
        put("Deception", "CHA");put("Intimidation", "CHA"); put("Performance", "CHA"); put("Persuasion", "CHA");
    }};

    Hashtable<String, Integer> stats = new Hashtable<String, Integer>() {{
        put("STR", 10); put("CON", 10); put("DEX", 10);
        put("INT", 10); put("WIS", 10); put("CHA", 10);
    }};

    public Hashtable<String, Integer> proficiency = new Hashtable<String, Integer>(){{
        put("STR", 0); put("CON", 0); put("DEX", 0);
        put("INT", 0); put("WIS", 0); put("CHA", 0);
        put("Athletics", 0);
        put("Acrobatics", 0); put("Sleight of Hand", 0); put("Stealth", 0);
        put("Arcana", 0); put("History", 0); put("Investigation", 0); put("Nature", 0); put("Religion", 0);
        put("Animal Handling", 0); put("Insight", 0); put("Medicine", 0);put("Perception", 0); put("Survival", 0);
        put("Deception", 0);put("Intimidation", 0); put("Performance", 0); put("Persuasion", 0);
    }};

    public static void save(Context context, Character character)
    {
        DatabaseManager db = new DatabaseManager(context);
        db.update(character.pk, "CHARACTERS", character.toJson().toString());
        db.close();
    }
    public void levelUp() {
        level += 1;
        int [] p = {5, 9, 13, 17};
        for (int i: p) {
            if (i == level) { pBonus -= 1; }
        }
    }
    public void levelDown()
    {
        level -= 1;
        int [] p = {4,8,12,16};
        for (int i: p) {
            if (i == level) {pBonus -= 1;}
        }
    }

    public void setStats(String stat, int set) {
        stats.put(stat, set);
        for(Object o: profMap.entrySet()) {
            Map.Entry prof = (Map.Entry) o;
            if(prof.getKey().equals(stat)) {
                //skills.put(stat, set);
            }
        }
    }

    public int calcStatBonus(String stat) {
        int statValue = stats.get(stat);
        return (statValue - 10) / 2;
    }

    public int calcProfBonus(String stat) {
        int profBonus = proficiency.get(stat) * pBonus;
        return calcStatBonus(stat) + profBonus;
    }
    public int calcProfBonus(String skill, String stat) {
        int profBonus = proficiency.get(skill) * pBonus;
        return calcStatBonus(stat) + profBonus;
    }

    public void setProf(Context ctx, String name, Integer value) {
        proficiency.put(name, value);
        save(ctx, this);
    }
}

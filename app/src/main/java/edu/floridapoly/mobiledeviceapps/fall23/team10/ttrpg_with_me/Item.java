package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;



public class Item extends ClassManager {
    // Format Variables: Item Type, Level, Race, Class
    private final static String ITEM_PROMPT =
            "Create and explain a DnD %s for a level %d %s %s using the format <Name> <Description>\n"
            + "Do not provide any extra info, only the name and description.";

    String name;
    String description;
    String type;
    boolean favorited;

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
        trackObject(this);
    }
    public Item type(String type) {
        this.type = type;
        return this;
    }
    public Item favorited(boolean favorited) {
        this.favorited = favorited;
        return this;
    }

    /*
    public static Item Generate(String type, Character character) {
        //String formattedPrompt = String.format(ITEM_PROMPT, type);
    }
    */
}

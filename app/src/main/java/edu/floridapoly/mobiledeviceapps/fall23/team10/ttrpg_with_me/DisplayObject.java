package edu.floridapoly.mobiledeviceapps.fall23.team10.ttrpg_with_me;

public class DisplayObject {
    String header;
    String body;
    boolean favorited;

    public DisplayObject(String header, String body, boolean favorited) {
        this.header = header;
        this.body = body;
        this.favorited = favorited;
    }

    String getHeader() { return header; }
    String getBody() { return body; }

    void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }
}

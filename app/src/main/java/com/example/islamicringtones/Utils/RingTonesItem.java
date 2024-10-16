package com.example.islamicringtones.Utils;


public class RingTonesItem {
    private int image;
    private String title;
    private String categories;
    private String totalLengthOfSound;
    private int ringtoneResId;

    public RingTonesItem(int image, String title, String categories, String totalLengthOfSound, int ringtoneResId) {
        this.image = image;
        this.title = title;
        this.categories = categories;
        this.totalLengthOfSound = totalLengthOfSound;
        this.ringtoneResId = ringtoneResId;
    }

    // Getters
    public int getImage() { return image; }
    public String getTitle() { return title; }
    public String getCategories() { return categories; }
    public String getTotalLengthOfSound() { return totalLengthOfSound; }
    public int getRingtoneResId() { return ringtoneResId; }
}

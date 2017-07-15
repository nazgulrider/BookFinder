package com.avempra.bookfinder;

import android.graphics.drawable.Drawable;

/**
 * Created by shres on 5/21/2017.
 */

public class Book {
    private String title;
    private String subTitle;
    private String authors;
    private String categories;
    private String description;
    private double averageRating;
    private String ratingsCount;
    private String imageUrl;
    private String previewLink;
    private Drawable image;

    public Drawable getImage() {
        return image;
    }

    public Book(String title, String subTitle, String authors, String categories, String description, double averageRating, String ratingsCount, String imageUrl, String previewLink, Drawable image) {
        this.title = title;
        this.subTitle = subTitle;
        this.authors = authors;
        this.categories = categories;
        this.description = description;
        this.averageRating = averageRating;
        this.ratingsCount = ratingsCount;
        this.imageUrl = imageUrl;
        this.previewLink = previewLink;
        this.image=image;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getAuthors() {
        return authors;
    }

    public String getCategories() {
        return categories;
    }

    public String getDescription() {
        return description;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public String getRatingsCount() {
        return ratingsCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPreviewLink() {
        return previewLink;
    }
}


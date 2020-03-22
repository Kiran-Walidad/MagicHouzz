package dev.crew.magichouse;

public class ItemImage3DModel {
    String catName , imageTitle;
    String image;


    public ItemImage3DModel(String catName, String imageTitle, String image) {
        this.catName = catName;
        this.imageTitle = imageTitle;
        this.image = image;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

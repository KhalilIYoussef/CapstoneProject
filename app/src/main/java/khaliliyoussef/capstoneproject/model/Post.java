package khaliliyoussef.capstoneproject.model;

/**
 * Created by Khalil on 8/3/2017.
 */

public class Post {

    private String title;
    private String description;
    private String photoUrl;


    public Post() {
    }

    public Post(String text, String name, String photoUrl) {
        this.title = text;
        this.description = name;
        this.photoUrl = photoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String text) {
        this.title = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String name) {
        this.description = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}

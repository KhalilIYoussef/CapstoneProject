package khaliliyoussef.capstoneproject.model;

/**
 * Created by Khalil on 8/4/2017.
 */

public class User {
    private String name;
    private String image;

    public User() {

    }

    public User(String name, String image) {
        this.name = name;

        this.image = image;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

package sv.edu.itca.apployment.modelos;

public class Publication {
    private String title;
    private String description;
    private String location;

    // Constructor
    public Publication(String title, String description, String location) {
        this.title = title;
        this.description = description;
        this.location = location;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }
}

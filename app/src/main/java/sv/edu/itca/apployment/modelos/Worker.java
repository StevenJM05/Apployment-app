package sv.edu.itca.apployment.modelos;

public class Worker {
    private int id;
    private String names;
    private String last_name;
    private String profession;
    private String city;
    private String photo;

    public int getId() {
        return id;
    }
    public String getName() {
        return names + " " + last_name;
    }

    public String getProfession() {
        return profession;
    }

    public String getCity() {
        return city;
    }

    public String getPhotoUrl() {
        return "http://192.168.56.1/storage/" + photo;
    }
}

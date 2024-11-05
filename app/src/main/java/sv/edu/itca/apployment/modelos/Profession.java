package sv.edu.itca.apployment.modelos;

public class Profession {
    private String professionName;
    private String description;

    public Profession(String professionName, String description) {
        this.professionName = professionName;
        this.description = description;
    }

    public String getProfessionName() {
        return professionName;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return professionName + ": " + description;
    }
}

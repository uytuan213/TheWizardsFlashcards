package ca.thewizards.flashcards.Model;

public class Collection {
    private int id;
    private String name;

    public Collection() {

    }

    public Collection(int modelId, String modelName) {
        id = modelId;
        name = modelName;
    }

    public int getId() {
        return id;
    }

    public void setId(int modelId) {
        id = modelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String modelName) {
        name = modelName;
    }
}

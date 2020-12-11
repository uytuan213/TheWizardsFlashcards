package ca.thewizards.flashcards.Model;

public class Collection {
    private int modelId;
    private String modelName;

    public Collection() {

    }

    public Collection(int id, String name) {
        modelId = id;
        modelName = name;
    }

    public int getId() {
        return modelId;
    }

    public void setId(int id) {
        modelId = id;
    }

    public String getName() {
        return modelName;
    }

    public void setName(String name) {
        modelName = name;
    }
}

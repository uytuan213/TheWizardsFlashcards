package ca.thewizards.flashcards.Model;

public class Question {
    private int modelId;
    private String modelAnswer;
    private String modelQuestion;
    private int modelCollectionId;

    public Question() {

    }

    public Question(int id, String answer, String question, int collectionId) {
        modelId = id;
        modelAnswer = answer;
        modelQuestion = question;
        modelCollectionId = collectionId;
    }

    public int getId() {
        return modelId;
    }

    public void setId(int id) {
        modelId = id;
    }

    public String getAnswer() {
        return modelAnswer;
    }

    public void setAnswer(String answer) {
        modelAnswer = answer;
    }

    public String getQuestion() {
        return modelQuestion;
    }

    public void setQuestion(String question) {
        modelQuestion = question;
    }

    public int getCollectionId() {
        return modelCollectionId;
    }

    public void setCollectionId(int collectionId) {
        modelCollectionId = collectionId;
    }

}

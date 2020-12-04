package ca.thewizards.flashcards.Model;

public class Question {
    private int id;
    private String answer;
    private String question;
    private int collectionId;

    public Question() {

    }

    public Question(int modelId, String modelAnswer, String modelQuestion, int modelCollectionId) {
        id = modelId;
        answer = modelAnswer;
        question = modelQuestion;
        collectionId = modelCollectionId;
    }

    public int getId() {
        return id;
    }

    public void setId(int modelId) {
        id = modelId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String modelAnswer) {
        answer = modelAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String modelQuestion) {
        question = modelQuestion;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int modelCollectionId) {
        collectionId = modelCollectionId;
    }

}

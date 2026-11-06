package com.apepdcl.gis_survey.sasa;

public class QuestionModel {
    private String id,question, answer, image, image_from, image_path, convertedImage;

    public String getConvertedImage() {
        return convertedImage;
    }

    public void setConvertedImage(String convertedImage) {
        this.convertedImage = convertedImage;
    }

    private int selectedRadioButtonId = -1;

    public int getSelectedRadioButtonId() {
        return selectedRadioButtonId;
    }

    public void setSelectedRadioButtonId(int selectedRadioButtonId) {
        this.selectedRadioButtonId = selectedRadioButtonId;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getImage_from() {
        return image_from;
    }

    public void setImage_from(String image_from) {
        this.image_from = image_from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

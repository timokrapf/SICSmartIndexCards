package com.example.timokrapf.sic_smartindexcards;

public class SmartIndexCards {

    private String subject, question, answer;

    public SmartIndexCards(String subject, String question, String answer) {
        this.subject = subject;
        this.question = question;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getSubject() {
        return subject;
    }
}

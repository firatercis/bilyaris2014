package com.o6Systems.bilyarisAppFund;

public interface BYDatabaseInterface {
	
	public String getQuestionsDate();
	public void insertQuestionPack(QuestionPack qp);
	public void updateQuestion(Question q);
	public QuestionPack getQuestions(String category, int userID, int minDifficulty, int maxDifficulty);
	public QuestionPack getQuestions(QuestionQuery query);
	
	public void insertUser(User user);
	public void setQuestionsAsked(QuestionPack qp, User u);
	
}

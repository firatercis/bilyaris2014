package com.o6Systems.bilyarisAppFund;

public interface BYDatabaseInterface {
	
	public void insertQuestionPack(QuestionPack qp);
	public QuestionPack getQuestions(String category, int userID, int minDifficulty, int maxDifficulty);
	public void clearQuestions();
	
	public void insertUser(User user);
	public void setQuestionsAsked(QuestionPack qp, User u);
	
}

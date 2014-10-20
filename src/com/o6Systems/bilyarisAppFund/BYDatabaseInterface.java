package com.o6Systems.bilyarisAppFund;

public interface BYDatabaseInterface {
	
	public void insertQuestionPack(QuestionPack qp);
	public QuestionPack getQuestions(String category, int userID, int minDifficulty, int maxDifficulty, int nQuestions);
	public void clearQuestions();
	
	public void insertUser(User user);
	public void deleteUser(int userID);
	public User[] getUsers();
	public void setQuestionAsked(Question Q, User U);
	public void updateStatValue(int uid,String category, String tag);
}

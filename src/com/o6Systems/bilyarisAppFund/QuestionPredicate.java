/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.o6Systems.bilyarisAppFund;

import com.google.common.base.Predicate;

/**
 *
 * @author Administrator
 */
public class QuestionPredicate implements Predicate<Question>{
    
    public final static int QUESTION_IN_CATEGORY = 0;
    public final static int QUESTION_HARDER_THAN = 1;
    public final static int QUESTION_EASIER_THAN = 2;
    public final static int QUESTION_SAME_HARDNESS = 3; 
    public final static int QUESTION_CONTAINS_PATTERN = 4;
    
    int searchType;
    String param;
    
    
    public  QuestionPredicate(int searchType,String param){
        this.searchType = searchType;
        this.param = param;
    }


    @Override
    public boolean apply(Question question) {
        boolean result = false;
        switch(searchType){
            case QUESTION_HARDER_THAN:
                int difficulty = Integer.parseInt(param);
                result = (question.difficultyLevel > difficulty);
                break;
            case QUESTION_EASIER_THAN:
                difficulty = Integer.parseInt(param);
                result = (question.difficultyLevel < difficulty);
                break;
            case QUESTION_SAME_HARDNESS:
                difficulty = Integer.parseInt(param);
                result = (question.difficultyLevel == difficulty);
                break;
            case QUESTION_CONTAINS_PATTERN:
                String keyword = param;
                result = question.text.contains(keyword);
                for(int i=0; i< question.getAlternatives().length; i++){
                    result |= question.getAlternative(i).contains(keyword);
                }
                break;
            case QUESTION_IN_CATEGORY:
                String category = param;
                result =  (question.category.equals(category));
                break;
            default:
                break;
        }
        return result;
    }
   
    
}

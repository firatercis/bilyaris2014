/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.o6Systems.bilyarisAppFund;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 *
 * @author fercis
 */
public class Question {
    
    public final static int N_POSSIBLE_ANSWERS=4;
    
    public final static String[] ALTERNATIVE_CAPTIONS = {"A","B","C","D"};
    
    // Attributes
    
    @Element
    public int questionID;
    @Element
    public String category;
    @Element
    public int difficultyLevel;
    @Element
    public String text;
    @ElementArray(required = false)
    private String[] alternatives;
    
    @Element(required=false)
    private String choice0;
    @Element(required=false)
    private String choice1;
    @Element(required=false)
    private String choice2;
    @Element(required=false)
    private String choice3;
    
    @Element(required=false)
    private int answerID = -1;
    // State variables
    
    private int nAddedAlternatives=0;
    
    public String[] getAlternatives() {
        
        if(alternatives == null){
            restoreAlternatives();
        }
        
        return alternatives;           
    }
    
    public void backupAlternatives(){
        if(alternatives != null){
            choice0 = alternatives[0];
            choice1 = alternatives[1];
            choice2 = alternatives[2];
            choice3 = alternatives[3];
            alternatives = null;
        }
        
        
    }
    public void restoreAlternatives(){
        
        alternatives = new String[4];
        alternatives[0] = choice0;
        alternatives[1] = choice1;
        alternatives[2] = choice2;
        alternatives[3] = choice3;
    }
    
    public void addAlternative(String alternative){
        if(alternatives == null){
            alternatives = new String[N_POSSIBLE_ANSWERS];
        }
        alternatives[nAddedAlternatives] = alternative;
       
        nAddedAlternatives++;

    }
    
    public void clearAlternatives(){
        nAddedAlternatives = 0;
        alternatives = null;
    }
    
    
    public String getAlternative(int i){
        //String result = ALTERNATIVE_CAPTIONS[i];
        //result += ": ";
        
        if(alternatives == null){
            restoreAlternatives();
        }
        
        String result = alternatives[i];
        return result;
    }
    
    public int getAnswer(){
        return answerID;
    }
    
    public void setAnswer(int answer){
        answerID = answer;
    }
    
}

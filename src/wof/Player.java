package wof;


public class Player {
    private int totalScore;
    private int currentScore;
    private int turnLeft;
    public void setTurnLeft(int input)
    {
        turnLeft=input;
    }
    
    public void setTotalScore(int input)
    {
        totalScore+=input;
    }
    
    public void setCurrentScore(int input)
    {
        currentScore=input;
    }
     
    public int getCurrentScore()
    {
        return currentScore;
    }

    public int getTotalScore()
    {
        return totalScore;
    }
    
    public int getTurnLeft()
    {
        return turnLeft;
    }
    
    public void keepPoints(int points)
    {
        this.totalScore += points;
    }
    
    public void reduceTurnLeft()
    {
        turnLeft--;
    }

    public void addPoints(int points)
    {
        this.currentScore += points;
    }
   
}
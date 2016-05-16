/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wof;

import java.io.File;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author TienLA
 */
public class GameGUI extends javax.swing.JFrame {
    
    // The static attribute 
    private static int numberPlayer;
    private static Player[] player;
    private static int numberOfRound;
    private static int roundIndex=1;
    
    //Check if the player 
    private boolean isOutOfTurn;
    
    //The guess character
    private char guessChar;
    
    //The board (Question, Answer)
    private static Board b;
    
    //The result of spinning wheel
    private int curSpin;
    
    //The index of the current player
    private int i;
    
    //Check if the game finish
    private int finish;
    
    //The theme of the game
    private String theme;
    
    //The starting of the phrase in the board on screen    
    private int index;
    
    //Check if the curSpin is present or not
    private int present;
    
    //The Attribute to caculate the spinning whell
    private int angleTest=0;
    private int angleCal=0;
    private Point x,y;
    private int value;    
    private int maxTime=400;
    private int totalTime;
    
    //The final value of special spinresult
    private final int GET_A_TURN=1;
    private final int LOSE_A_TURN=-1;
    private final int PRIZE=3;
    private final int BANK_RUPT=0;
    
    //*****************************************************************
    // The new Thread to show the spining wheel
    //*****************************************************************
    
    //Read the wheel image and spin it
    public class MyRunner implements Runnable{
        int time;
        int totalTime ;
        
        public MyRunner(int input, int totalTime){
            time=input;
            this.totalTime=totalTime;
        }
        
        public void run(){
            String inputFileLocation="img/Wheel.png";
            double angleOfRotation=0.0;
            double angleMax = 20.0;
            int speedUpTime = (int)(totalTime*((float)(50-value/2)/100));
            int speedDownTime = (int)(totalTime*0.5);
            JFrame frame = new JFrame("Wheel"); 
            JLabel label = new JLabel();
            JLabel label1 = new JLabel();
            BufferedImage originalImage=readImage(inputFileLocation);
            BufferedImage processedImage;
            frame.add(label);
            frame.setSize(500,550);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.add(label1);
            label.setLocation(0, 0);
            label1.setIcon(new ImageIcon("img/Pointer.png"));
            for(int i=0;i<totalTime;i++){
                if (i<speedUpTime) angleOfRotation += angleMax/speedUpTime;
                else if (i>=totalTime - speedDownTime) angleOfRotation -= angleMax/speedDownTime;
                try {
                    Thread.sleep(time);
                } catch (InterruptedException ex) {
                }
                processedImage=rotateMyImage(originalImage, angleOfRotation);
                label.setIcon(new ImageIcon(processedImage));
            }
            try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                }
            frame.setVisible(false);
            
        }
    }
    
    //Caculate the current angle after spin the wheel
    private void calAngle(){
        totalTime=(int)(maxTime*value/100);
        
//        Random r =new Random();
//        
//        totalTime+=r.nextInt(50);
       
        int a = (int)(totalTime*((float)(50-value/2)/100));
     
        int s = (int)(totalTime*0.5);

        double c=0.0;
        double d=20.0;
        for(int k=0;k<totalTime;k++){
                if (k<a) {c += d/a;}
                else if (k>=totalTime - s) c -= d/(totalTime-s);
                angleCal += c;
   
        }
        System.out.println("Input:" + angleCal);
    }
    
    
    public void Run(){
        calAngle();
        MyRunner myRunner = new MyRunner(20,totalTime);
        Thread myThread = new Thread(myRunner);
        myThread.start();
        
    }
    
    // Rotate the Image
    public BufferedImage rotateMyImage(BufferedImage img, double angle) {
        angleTest+=angle;
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg =new BufferedImage(w, h, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.rotate(Math.toRadians(angleTest), w/2, h/2);
        g.drawImage(img, null, 0, 0);
        return dimg;
    }
 
    //Read the image from file
    public static BufferedImage readImage(String fileLocation) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(fileLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }   
        return img;
    }
    
    //Caculate spin result
    public int spinResult(int angle){
        int result=0;
        int temp=angle % 360;
        temp=temp/24;
        switch(temp){
            case 0: result = 900; break;
            case 1: result = GET_A_TURN; break;
            case 2: result = 500; break;
            case 3: result = 1000; break;
            case 4: result = 400; break;
            case 5: result = LOSE_A_TURN; break;
            case 6: result = 600; break;
            case 7: result = PRIZE; break;
            case 8: result = 300; break;
            case 9: result = 800; break;
            case 10: result = 1100; break;
            case 11: result = 100; break;
            case 12: result =200; break;
            case 13: result =700; break;
            case 14: result = BANK_RUPT; break ;    
        }
        return result;
    }
    
    
    
    
    //*****************************************************************
    //Update the player panel
    private void update(){
        switch(i+1){
                    case 1:
                        TurnLabel1.setText(Integer.toString(player[i].getTurnLeft()));
                        ScoreLabel1.setText(Integer.toString(player[i].getCurrentScore()));
                        PlayerLabel1.setForeground(Color.blue);
                        PlayerLabel2.setForeground(Color.black);
                        PlayerLabel3.setForeground(Color.black);
                        PlayerLabel4.setForeground(Color.black);
                        break;
                    case 2:
                        TurnLabel2.setText(Integer.toString(player[i].getTurnLeft()));
                        ScoreLabel2.setText(Integer.toString(player[i].getCurrentScore()));
                        PlayerLabel2.setForeground(Color.blue);
                        PlayerLabel1.setForeground(Color.black);
                        PlayerLabel3.setForeground(Color.black);
                        PlayerLabel4.setForeground(Color.black);
                        break;  
                    case 3:
                        TurnLabel3.setText(Integer.toString(player[i].getTurnLeft()));
                        ScoreLabel3.setText(Integer.toString(player[i].getCurrentScore()));
                        PlayerLabel3.setForeground(Color.blue);
                        PlayerLabel2.setForeground(Color.black);
                        PlayerLabel1.setForeground(Color.black);
                        PlayerLabel4.setForeground(Color.black);
                        break;  
                    case 4:
                        TurnLabel4.setText(Integer.toString(player[i].getTurnLeft()));
                        ScoreLabel4.setText(Integer.toString(player[i].getCurrentScore()));
                        PlayerLabel4.setForeground(Color.blue);
                        PlayerLabel2.setForeground(Color.black);
                        PlayerLabel3.setForeground(Color.black);
                        PlayerLabel1.setForeground(Color.black);
                        break;    
                    }
    }
    
    //Help show the phrase to the board
    private void setAnswerText(int index, char input){
        switch(index){
            case 1:
                AnswerChar1.setText(Character.toString(input));
                break;
            case 2:
                AnswerChar2.setText(Character.toString(input));
                break;
            case 3:
                AnswerChar3.setText(Character.toString(input));
                break;
            case 4:
                AnswerChar4.setText(Character.toString(input));
                break;
            case 5:
                AnswerChar5.setText(Character.toString(input));
                break;
            case 6:
                AnswerChar6.setText(Character.toString(input));
                break;
            case 7:
                AnswerChar7.setText(Character.toString(input));
                break;
            case 8:
                AnswerChar8.setText(Character.toString(input));
                break;
            case 9:
                AnswerChar9.setText(Character.toString(input));
                break;
            case 10:
                AnswerChar10.setText(Character.toString(input));
                break;
            case 11:
                AnswerChar11.setText(Character.toString(input));
                break;
            case 12:
                AnswerChar12.setText(Character.toString(input));
                break;    
            case 13:
                AnswerChar13.setText(Character.toString(input));
                break;
            case 14:
                AnswerChar14.setText(Character.toString(input));
                break;
           case 15:
                AnswerChar15.setText(Character.toString(input));
                break;
            case 16:
                AnswerChar16.setText(Character.toString(input));
                break;   
            case 17:
                AnswerChar17.setText(Character.toString(input));
                break;
            case 18:
                AnswerChar18.setText(Character.toString(input));
                break;
            case 19:
                AnswerChar17.setText(Character.toString(input));
                break;
            case 20:
                AnswerChar20.setText(Character.toString(input));
                break;    
            case 21:
                AnswerChar21.setText(Character.toString(input));
                break;   
            case 22:
                AnswerChar17.setText(Character.toString(input));
                break;        
        }
    }
    
    //Help show the phrase to the board
    private void setAnswerColor(int index, Color input){
        switch(index){
            case 1:
                AnswerChar1.setOpaque(true);
                AnswerChar1.setBackground(input);
                break;
            case 2:
                AnswerChar2.setOpaque(true);
                AnswerChar2.setBackground(input);
                break;
            case 3:
                AnswerChar3.setOpaque(true);
                AnswerChar3.setBackground(input);
                break;
            case 4:
                AnswerChar4.setOpaque(true);
                AnswerChar4.setBackground(input);
                break;
            case 5:
                AnswerChar5.setOpaque(true);
                AnswerChar5.setBackground(input);
                break;
            case 6:
                AnswerChar6.setOpaque(true);
                AnswerChar6.setBackground(input);
                break;
            case 7:
                AnswerChar7.setOpaque(true);
                AnswerChar7.setBackground(input);
                break;
            case 8:
                AnswerChar8.setOpaque(true);
                AnswerChar8.setBackground(input);
                break;
            case 9:
                AnswerChar9.setOpaque(true);
                AnswerChar9.setBackground(input);
                break;
            case 10:
                AnswerChar10.setOpaque(true);
                AnswerChar10.setBackground(input);
                break;
            case 11:
                AnswerChar11.setOpaque(true);
                AnswerChar11.setBackground(input);
                break;
            case 12:
                AnswerChar12.setOpaque(true);
                AnswerChar12.setBackground(input);
                break;    
            case 13:
                AnswerChar13.setOpaque(true);
                AnswerChar13.setBackground(input);
                break;
            case 14:
                AnswerChar14.setOpaque(true);
                AnswerChar14.setBackground(input);
                break;
            case 15:
                AnswerChar15.setOpaque(true);
                AnswerChar15.setBackground(input);
                break;
            case 16:
                AnswerChar16.setOpaque(true);
                AnswerChar16.setBackground(input);
                break;   
            case 17:
                AnswerChar17.setOpaque(true);
                AnswerChar17.setBackground(input);
                break;
            case 18:
                AnswerChar18.setOpaque(true);
                AnswerChar18.setBackground(input);
                break;
            case 19:
                AnswerChar19.setOpaque(true);
                AnswerChar19.setBackground(input);
                break;
            case 20:
                AnswerChar20.setOpaque(true);
                AnswerChar20.setBackground(input);
                break;    
            case 21:
                AnswerChar21.setOpaque(true);
                AnswerChar21.setBackground(input);
                break;   
            case 22:
                AnswerChar22.setOpaque(true);
                AnswerChar22.setBackground(input);
                break;           
        }
    }
    
    //Caculate the starting point of the phrase on the board
    private int computeIndex(String str){
        return ((22-str.length())/2);
    }
    
    //Initalize a game
    public GameGUI(int num, Player[] player,Board b,int present) {
        initComponents();
        for (int k=1;k<=22;k++){
            setAnswerColor(k,Color.YELLOW);
        }
        this.present=present;
        GameGUI.player=player;
        GameGUI.numberOfRound=num;
        GameGUI.b=b;
        b.newPhrase();
        System.out.println(num);
        numberPlayer=num;
        RoundLabel.setText("Round "+roundIndex);
        QuestionLabel.setText(b.getQuestion());
        index=computeIndex(b.getCurrentPhrase());
        updateAnswerPanel(index,b.getCurrentPhrase());
        for(i=0;i<numberPlayer;i++)
        {
           player[i].setTurnLeft(3);
           player[i].setCurrentScore(0);
           switch(i+1){
            case 1:
                 TurnLabel1.setText(Integer.toString(player[i].getTurnLeft()));
                 ScoreLabel1.setText(Integer.toString(player[i].getCurrentScore()));
                 break;
            case 2:
                 TurnLabel2.setText(Integer.toString(player[i].getTurnLeft()));
                 ScoreLabel2.setText(Integer.toString(player[i].getCurrentScore()));
                 break;  
            case 3:
                 TurnLabel3.setText(Integer.toString(player[i].getTurnLeft()));
                 ScoreLabel3.setText(Integer.toString(player[i].getCurrentScore()));
                 break;  
            case 4:
                 TurnLabel4.setText(Integer.toString(player[i].getTurnLeft()));
                 ScoreLabel4.setText(Integer.toString(player[i].getCurrentScore()));
                 break;    
            }
                    
         }
         i=roundIndex-1;
         ResultLabel.setText("Player "+(i+1)+" start first!");
         LetterPanel.setEnabled(false);
         this.update();
            
    }

    private void runGame(){
        int continueToSpin=1;
        int letters ;
        switch(curSpin){                   
            case GET_A_TURN:                
                letters = b.guessLetter(guessChar);
                if(letters==0)
                {
                     player[i].reduceTurnLeft();
                     ResultLabel.setText("There were "+letters+" "
                             +guessChar+"'s in that phrase. Next Player!");
                     continueToSpin=0;
                }else{
                    player[i].setTurnLeft(player[i].getTurnLeft()+1);
                    updateAnswerPanel(index,b.getCurrentPhrase());
                    ResultLabel.setText("There were "+letters+" "
                            +guessChar+"'s in that phrase. You are added 1 more turn.");
                    continueToSpin=1;
                }
                if (!b.getCurrentPhrase().contains("_"))
                {
                    ResultLabel.setText("You guessed the phrase!");
                    updateAnswerPanel(index,b.getMyPhrase());
                    finish=1; 
                    player[i].setTotalScore(player[i].getCurrentScore());
                    if(player[i].getCurrentScore()==0) player[i].setTotalScore(500);
                    if(player[i].getCurrentScore()==0) player[i].setCurrentScore(500);
                    this.update();
                    continueToSpin=0;
                }
            break;
            default:
                letters = b.guessLetter(guessChar);
                int winnings = letters * curSpin;
                if (letters==0) 
                {
                    player[i].reduceTurnLeft();
                    continueToSpin=0;
                }
                updateAnswerPanel(index,b.getCurrentPhrase());
                player[i].addPoints(winnings);
                if(letters==0){
                     ResultLabel.setText("There were "+letters+" "
                             +guessChar+"'s in that phrase. Next Player!");
                 }else{
                     ResultLabel.setText("There were "+letters+" "
                             +guessChar+"'s in that phrase. You won: "+winnings+"\n");
                 }
                 if (!b.getCurrentPhrase().contains("_"))
                 {
                     ResultLabel.setText("You guessed the phrase!");
                     updateAnswerPanel(index,b.getMyPhrase());
                     finish=1; 
                     player[i].setTotalScore(player[i].getCurrentScore());
                     continueToSpin=0;
                 }
            break;
        }
        this.update();
        if (finish==0)
                {
                  finish=1;
                  isOutOfTurn=false;
                  for(int j=0;j<numberPlayer;j++)
                        {
                            if(player[j].getTurnLeft()!=0) finish=0;
                        }                  
                  if (finish==1) isOutOfTurn=true;
                }
        if (finish==1){
                for(int j=0;j<numberPlayer;j++)
                {
                    System.out.println("player"+(j+1)+
                            "'s total score: $"+player[j].getTotalScore());
                }
                if (isOutOfTurn){
                    JOptionPane.showMessageDialog(null, 
                            "ROUND OVER!\nThere is no winner.");
                }else{
                    JOptionPane.showMessageDialog(null, 
                            "ROUND OVER!\nThe winner is Player "+ (i+1)+".");
                }
                this.setVisible(false);
                roundIndex++;
                if(roundIndex<=numberOfRound){
                        GameGUI game=new GameGUI(numberPlayer,player,b,present);
                        game.setVisible(true);
                }else{
                        SpecialRound specialround=new SpecialRound(numberPlayer,player,b);
                        specialround.setVisible(true);
                }
        }
        if (continueToSpin==0){
            do{
            i++;
            if (i==numberPlayer) i=0;
            }while(player[i].getTurnLeft()==0);
        }
        GuessButton.setEnabled(true);
        PowerBar.setEnabled(true);
        LetterPanel.setEnabled(false);
        this.update();
       }

    
       private void updateAnswerPanel(int start, String str){
        int length=str.length();
        for (int k=0;k<=length-1;k++){
            setAnswerColor(k+1+start,Color.WHITE);
            if (str.charAt(k)==' '){
                setAnswerColor(k+1+start,Color.YELLOW);
            }else{
            setAnswerText(k+1+start,str.charAt(k));
            }
        }
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        InfoPanel = new javax.swing.JPanel();
        PlayerPanel1 = new javax.swing.JPanel();
        PlayerLabel1 = new javax.swing.JLabel();
        ScoreLabel1 = new javax.swing.JLabel();
        TurnLabel1 = new javax.swing.JLabel();
        PlayerPanel2 = new javax.swing.JPanel();
        PlayerLabel2 = new javax.swing.JLabel();
        ScoreLabel2 = new javax.swing.JLabel();
        TurnLabel2 = new javax.swing.JLabel();
        PlayerPanel3 = new javax.swing.JPanel();
        PlayerLabel3 = new javax.swing.JLabel();
        ScoreLabel3 = new javax.swing.JLabel();
        TurnLabel3 = new javax.swing.JLabel();
        PlayerPanel4 = new javax.swing.JPanel();
        PlayerLabel4 = new javax.swing.JLabel();
        ScoreLabel4 = new javax.swing.JLabel();
        TurnLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        AnswerPanel = new javax.swing.JPanel();
        RoundLabel = new javax.swing.JLabel();
        QuestionLabel = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        AnswerChar1 = new javax.swing.JLabel();
        AnswerChar2 = new javax.swing.JLabel();
        AnswerChar3 = new javax.swing.JLabel();
        AnswerChar4 = new javax.swing.JLabel();
        AnswerChar5 = new javax.swing.JLabel();
        AnswerChar6 = new javax.swing.JLabel();
        AnswerChar7 = new javax.swing.JLabel();
        AnswerChar8 = new javax.swing.JLabel();
        AnswerChar9 = new javax.swing.JLabel();
        AnswerChar10 = new javax.swing.JLabel();
        AnswerChar11 = new javax.swing.JLabel();
        AnswerChar12 = new javax.swing.JLabel();
        AnswerChar13 = new javax.swing.JLabel();
        AnswerChar14 = new javax.swing.JLabel();
        AnswerChar15 = new javax.swing.JLabel();
        AnswerChar17 = new javax.swing.JLabel();
        AnswerChar21 = new javax.swing.JLabel();
        AnswerChar16 = new javax.swing.JLabel();
        AnswerChar18 = new javax.swing.JLabel();
        AnswerChar22 = new javax.swing.JLabel();
        AnswerChar19 = new javax.swing.JLabel();
        AnswerChar20 = new javax.swing.JLabel();
        GuessButton = new javax.swing.JButton();
        GuessAnswer = new javax.swing.JTextField();
        LetterPanel = new javax.swing.JPanel();
        ButtonA = new javax.swing.JButton();
        ButtonB = new javax.swing.JButton();
        ButtonC = new javax.swing.JButton();
        ButtonD = new javax.swing.JButton();
        ButtonE = new javax.swing.JButton();
        ButtonF = new javax.swing.JButton();
        ButtonG = new javax.swing.JButton();
        ButtonH = new javax.swing.JButton();
        ButtonI = new javax.swing.JButton();
        ButtonJ = new javax.swing.JButton();
        ButtonK = new javax.swing.JButton();
        ButtonL = new javax.swing.JButton();
        ButtonM = new javax.swing.JButton();
        ButtonN = new javax.swing.JButton();
        ButtonO = new javax.swing.JButton();
        ButtonP = new javax.swing.JButton();
        ButtonQ = new javax.swing.JButton();
        ButtonR = new javax.swing.JButton();
        ButtonS = new javax.swing.JButton();
        ButtonT = new javax.swing.JButton();
        ButtonU = new javax.swing.JButton();
        ButtonV = new javax.swing.JButton();
        ButtonW = new javax.swing.JButton();
        ButtonX = new javax.swing.JButton();
        ButtonY = new javax.swing.JButton();
        ButtonZ = new javax.swing.JButton();
        ResultSpinPanel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        ResultLabel = new javax.swing.JLabel();
        WheelPanel = new javax.swing.JPanel();
        PowerBar = new javax.swing.JProgressBar();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Game");
        setBackground(new java.awt.Color(102, 255, 255));
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(252, 232, 161));

        PlayerLabel1.setBackground(new java.awt.Color(255, 255, 255));
        PlayerLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        PlayerLabel1.setText(" Player 1");

        TurnLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout PlayerPanel1Layout = new javax.swing.GroupLayout(PlayerPanel1);
        PlayerPanel1.setLayout(PlayerPanel1Layout);
        PlayerPanel1Layout.setHorizontalGroup(
            PlayerPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayerPanel1Layout.createSequentialGroup()
                .addComponent(PlayerLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ScoreLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TurnLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        PlayerPanel1Layout.setVerticalGroup(
            PlayerPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PlayerLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(PlayerPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ScoreLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(TurnLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        PlayerLabel2.setBackground(new java.awt.Color(255, 255, 255));
        PlayerLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        PlayerLabel2.setText(" Player 2");

        TurnLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout PlayerPanel2Layout = new javax.swing.GroupLayout(PlayerPanel2);
        PlayerPanel2.setLayout(PlayerPanel2Layout);
        PlayerPanel2Layout.setHorizontalGroup(
            PlayerPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayerPanel2Layout.createSequentialGroup()
                .addComponent(PlayerLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ScoreLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TurnLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 39, Short.MAX_VALUE))
        );
        PlayerPanel2Layout.setVerticalGroup(
            PlayerPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PlayerLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PlayerPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ScoreLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(TurnLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        PlayerLabel3.setBackground(new java.awt.Color(255, 255, 255));
        PlayerLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        PlayerLabel3.setText(" Player 3");

        TurnLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout PlayerPanel3Layout = new javax.swing.GroupLayout(PlayerPanel3);
        PlayerPanel3.setLayout(PlayerPanel3Layout);
        PlayerPanel3Layout.setHorizontalGroup(
            PlayerPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayerPanel3Layout.createSequentialGroup()
                .addComponent(PlayerLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ScoreLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TurnLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 56, Short.MAX_VALUE))
        );
        PlayerPanel3Layout.setVerticalGroup(
            PlayerPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PlayerLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(PlayerPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ScoreLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(TurnLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        PlayerLabel4.setBackground(new java.awt.Color(255, 255, 255));
        PlayerLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        PlayerLabel4.setText(" Player 4");

        TurnLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout PlayerPanel4Layout = new javax.swing.GroupLayout(PlayerPanel4);
        PlayerPanel4.setLayout(PlayerPanel4Layout);
        PlayerPanel4Layout.setHorizontalGroup(
            PlayerPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayerPanel4Layout.createSequentialGroup()
                .addComponent(PlayerLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ScoreLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TurnLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 56, Short.MAX_VALUE))
        );
        PlayerPanel4Layout.setVerticalGroup(
            PlayerPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PlayerLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(PlayerPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(ScoreLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(TurnLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel1.setText("Score");

        jLabel2.setText("Turn");

        jLabel3.setText(" Score");

        jLabel4.setText("Turn");

        javax.swing.GroupLayout InfoPanelLayout = new javax.swing.GroupLayout(InfoPanel);
        InfoPanel.setLayout(InfoPanelLayout);
        InfoPanelLayout.setHorizontalGroup(
            InfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InfoPanelLayout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(107, 107, 107))
            .addGroup(InfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(InfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(PlayerPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PlayerPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                .addGroup(InfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PlayerPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PlayerPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52))
        );
        InfoPanelLayout.setVerticalGroup(
            InfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InfoPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(InfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PlayerPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PlayerPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PlayerPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PlayerPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        AnswerPanel.setOpaque(false);

        RoundLabel.setBackground(new java.awt.Color(255, 255, 51));
        RoundLabel.setFont(new java.awt.Font("Times New Roman", 3, 24)); // NOI18N
        RoundLabel.setForeground(new java.awt.Color(0, 0, 255));
        RoundLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        RoundLabel.setText("Round");

        QuestionLabel.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        QuestionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        QuestionLabel.setText("Question");

        jPanel2.setBackground(new java.awt.Color(153, 255, 255));

        AnswerChar1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar17.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar19.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        AnswerChar20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        AnswerChar20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(AnswerChar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar15, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar17, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar18, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar19, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar20, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar21, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AnswerChar22, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AnswerChar22, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar21, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar18, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar17, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar16, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar6, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar7, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar9, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar11, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar12, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar14, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar15, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar19, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AnswerChar20, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout AnswerPanelLayout = new javax.swing.GroupLayout(AnswerPanel);
        AnswerPanel.setLayout(AnswerPanelLayout);
        AnswerPanelLayout.setHorizontalGroup(
            AnswerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, AnswerPanelLayout.createSequentialGroup()
                .addGroup(AnswerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(AnswerPanelLayout.createSequentialGroup()
                        .addComponent(RoundLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(QuestionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(AnswerPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(34, 34, 34))
        );
        AnswerPanelLayout.setVerticalGroup(
            AnswerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AnswerPanelLayout.createSequentialGroup()
                .addGroup(AnswerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(RoundLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(QuestionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GuessButton.setText("Guess");
        GuessButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuessButtonActionPerformed(evt);
            }
        });

        LetterPanel.setOpaque(false);

        ButtonA.setText("A");
        ButtonA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonAMousePressed(evt);
            }
        });

        ButtonB.setText("B");
        ButtonB.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonBMousePressed(evt);
            }
        });

        ButtonC.setText("C");
        ButtonC.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonCMousePressed(evt);
            }
        });

        ButtonD.setText("D");
        ButtonD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonDMousePressed(evt);
            }
        });

        ButtonE.setText("E");
        ButtonE.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonEMousePressed(evt);
            }
        });

        ButtonF.setText("F");
        ButtonF.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonFMousePressed(evt);
            }
        });

        ButtonG.setText("G");
        ButtonG.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonGMousePressed(evt);
            }
        });

        ButtonH.setText("H");
        ButtonH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonHMousePressed(evt);
            }
        });

        ButtonI.setText("I");
        ButtonI.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonIMousePressed(evt);
            }
        });

        ButtonJ.setText("J");
        ButtonJ.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonJMousePressed(evt);
            }
        });

        ButtonK.setText("K");
        ButtonK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonKMousePressed(evt);
            }
        });

        ButtonL.setText("L");
        ButtonL.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonLMousePressed(evt);
            }
        });

        ButtonM.setText("M");
        ButtonM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonMMousePressed(evt);
            }
        });

        ButtonN.setText("N");
        ButtonN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonNMousePressed(evt);
            }
        });

        ButtonO.setText("O");
        ButtonO.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonOMousePressed(evt);
            }
        });

        ButtonP.setText("P");
        ButtonP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonPMousePressed(evt);
            }
        });

        ButtonQ.setText("Q");
        ButtonQ.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonQMousePressed(evt);
            }
        });

        ButtonR.setText("R");
        ButtonR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonRMousePressed(evt);
            }
        });

        ButtonS.setText("S");
        ButtonS.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonSMousePressed(evt);
            }
        });

        ButtonT.setText("T");
        ButtonT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonTMousePressed(evt);
            }
        });

        ButtonU.setText("U");
        ButtonU.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonUMousePressed(evt);
            }
        });

        ButtonV.setText("V");
        ButtonV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonVMousePressed(evt);
            }
        });

        ButtonW.setText("W");
        ButtonW.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonWMousePressed(evt);
            }
        });

        ButtonX.setText("X");
        ButtonX.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonXMousePressed(evt);
            }
        });

        ButtonY.setText("Y");
        ButtonY.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonYMousePressed(evt);
            }
        });

        ButtonZ.setText("Z");
        ButtonZ.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ButtonZMousePressed(evt);
            }
        });

        javax.swing.GroupLayout LetterPanelLayout = new javax.swing.GroupLayout(LetterPanel);
        LetterPanel.setLayout(LetterPanelLayout);
        LetterPanelLayout.setHorizontalGroup(
            LetterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LetterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(LetterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LetterPanelLayout.createSequentialGroup()
                        .addGroup(LetterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(LetterPanelLayout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addComponent(ButtonU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonW, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(LetterPanelLayout.createSequentialGroup()
                                .addComponent(ButtonA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(LetterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ButtonF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ButtonX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(LetterPanelLayout.createSequentialGroup()
                        .addComponent(ButtonK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(LetterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LetterPanelLayout.createSequentialGroup()
                        .addComponent(ButtonG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonH))
                    .addGroup(LetterPanelLayout.createSequentialGroup()
                        .addGroup(LetterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(ButtonY, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ButtonQ, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(LetterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ButtonR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ButtonZ, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(LetterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ButtonI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ButtonS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(LetterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ButtonJ, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ButtonT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        LetterPanelLayout.setVerticalGroup(
            LetterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LetterPanelLayout.createSequentialGroup()
                .addGroup(LetterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ButtonE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ButtonA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(LetterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ButtonG, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonH, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonJ, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonF, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(LetterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ButtonL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(LetterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ButtonN, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonQ, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonR, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButtonM, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(LetterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ButtonV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ButtonW, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ButtonX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ButtonY, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ButtonZ, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        ResultSpinPanel.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        ResultSpinPanel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ResultSpinPanel.setText("Result");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("You landed on:");

        ResultLabel.setBackground(new java.awt.Color(0, 0, 0));
        ResultLabel.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        ResultLabel.setForeground(new java.awt.Color(0, 255, 0));
        ResultLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ResultLabel.setOpaque(true);

        WheelPanel.setOpaque(false);

        PowerBar.setBackground(new java.awt.Color(255, 255, 255));
        PowerBar.setForeground(new java.awt.Color(255, 0, 0));
        PowerBar.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 0)));
        PowerBar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        PowerBar.setOpaque(true);
        PowerBar.setString("");
        PowerBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                PowerBarMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                PowerBarMouseReleased(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("SPIN THE WHEEL");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/wof/black_arrow_png_by_mjmoonwalkerfan-d5ddhxk.png"))); // NOI18N

        javax.swing.GroupLayout WheelPanelLayout = new javax.swing.GroupLayout(WheelPanel);
        WheelPanel.setLayout(WheelPanelLayout);
        WheelPanelLayout.setHorizontalGroup(
            WheelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WheelPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(WheelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PowerBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WheelPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
        WheelPanelLayout.setVerticalGroup(
            WheelPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WheelPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PowerBar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(AnswerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 717, Short.MAX_VALUE)
            .addComponent(InfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(116, 116, 116)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ResultSpinPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(LetterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(52, 52, 52)
                                .addComponent(ResultLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(GuessAnswer, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(GuessButton)
                        .addGap(106, 106, 106)))
                .addComponent(WheelPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(AnswerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ResultSpinPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ResultLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(LetterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(GuessButton)
                            .addComponent(GuessAnswer, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 39, Short.MAX_VALUE))
                    .addComponent(WheelPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(InfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonZMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonZMousePressed
        // TODO add your handling code here:
        if(ButtonZ.isEnabled() && LetterPanel.isEnabled()){
            guessChar='Z';
            ButtonZ.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonZMousePressed

    private void ButtonYMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonYMousePressed
        // TODO add your handling code here:
        if(ButtonY.isEnabled() && LetterPanel.isEnabled()){
            guessChar='Y';
            ButtonY.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonYMousePressed

    private void ButtonXMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonXMousePressed
        // TODO add your handling code here:
        if(ButtonX.isEnabled() && LetterPanel.isEnabled()){
            guessChar='X';
            ButtonX.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonXMousePressed

    private void ButtonWMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonWMousePressed
        // TODO add your handling code here:
        if(ButtonW.isEnabled() && LetterPanel.isEnabled()){
            guessChar='W';
            ButtonW.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonWMousePressed

    private void ButtonVMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonVMousePressed
        // TODO add your handling code here:
        if(ButtonV.isEnabled() && LetterPanel.isEnabled()){
            guessChar='V';
            ButtonV.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonVMousePressed

    private void ButtonUMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonUMousePressed
        // TODO add your handling code here:
        if(ButtonU.isEnabled() && LetterPanel.isEnabled()){
            guessChar='U';
            ButtonU.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonUMousePressed

    private void ButtonTMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonTMousePressed
        // TODO add your handling code here:
        if(ButtonT.isEnabled() && LetterPanel.isEnabled()){
            guessChar='T';
            ButtonT.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonTMousePressed

    private void ButtonSMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonSMousePressed
        // TODO add your handling code here:
        if(ButtonS.isEnabled() && LetterPanel.isEnabled()){
            guessChar='S';
            ButtonS.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonSMousePressed

    private void ButtonRMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonRMousePressed
        // TODO add your handling code here:
        if(ButtonR.isEnabled() && LetterPanel.isEnabled()){
            guessChar='R';
            ButtonR.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonRMousePressed

    private void ButtonQMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonQMousePressed
        // TODO add your handling code here:
        if(ButtonQ.isEnabled() && LetterPanel.isEnabled()){
            guessChar='Q';
            ButtonQ.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonQMousePressed

    private void ButtonPMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonPMousePressed
        // TODO add your handling code here:
        if(ButtonP.isEnabled() && LetterPanel.isEnabled()){
            guessChar='P';
            ButtonP.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonPMousePressed

    private void ButtonOMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonOMousePressed
        // TODO add your handling code here:
        if(ButtonO.isEnabled() && LetterPanel.isEnabled()){
            guessChar='O';
            ButtonO.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonOMousePressed

    private void ButtonNMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonNMousePressed
        // TODO add your handling code here:
        if(ButtonN.isEnabled() && LetterPanel.isEnabled()){
            guessChar='N';
            ButtonN.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonNMousePressed

    private void ButtonMMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonMMousePressed
        // TODO add your handling code here:
        if(ButtonM.isEnabled() && LetterPanel.isEnabled()){
            guessChar='M';
            ButtonM.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonMMousePressed

    private void ButtonLMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonLMousePressed
        // TODO add your handling code here:
        if(ButtonL.isEnabled() && LetterPanel.isEnabled()){
            guessChar='L';
            ButtonL.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonLMousePressed

    private void ButtonKMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonKMousePressed
        // TODO add your handling code here:
        if(ButtonK.isEnabled() && LetterPanel.isEnabled()){
            guessChar='K';
            ButtonK.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonKMousePressed

    private void ButtonJMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonJMousePressed
        // TODO add your handling code here:
        if(ButtonJ.isEnabled() && LetterPanel.isEnabled()){
            guessChar='J';
            ButtonJ.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonJMousePressed

    private void ButtonIMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonIMousePressed
        // TODO add your handling code here:
        if(ButtonI.isEnabled() && LetterPanel.isEnabled()){
            guessChar='I';
            ButtonI.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonIMousePressed

    private void ButtonHMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonHMousePressed
        // TODO add your handling code here:
        if(ButtonH.isEnabled() && LetterPanel.isEnabled()){
            guessChar='H';
            ButtonH.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonHMousePressed

    private void ButtonGMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonGMousePressed
        // TODO add your handling code here:
        if(ButtonG.isEnabled() && LetterPanel.isEnabled()){
            guessChar='G';
            ButtonG.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonGMousePressed

    private void ButtonFMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonFMousePressed
        // TODO add your handling code here:
        if(ButtonF.isEnabled() && LetterPanel.isEnabled()){
            guessChar='F';
            ButtonF.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonFMousePressed

    private void ButtonEMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonEMousePressed
        // TODO add your handling code here:
        if(ButtonE.isEnabled() && LetterPanel.isEnabled()){
            guessChar='E';
            ButtonE.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonEMousePressed

    private void ButtonDMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonDMousePressed
        // TODO add your handling code here:
        if(ButtonD.isEnabled() && LetterPanel.isEnabled()){
            guessChar='D';
            ButtonD.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonDMousePressed

    private void ButtonCMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonCMousePressed
        // TODO add your handling code here:
        if(ButtonC.isEnabled() && LetterPanel.isEnabled()){
            guessChar='C';
            ButtonC.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonCMousePressed

    private void ButtonBMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonBMousePressed
        // TODO add your handling code here:
        if(ButtonB.isEnabled() && LetterPanel.isEnabled()){
            guessChar='B';
            ButtonB.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonBMousePressed

    private void ButtonAMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ButtonAMousePressed
        // TODO add your handling code here:
        if(ButtonA.isEnabled() && LetterPanel.isEnabled()){
            guessChar='A';
            ButtonA.setEnabled(false);
            this.runGame();
        }
    }//GEN-LAST:event_ButtonAMousePressed

    private void GuessButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuessButtonActionPerformed
        // TODO add your handling code here:
        if(GuessButton.isEnabled()){
            if(GuessAnswer.getText().length()>0){
                String tempString = GuessAnswer.getText();
                if (b.guessPhrase(tempString))
                {
                    System.out.println("You guessed"
                        + " the phrase!");
                    ResultLabel.setText("You guessed"
                        + " the phrase!");
                    updateAnswerPanel(index,b.getMyPhrase());
                    finish=1;
                    //                 b.newPhrase();
                    System.out.println("The Winner is Player:"
                        + i );
                    player[i].setTotalScore(player[i].getCurrentScore());
                    this.update();
                }
                else
                {
                    System.out.println("You guessed wrong!");
                    ResultLabel.setText("You guessed wrong! Next Player!");
                    player[i].setTurnLeft(0);
                    this.update();
                }
                if (finish==0)
                {
                    finish=1;
                    isOutOfTurn=false;
                    for(int j=0;j<numberPlayer;j++)
                    {
                        if(player[j].getTurnLeft()!=0) finish=0;
                    }
                    if (finish==1) isOutOfTurn=true;
                }
                if (finish==1){
                    if (isOutOfTurn){
                        JOptionPane.showMessageDialog(null, "ROUND OVER!\nThere is no winner.");
                    }else{
                        if(player[i].getCurrentScore()==0) player[i].setTotalScore(500);
                        if(player[i].getCurrentScore()==0) player[i].setCurrentScore(500);
                        this.update();
                        JOptionPane.showMessageDialog(null, "ROUND OVER!\nThe winner is Player "+ (i+1)+".");
                    }
                    this.setVisible(false);
                    //                 b.newPhrase();
                    roundIndex++;
                    if(roundIndex<=numberOfRound){
                        GameGUI game=new GameGUI(numberPlayer,player,b,present);
                        game.setVisible(true);
                    }else{
                        SpecialRound specialround=new SpecialRound(numberPlayer,player,b);
                        specialround.setVisible(true);
                    }

                }else{
                    do{
                        i++;
                        if (i==numberPlayer) i=0;
                    }while(player[i].getTurnLeft()==0);
                    this.update();
                }
            }
        }
    }//GEN-LAST:event_GuessButtonActionPerformed

    private void PowerBarMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PowerBarMousePressed
        // TODO add your handling code here:
        x=evt.getPoint();
        PowerBar.setValue(0);
        PowerBar.setStringPainted(true);
    }//GEN-LAST:event_PowerBarMousePressed

    private void PowerBarMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PowerBarMouseReleased
        // TODO add your handling code here:
        if(PowerBar.isEnabled()){
            y=evt.getPoint();
            int x1=(int)Math.round(x.getX());
            int y1=(int)Math.round(y.getX());
            value= (int)(((float)(y1-x1)/PowerBar.getWidth())*100);
            PowerBar.setValue(value); 
            if (value>1){
                Run();
                curSpin=spinResult(angleCal);
                ResultLabel.setText("");
                boolean isPresent=false;    
                GuessButton.setEnabled(false);    
                if (curSpin == BANK_RUPT){
                    ResultSpinPanel.setText("BANK RUPT");
                }else if (curSpin==LOSE_A_TURN){
                    ResultSpinPanel.setText("LOSE A TURN");
                }else if (curSpin==GET_A_TURN){
                    ResultSpinPanel.setText("GET A TURN");
                }else if (curSpin==PRIZE){
                    ResultSpinPanel.setText("PRIZE");
                }else{
                    ResultSpinPanel.setText("$"+curSpin);
                }
                PowerBar.setEnabled(false);
                if(curSpin == BANK_RUPT || curSpin==LOSE_A_TURN || curSpin == PRIZE){
                switch(curSpin){
                    case PRIZE:
                        ResultLabel.setText("You are add $"+present+"!");
                        player[i].addPoints(present);
                        isPresent=true;
                    break;
                    case BANK_RUPT:
                        System.out.println("Sorry you landed on Bankrupt.");
                        player[i].addPoints(player[i].getCurrentScore() * -1);
                        ResultLabel.setText("Next Player!");
                    break;
                                
                    case LOSE_A_TURN:
                        System.out.println("Sorry you had lose a turn.");
                        player[i].reduceTurnLeft();
                        ResultLabel.setText("Next Player!");
                break;
                }
            this.update();
            if (finish==0)
                {
                  finish=1;
                  isOutOfTurn=false;
                  for(int j=0;j<numberPlayer;j++)
                        {
                            if(player[j].getTurnLeft()!=0) finish=0;
                        }                  
                  if (finish==1) isOutOfTurn=true;
              }
            if (finish==1){
            System.out.println("Round Over!");
            for(int j=0;j<numberPlayer;j++)
                {
                    System.out.println("Player"+ (j+1) +
                            "'s total score: $"+player[j].getTotalScore());
                }
                if (isOutOfTurn){
                    JOptionPane.showMessageDialog(null, "ROUND OVER!\nThere is no winner.");
                }else{
                    JOptionPane.showMessageDialog(null, "ROUND OVER!\nThe winner is Player "+ (i+1)+".");
                }
                    this.setVisible(false);
  //              b.newPhrase();
                    roundIndex++;
                    if(roundIndex<=numberOfRound){
                        GameGUI game=new GameGUI(numberPlayer,player,b,present);
                        game.setVisible(true);
                    }else{
                        SpecialRound specialround=new SpecialRound(numberPlayer,player,b);
                        specialround.setVisible(true);
                    }
                
        }
        do{
            if (isPresent==false){
            i++;
            if (i==numberPlayer) i=0;            
            }else{
                isPresent=false;
            }
        }while(player[i].getTurnLeft()==0);
        PowerBar.setEnabled(true);
        LetterPanel.setEnabled(false);
        this.update();
        
        }else{
                           LetterPanel.setEnabled(true);
        }
      }    
      }
        
    }//GEN-LAST:event_PowerBarMouseReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GameGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                new GameGUI(numberPlayer,theme).setVisible(true);
//                System.out.println("Success");
         
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AnswerChar1;
    private javax.swing.JLabel AnswerChar10;
    private javax.swing.JLabel AnswerChar11;
    private javax.swing.JLabel AnswerChar12;
    private javax.swing.JLabel AnswerChar13;
    private javax.swing.JLabel AnswerChar14;
    private javax.swing.JLabel AnswerChar15;
    private javax.swing.JLabel AnswerChar16;
    private javax.swing.JLabel AnswerChar17;
    private javax.swing.JLabel AnswerChar18;
    private javax.swing.JLabel AnswerChar19;
    private javax.swing.JLabel AnswerChar2;
    private javax.swing.JLabel AnswerChar20;
    private javax.swing.JLabel AnswerChar21;
    private javax.swing.JLabel AnswerChar22;
    private javax.swing.JLabel AnswerChar3;
    private javax.swing.JLabel AnswerChar4;
    private javax.swing.JLabel AnswerChar5;
    private javax.swing.JLabel AnswerChar6;
    private javax.swing.JLabel AnswerChar7;
    private javax.swing.JLabel AnswerChar8;
    private javax.swing.JLabel AnswerChar9;
    private javax.swing.JPanel AnswerPanel;
    private javax.swing.JButton ButtonA;
    private javax.swing.JButton ButtonB;
    private javax.swing.JButton ButtonC;
    private javax.swing.JButton ButtonD;
    private javax.swing.JButton ButtonE;
    private javax.swing.JButton ButtonF;
    private javax.swing.JButton ButtonG;
    private javax.swing.JButton ButtonH;
    private javax.swing.JButton ButtonI;
    private javax.swing.JButton ButtonJ;
    private javax.swing.JButton ButtonK;
    private javax.swing.JButton ButtonL;
    private javax.swing.JButton ButtonM;
    private javax.swing.JButton ButtonN;
    private javax.swing.JButton ButtonO;
    private javax.swing.JButton ButtonP;
    private javax.swing.JButton ButtonQ;
    private javax.swing.JButton ButtonR;
    private javax.swing.JButton ButtonS;
    private javax.swing.JButton ButtonT;
    private javax.swing.JButton ButtonU;
    private javax.swing.JButton ButtonV;
    private javax.swing.JButton ButtonW;
    private javax.swing.JButton ButtonX;
    private javax.swing.JButton ButtonY;
    private javax.swing.JButton ButtonZ;
    private javax.swing.JTextField GuessAnswer;
    private javax.swing.JButton GuessButton;
    private javax.swing.JPanel InfoPanel;
    private javax.swing.JPanel LetterPanel;
    private javax.swing.JLabel PlayerLabel1;
    private javax.swing.JLabel PlayerLabel2;
    private javax.swing.JLabel PlayerLabel3;
    private javax.swing.JLabel PlayerLabel4;
    private javax.swing.JPanel PlayerPanel1;
    private javax.swing.JPanel PlayerPanel2;
    private javax.swing.JPanel PlayerPanel3;
    private javax.swing.JPanel PlayerPanel4;
    private javax.swing.JProgressBar PowerBar;
    private javax.swing.JLabel QuestionLabel;
    private javax.swing.JLabel ResultLabel;
    private javax.swing.JLabel ResultSpinPanel;
    private javax.swing.JLabel RoundLabel;
    private javax.swing.JLabel ScoreLabel1;
    private javax.swing.JLabel ScoreLabel2;
    private javax.swing.JLabel ScoreLabel3;
    private javax.swing.JLabel ScoreLabel4;
    private javax.swing.JLabel TurnLabel1;
    private javax.swing.JLabel TurnLabel2;
    private javax.swing.JLabel TurnLabel3;
    private javax.swing.JLabel TurnLabel4;
    private javax.swing.JPanel WheelPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static java.util.Objects.isNull;

public class GameFrame extends AnimationFrame implements WindowListener,Runnable,ActionListener{
    public static final int FRAME_W = 800;      
    public static final int FRAME_H = 600;
    Thread th = null;
    String def = "?";
    String[] numbers = new String[]{"1", "1", "2", "2", "3", "3", "4", "4", "5"};

    String selectedNumber = null;
    Integer firstSelectedButtonNumber = null;
    Integer secondSelectedButtonNumber = null;

    Button[] bt = new Button[9];

    Panel centerPanel = new Panel();

    Button newGame = new Button("new");
    Button resetGame = new Button("reset");

    Panel menuPanel = new Panel();

    Boolean answer = true;

    public GameFrame(String title){
        super(title, FRAME_W, FRAME_H);
        setSize(FRAME_W, FRAME_H);

        setLayout(new BorderLayout());

        centerPanel.setLayout(new GridLayout(3,3));
        for(int i = 0; i < bt.length; i++){
            bt[i] = new Button(def);
            bt[i].setFont(new Font("ＭＳ ゴシック", Font.BOLD, 30));
            centerPanel.add(bt[i]);
            bt[i].addActionListener(this); 
        }
        add(centerPanel,"Center");

        menuPanel.setLayout(new GridLayout(1, 2));
        menuPanel.add(newGame);
        menuPanel.add(resetGame);
        newGame.addActionListener(this);
        resetGame.addActionListener(this);
        add(menuPanel ,"North");

        addWindowListener(this);

        shuffle();
        
        th = new Thread(this);
        th.start();
        setVisible(true);
    }

    public void run(){
        while(th != null){
            repaint();

            try{
                Thread.sleep(30);
            } catch (InterruptedException e){
            }
        }
    }
    public void aniPaint(Graphics g){

    }

    public void update(Graphics g){
        paint(g);
        if (!answer) {
            try {
                answer = true;
                Thread.sleep(500);
                bt[firstSelectedButtonNumber].setLabel(def);
                bt[secondSelectedButtonNumber].setLabel(def);
                paint(g);
                selectedNumber = null;
                firstSelectedButtonNumber = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void actionPerformed(ActionEvent  evt){
        for(int i = 0; i < bt.length; i++){
            if(bt[i] == evt.getSource()){

                if (bt[i].getLabel().equals(def)) {
                    if (isNull(selectedNumber) && isNull(firstSelectedButtonNumber)) {
                        bt[i].setLabel(numbers[i]);
                        selectedNumber = bt[i].getLabel();
                        firstSelectedButtonNumber = i;
                        bt[i].setLabel(numbers[i]);
                    } else if (firstSelectedButtonNumber != i) {
                        if (selectedNumber.equals(numbers[i])) {
                            bt[i].setLabel(numbers[i]);
                            selectedNumber = null;
                            firstSelectedButtonNumber = null;

                        } else {
                            bt[i].setLabel(numbers[i]);
                            answer = false;
                            secondSelectedButtonNumber = i;
                        }

                    }
                }
            }
        }

        if (newGame == evt.getSource()) {
            shuffle();
            for (int i = 0; i < bt.length; i++) {
                bt[i].setLabel(def);
            }
            selectedNumber = null;
            firstSelectedButtonNumber = null;
        } else if (resetGame == evt.getSource()) {
            for (int i = 0; i < bt.length; i++) {
                bt[i].setLabel(def);
            }
            selectedNumber = null;
            firstSelectedButtonNumber = null;
        }
    }

    public void windowClosing(WindowEvent e){
         dispose();
    }
    public void windowOpened(WindowEvent e){}
    public void windowIconified(WindowEvent e){}
    public void windowDeiconified(WindowEvent e){}
    public void windowClosed(WindowEvent e){}
    public void windowActivated(WindowEvent e){}
    public void windowDeactivated(WindowEvent e){}

    private void shuffle() {
        if (numbers.length <= 1) {
            return;
        }

        Random rnd = ThreadLocalRandom.current();
        for (int i = numbers.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);

            String tmp = numbers[index];
            numbers[index] = numbers[i];
            numbers[i] = tmp;
        }
    }
}
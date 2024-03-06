import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class PlayBoard extends JPanel implements ActionListener {
    
	private static final long serialVersionUID = 1L;
	private final String HIGHSCORE_FILE = "src/highscore.txt";
	
	
    private final int Total_Dots = 1600;
    private final int Dot_Size = 10;
    private final int Random_POS = 32;

    private Image fruitapple;
    private Image snakehead;
    private Image snaketail;
    
    private int fruitapple_x;
    private int fruitapple_y;
    
    private final int x[] = new int[Total_Dots];
    private final int y[] = new int[Total_Dots];
    
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    
    private boolean running = true;
    
    private int score = 0;
    private int highestScore = 0;
    
    private int dots;
    private Timer timerSet;
    
    PlayBoard() {
        addKeyListener(new TAdapter());
        
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(400, 400));
        setFocusable(true);
        
        renderImages(); //rendering the images on board
        initGame(); // starting the game
        readHighScore(); // getting the highest score from file i.e. fetching his previous high score
    }
    
    private void readHighScore() {
        try {
            File file = new File(HIGHSCORE_FILE);
            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (!created) {
                    System.err.println("Failed to create the file");
                }
                highestScore = 0;
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String text = reader.readLine();
                    if (text != null) {
                        highestScore = Integer.parseInt(text);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the high score: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void renderImages() { //getting the images from the local system
        ImageIcon image1 = new ImageIcon(ClassLoader.getSystemResource("Icons/fruitapple.png"));
        fruitapple = image1.getImage();
        
        ImageIcon image2 = new ImageIcon(ClassLoader.getSystemResource("Icons/snaketail.png"));
        snaketail = image2.getImage();
        
        ImageIcon image3 = new ImageIcon(ClassLoader.getSystemResource("Icons/snakehead.png"));
        snakehead = image3.getImage();
    }
    
    public void initGame() {
        dots = 3;
        
        for (int i = 0; i < dots; i++) {
            y[i] = 70;
            x[i] = 70 - i * Dot_Size; 
        }
        
        PlaceApple();
        
        timerSet = new Timer(200, this);
        timerSet.start();
    }
    
    
    public void PlaceApple() { // using Math.random() to place the apple randomly on the board
        int z = (int)(Math.random() * Random_POS);
        fruitapple_x = z * Dot_Size;
                
        z = (int)(Math.random() * Random_POS);
        fruitapple_y = z * Dot_Size;
    }
    
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    
    public void draw(Graphics g) {
        if (running) {
            g.drawImage(fruitapple, fruitapple_x, fruitapple_y, this);

            for (int i = 0 ; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(snakehead, x[i], y[i], this); // placing the head of the snake
                } else {
                    g.drawImage(snaketail, x[i], y[i], this); // placing its tail
                }
            }

            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
            FinalScoreDisplay(g);
        }
    }
    
    public void FinalScoreDisplay(Graphics g) { // Displaying the score and highest score message
    	String message = "Your Score:";
        String highestScoreMsg = "Highest score:";
        Font font = new Font("Times New Roman", Font.BOLD, 14);
        
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(message+score, 150,120);
        g.drawString(highestScoreMsg+highestScore, 150, 140);
    }
    
    public void gameOver(Graphics g) { // Displaying the Game Over message
        String message = "Game Over!!";
        Font font = new Font("Times New Roman", Font.BOLD, 20);
        FontMetrics metrices = getFontMetrics(font);
        
        g.setFont(font);
        g.setColor(Color.RED);
        g.drawString(message, (400 - metrices.stringWidth(message)) / 2, 400/2);
    }
    
    public void movement() { 
        for (int i = dots ; i > 0 ; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        
        if (left) {
            x[0] = x[0] - Dot_Size;
        }
        if (right) {
            x[0] = x[0] + Dot_Size;
        }
        if (up) {
            y[0] = y[0] - Dot_Size;
        }
        if (down) {
            y[0] = y[0] + Dot_Size;
        }
    }
    
    public void checkApple() {
        if ((x[0] == fruitapple_x) && (y[0] == fruitapple_y)) {
        	score++; // increment the score
            dots++; // increment the tail dots
            if (score > highestScore) {
                highestScore = score;
                updateHighScore();
            }
            PlaceApple();
        }
    }
    
    public void checkCollision() {
        for(int i = dots; i > 0; i--) {
            if (( i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                running = false; // end the game if collision happens 
            }
        }
        
        if (y[0] >= 400 || y[0] < 0 || x[0] >= 400 || x[0] < 0) { // boundary conditions for collision
            running = false;
        }
        
        if (!running) {
            timerSet.stop();
        }
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (running) {
            checkApple();
            checkCollision();
            movement();
        }else {
            updateHighScore();
        }
        
        repaint();
    }
    
    private void updateHighScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGHSCORE_FILE))) {
            writer.write(Integer.toString(highestScore));
            writer.flush(); 
        } catch (IOException e) {
            System.err.println("Error updating the high score: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) { // to handle key events pressed 
        	int key = e.getKeyCode();

            switch (key) {
                case KeyEvent.VK_LEFT:
                    if (!right) {
                        left = true;
                        up = false;
                        down = false;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (!left) {
                        right = true;
                        up = false;
                        down = false;
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (!down) {
                        up = true;
                        left = false;
                        right = false;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (!up) {
                        down = true;
                        left = false;
                        right = false;
                    }
                    break;
            }
        }
    }
}
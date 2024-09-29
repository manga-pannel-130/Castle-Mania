import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleGame extends JPanel implements ActionListener, KeyListener {

    private Timer timer;
    private int x, y;                // Player position
    private int velocityY = 0;        // Vertical velocity
    private final int GRAVITY = 1;    // Gravity force
    private final int JUMP_FORCE = -15;  // Upward force for the jump
    private boolean isJumping = false;  // To check if the player is jumping
    private Image heroImage;
    private Image backgroundImage;
    private Image floorImage;         // New floor image (brick wall)

    private int FLOOR_HEIGHT;          // Height of the floor block
    private int GROUND_LEVEL;          // Y-position of the ground

    public SimpleGame() {
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));
        addKeyListener(this);

        // Load the hero image (ninja assassin)
        heroImage = new ImageIcon("hero.png").getImage();  // Make sure hero.png is in your project directory

        // Load the pixelated scenery background image
        backgroundImage = new ImageIcon("background.png").getImage();  // Make sure background.png is in your project directory

        // Load the brick wall image for the floor
        floorImage = new ImageIcon("brick.png").getImage();  // Make sure brick.png is in your project directory

        // Set initial floor height to 1/10th of the screen height
        FLOOR_HEIGHT = 60;
        GROUND_LEVEL = 600 - FLOOR_HEIGHT;  // Set initial ground level

        x = 100;
        y = GROUND_LEVEL - 50;  // Start the hero on the ground

        timer = new Timer(16, this);  // 60 FPS game loop
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the background image to fill the panel
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        // Draw the brick wall floor image
        for (int i = 0; i < getWidth(); i += 100) {  // Draw the floor across the entire width
            g.drawImage(floorImage, i, GROUND_LEVEL, 100, FLOOR_HEIGHT, this);  // Adjust size and position
        }

        // Draw the hero image on top of the background and floor
        g.drawImage(heroImage, x, y, 50, 50, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Apply gravity if the hero is not on the ground
        if (y + 50 < GROUND_LEVEL || isJumping) {
            velocityY += GRAVITY;
        }

        // Update the player's vertical position
        y += velocityY;

        // Stop falling when hitting the brick floor (ground level)
        if (y + 50 >= GROUND_LEVEL) {
            y = GROUND_LEVEL - 50;  // Place the player on the top of the brick floor
            velocityY = 0;    // Stop vertical movement when on the ground
            isJumping = false;  // The player is no longer jumping
        }

        // Prevent the player from falling off the screen
        if (y > GROUND_LEVEL) {
            y = GROUND_LEVEL - 50;
        }

        repaint();  // Redraw the screen
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            x -= 10;  // Move left
        } else if (key == KeyEvent.VK_RIGHT) {
            x += 10;  // Move right
        } else if ((key == KeyEvent.VK_SPACE || key == KeyEvent.VK_UP) && !isJumping) {
            // Only jump if the player is not already in the air
            velocityY = JUMP_FORCE;  // Apply the jump force
            isJumping = true;  // Set jumping to true
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Swing Game with Brick Floor and Jumping");
        SimpleGame game = new SimpleGame();
        
        // Set frame to be resizable
        frame.setResizable(true);
        
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        // Adjust the ground level when the frame is resized
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                game.updateGroundLevel();
            }
        });
    }
    
    // Method to update the ground level based on the current height of the window
    public void updateGroundLevel() {
        GROUND_LEVEL = getHeight() - FLOOR_HEIGHT;  // Adjust ground level to be 1/10th from the bottom
        y = GROUND_LEVEL - 50;  // Reset the character's position to stay on the ground
        repaint();  // Redraw the screen
    }
}

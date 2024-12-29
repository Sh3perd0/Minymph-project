package minymph_project;

import javax.swing.*;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;

public class GUIMap extends JPanel {

    private Map map;
    private final int TILE_SIZE = 20; // taille en pixels d'une tuile

    private Image water_1;
    private Image water_2;
    private Image grass;
    
    private int cameraX = 0; // Position X de la caméra
    private int cameraY = 0; // Position Y de la caméra

    public GUIMap(Map map) {
        
    	 water_1 = new ImageIcon("assets/water_1.png").getImage();
    	 water_2 = new ImageIcon("assets/water_2.png").getImage();
    	 grass = new ImageIcon("assets/grass.png").getImage();
    	this.map = map;

        // Ajout des listeners pour le clavier
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> moveCamera(0, -1);
                    case KeyEvent.VK_DOWN -> moveCamera(0, 1);
                    case KeyEvent.VK_LEFT -> moveCamera(-1, 0);
                    case KeyEvent.VK_RIGHT -> moveCamera(1, 0);
                }
            }
        });
    }

    private void moveCamera(int dx, int dy) {
        int maxX = map.getMap()[0].length - getWidth() / TILE_SIZE;
        int maxY = map.getMap().length - getHeight() / TILE_SIZE;

        cameraX = Math.max(0, Math.min(cameraX + dx, maxX));
        cameraY = Math.max(0, Math.min(cameraY + dy, maxY));

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int visibleCols = getWidth() / TILE_SIZE;
        int visibleRows = getHeight() / TILE_SIZE;


        for (int row = 0; row < visibleRows; row++) {
            for (int col = 0; col < visibleCols; col++) {
                int mapRow = cameraY + row;
                int mapCol = cameraX + col;

                if (mapRow < map.getMap().length && mapCol < map.getMap()[0].length) {
                    if (map.getMap()[mapRow][mapCol] == 0) {
 
                        g.drawImage(grass, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, this);
                    } else if (map.getMap()[mapRow][mapCol] == 1) {
                    	
                    	Random random = new Random();
                    	int randWater = random.nextInt(5);
                    	if (randWater==0)
                    	{
                    	g.drawImage(water_1, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, this);
                    	}
                    	else
                    	{
                    		g.drawImage(water_2, col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, this);
                    	}
                    } else if (map.getMap()[mapRow][mapCol] == 2) {
                        g.setColor(Color.BLACK); // Mur
                        g.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }

        g.setColor(Color.RED);
        int playerScreenX = (Game.getPlayerX() - cameraX) * TILE_SIZE;
        int playerScreenY = (Game.getPlayerY() - cameraY) * TILE_SIZE;
        g.fillRect(playerScreenX, playerScreenY, TILE_SIZE, TILE_SIZE);
    }

    public void display() {
        JFrame frame = new JFrame("Map");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int windowWidth = (int) (screenSize.width * 0.9);
        int windowHeight = (int) (screenSize.height * 0.9);

        frame.setSize(windowWidth, windowHeight);

        frame.setLocationRelativeTo(null);

        frame.add(this);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        requestFocusInWindow(); // Focus sur le JPanel pour les entrées clavier
    }
}

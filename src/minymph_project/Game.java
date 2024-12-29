package minymph_project;


public class Game 
{
	private static Map map;
	private static int playerX = 1;
	private static int playerY = 1;


public Game(Map map)
{
	this.map = map;
}

public static int getPlayerX()
{
	return playerX;
}

public static int getPlayerY()
{
	return playerX;
}


public void startGame()
{
	//surement de la gui ou quoi
	System.out.println("Welcome to Minymph Project\n");
	
	main(null);
}

private void movePlayer(int x, int y)
{
	int newX = playerX + x;
	int newY = playerY + y;
	 if (newX >= 0 && newX < map.getMap()[0].length && newY >= 0 && newY < map.getMap().length) {
         if (map.getMap()[newY][newX] != 1) { // Vérifie que ce n'est pas un mur
             playerX = newX;
             playerY = newY;
         }
     }
}



public static void main(String[] args) {
	GUIMap GUIMap = new GUIMap(map);
	GUIMap.display();

}



}

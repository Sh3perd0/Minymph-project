package minymph_project;

import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Map 
{
	private int[][] map;
	private final String cheminFichier = "assets/map.txt";
	private int[][] map1;

	
public Map()
{
	//map = createSquareMatrixWithBorders(150);
	
	try {
        map1 = lireMatrice(cheminFichier); //on lit la matrice du fichier
    } catch (IOException e) {
        e.printStackTrace();
    }
	map=map1;
	
	
}

	
public int[][] lireMatrice(String cheminFichier) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(cheminFichier));
    String line;
    int rowCount = 0;

    while ((line = reader.readLine()) != null) {
        if (!line.trim().isEmpty()) { 
            rowCount++;
        }
    }

    reader.close();
    reader = new BufferedReader(new FileReader(cheminFichier));

    int[][] mapRetenue = new int[rowCount][];
    int currentRow = 0;


    while ((line = reader.readLine()) != null) {
        if (!line.trim().isEmpty()) {  
            String[] values = line.trim().split("\\s+"); 
            int[] row = new int[values.length];

            for (int i = 0; i < values.length; i++) {
                try {
                    row[i] = Integer.parseInt(values[i].trim()); 
                } catch (NumberFormatException e) {
                    System.err.println("Erreur de format dans la ligne : " + line);
                    throw e; 
                }
            }
            mapRetenue[currentRow] = row;
            currentRow++;
        }
    }
    reader.close();

    return mapRetenue;
}

public int[][] getMap()
{
	return map;
}

public static int[][] createSquareMatrixWithBorders(int size) { //method to create random maps with rivers and lakes
									// when one is appreciated, I save it and copy it into a txt file
    int ySize = size + 50; 
    int[][] matrix = new int[size][ySize];
    Random random = new Random();


    for (int i = 0; i < size; i++) {
        for (int j = 0; j < ySize; j++) {
            if (i == 0 || i == size - 1 || j == 0 || j == ySize - 1) {
                matrix[i][j] = 2; // Walls at the borders
            } else {
                matrix[i][j] = 0;
            }
        }
    }

    
    int numberOfRivers = 2 + random.nextInt(2); 
    for (int r = 0; r < numberOfRivers; r++) {
        int startRow = random.nextInt(size - 2) + 1;
        int startCol = random.nextInt(ySize - 2) + 1;
        int length = random.nextInt(size / 4) + size / 8;
        int direction = random.nextBoolean() ? 1 : 0;
        int thickness = 1 + random.nextInt(2);

        for (int i = 0; i < length; i++) {
            for (int t = -thickness; t <= thickness; t++) {
                int adjustedRow = startRow + (direction == 0 ? t : 0);
                int adjustedCol = startCol + (direction == 1 ? t : 0);

                if (adjustedRow >= 1 && adjustedRow < size - 1 && adjustedCol >= 1 && adjustedCol < ySize - 1) {
                    matrix[adjustedRow][adjustedCol] = 1;
                }
            }
            if (direction == 0) {
                startRow += random.nextInt(3) - 1;
                startCol += 1;
            } else {
                startCol += random.nextInt(3) - 1;
                startRow += 1;
            }
        }
    }

    int numberOfLakes = 1 + random.nextInt(2);
    for (int l = 0; l < numberOfLakes; l++) {
        int lakeCenterRow = random.nextInt(size - 4) + 2;
        int lakeCenterCol = random.nextInt(ySize - 4) + 2;
        int lakeRadius = random.nextInt(2) + 10;

        for (int i = -lakeRadius; i <= lakeRadius; i++) {
            for (int j = -lakeRadius; j <= lakeRadius; j++) {
                if (lakeCenterRow + i >= 1 && lakeCenterRow + i < size - 1 &&
                    lakeCenterCol + j >= 1 && lakeCenterCol + j < ySize - 1) {

                    if (Math.sqrt(i * i + j * j) <= lakeRadius + random.nextInt(2)) {
                        matrix[lakeCenterRow + i][lakeCenterCol + j] = 1;
                    }
                }
            }
        }
    }

    return matrix;
}

public void updateMap(int x, int y, int value) {

    if (x >= 0 && x < map[0].length && y >= 0 && y < map.length) {
        map[y][x] = value;
    } else {
        System.out.println("Coordonnées hors limites : (" + x + ", " + y + ")");
    }
}


public void printMap() {
    for (int y = 0; y < map.length; y++) {
        for (int x = 0; x < map[y].length; x++) {
            System.out.print(map[y][x] + " ");
        }
        System.out.println();
    }
}






}




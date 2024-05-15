import java.util.Random;

public class Grid_NDTest {
    public static void main(String[] args) {

        int rows = 50;
        int cols = 50;
        Random random = new Random();
        Grid_ND grid = new Grid_ND(rows, cols);

        for (int i = 0; i < rows*cols*0.25; i++) 
        {
            grid.getCell(random.nextInt(rows), random.nextInt(cols)).setValue(true);
        }

        GrilleGraphique Grid_2D = new GrilleGraphique(grid.getDimensions()[0], grid.getDimensions()[1], 12);

        int i, j;
        for (i = 0; i < rows; i++) {
            for (j = 0; j < cols; j++) 
            {
                if (grid.getCell(i,j).getCellValue()) 
                {
                    Grid_2D.colorierCase(i, j);
                }
            }
        }
    }
}

public class Grid_NDTest
{
    public static void main(String[] args)
    {
        Grid_ND grid = new Grid_ND(50,50);

        int[] pos = {0, 2};
        System.out.println(grid.getCell(pos).getCellValue());
        grid.getCell(pos).setValue(true);
        System.out.println(grid.getCell(pos).getCellValue());
        System.out.println(grid);

        //On crée un objet ChampGraphique de 50 cases de large, et 60 de haut
        GrilleGraphique Grid_2D = new GrilleGraphique(grid.dimensions[0], grid.dimensions[1], 13);

        int i;
        for(i=0; i<30; i++)
        {
            int[] v = {i, 2};
            if(grid.getCell(v).getCellValue())
            {
                Grid_2D.colorierCase(i, 2);
            }
        }
    }
}
public class STRUCT_Grid_ND 
{
    private int[] dimensions; // number of dimensions V = (..., z, y, x) dans R^N
    private Object[] grid; // tab of grid or cells

    public STRUCT_Grid_ND(int... sizes)
    {
        dimensions = sizes;
        int N = sizes[0]; // Dimension size

        if (sizes.length == 1) 
        {
            grid = new STRUCT_Cell[N]; // Derniere dim tab cell
            for (int i = 0; i < N; i++) 
            {
                grid[i] = new STRUCT_Cell();
            }
        } 
        else 
        {
            grid = new STRUCT_Grid_ND[N];
            int[] newSizes = new int[sizes.length - 1];
            System.arraycopy(sizes, 1, newSizes, 0, sizes.length - 1);
            for (int i = 0; i < N; i++) {
                grid[i] = new STRUCT_Grid_ND(newSizes); // Recursive sous-grid
            }
        }
    }

    // Method to get the cell from a position
    public STRUCT_Cell getCell(int... pos) {
        if (pos.length == 1) {
            return ((STRUCT_Cell) grid[pos[0]]); // Get cell
        } else {
            int[] newPos = new int[pos.length - 1];
            System.arraycopy(pos, 1, newPos, 0, pos.length - 1);
            return ((STRUCT_Grid_ND) grid[pos[0]]).getCell(newPos); // Recursively get value from sub-grid
        }
    }

    public int[] getDimensions() {
        return dimensions;
    }

    public String toString()
    {
        String str = "********* GRID **********\n";
        str += "Dimensions : { ";
        for (int i = 0; i < dimensions.length; i++) 
        {
            str += dimensions[i] + " ";
        }
        str += "} | " + dimensions.length + "\n";
        /*for (int i = 0; i < grid.length; i++) 
        {
            if (grid[i] instanceof Cell) 
            {
                str += ((Cell) grid[i]).getCellValue() + " ";
            } 
            else 
            {
                str += "\n";
                str += ((Grid_ND) grid[i]).toString();
            }
        }*/
        return str;
    }
}

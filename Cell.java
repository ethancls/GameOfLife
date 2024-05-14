public class Cell
{
    private boolean value;

    public Cell() 
    {
        value = false; // Default value for a cell
    }
    public void setValue(boolean value)
    {
        this.value = value;
    }
    public boolean getCellValue()
    {
        return value;
    }
}
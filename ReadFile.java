import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadFile 
{
    /* Read char if letter G voisiage sinon operateur 
    puis lire jusqu'a la parentèse, espace ignoré, 
    chiffre jusqu'a une virgule ou une parenthese, 
    on crée les noeuds au fur et a mesure 
    
    ADD(8, SUB(3, MUL(4, 7))) , si erreur exception formule incorrecte
    

    if (leaf is const or voisinage G**)
    {
        return leaf
    }
    else 
    {
        Parsing(leaf)
    }
    */

    public void ParseFile(String path)
    {
        try 
        {
			FileReader fileReader = new FileReader(path);
			BufferedReader reader = new BufferedReader(fileReader);
			
			String line = reader.readLine();
			
			while (line != null) 
            {
				System.out.println(line);

                // Parsing


				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) 
        {
			e.printStackTrace();
		}
    }

    public static void main(String[] args) 
    {
        ReadFile readFile = new ReadFile();
        readFile.ParseFile("./configs/ex.txt");
    }
}


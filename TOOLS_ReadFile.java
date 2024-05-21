import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Random;
import java.awt.Color;

import javax.xml.parsers.DocumentBuilder;

public class TOOLS_ReadFile 
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
    public String path = "";
    public static String fileContent = "";
    public static int cursor = 0;

    public TOOLS_ReadFile(String path)
    {
        this.path = path;
        TOOLS_ReadFile.fileContent = readFileContent();
        System.out.println(fileContent);
    }

    private String readFileContent() {
        StringBuilder content = new StringBuilder();
        try 
        {
			FileReader fileReader = new FileReader(path);
			BufferedReader reader = new BufferedReader(fileReader);
			String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
			reader.close();
		} 
        catch (IOException e) 
        {
			e.printStackTrace();
		}
        return content.toString();
    }

    private static String ParseFile() {
        StringBuilder buffer = new StringBuilder();
        
        // Vérifiez que 'cursor' et 'fileContent' sont correctement initialisés
        if (cursor < 0 || cursor >= fileContent.length()) {
            return ""; // ou une autre valeur par défaut appropriée
        }
    
        // Lire les caractères jusqu'à rencontrer un délimiteur
        while (cursor < fileContent.length()) {
            char currentChar = fileContent.charAt(cursor);
            if (currentChar == '(' || currentChar == ')' || currentChar == ',' || currentChar == ' ') {
                cursor++; // passez au caractère suivant pour la prochaine appel
                // Continuer à avancer le curseur si le caractère suivant est également un délimiteur
                while (cursor < fileContent.length() && (fileContent.charAt(cursor) == '(' || fileContent.charAt(cursor) == ')' || fileContent.charAt(cursor) == ',' || fileContent.charAt(cursor) == ' ')) {
                    cursor++;
                }
                break;
            } else {
                buffer.append(currentChar);
            }
            cursor++;
        }
    
        System.out.println(buffer.toString());
        return buffer.toString();
    }

    public TreeNode createNode(String buffer)
    {   
        if(isNumeric(buffer) == true)
        {
            // Create a node with the constant
            return new ConstNode(Integer.parseInt(buffer));
        }
        else
        {
            // Create a node with the operator
            switch (buffer) {
                case "ET":
                    return new ET(createNode(ParseFile()), createNode(ParseFile()));
                case "OU":
                    return new OU(createNode(ParseFile()), createNode(ParseFile()));
                case "NON":
                    return new NON(createNode(ParseFile()));
                case "SUP":
                    return new SUP(createNode(ParseFile()), createNode(ParseFile()));
                case "SUPEQ":
                    return new SUPEQ(createNode(ParseFile()), createNode(ParseFile()));
                case "EQ":
                    return new EQ(createNode(ParseFile()), createNode(ParseFile()));
                case "ADD":
                    return new ADD(createNode(ParseFile()), createNode(ParseFile()));  
                case "SUB":
                    return new SUB(createNode(ParseFile()), createNode(ParseFile()));
                case "MUL":
                    return new MUL(createNode(ParseFile()), createNode(ParseFile()));
                case "SI":
                    return new SI(createNode(ParseFile()), createNode(ParseFile()), createNode(ParseFile()));
                case "COMPTER":
                    return new COMPTER(ParseFile());
                default:
                    System.out.println("Error: Invalid operator");
                    return null;
            }
        }
        
    }

    @SuppressWarnings("unused")
    private static boolean isNumeric(String buffer) 
    {
        if (buffer == null) 
        {
            return false;
        }
        try 
        {
            int d = Integer.parseInt(buffer);
        } 
        catch (NumberFormatException nfe) 
        {
            return false;
        }
        return true;
    }

    public void config(){

        try {
            File inputFile = new File("path_to_your_file.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            // Dimension
            int dimension = Integer.parseInt(doc.getElementsByTagName("Dimension").item(0).getTextContent());
            System.out.println("Dimension: " + dimension);

            // Grid Size
            NodeList sizeList = doc.getElementsByTagName("Size");
            int[] gridSize = new int[sizeList.getLength()];
            for (int i = 0; i < sizeList.getLength(); i++) {
                gridSize[i] = Integer.parseInt(sizeList.item(i).getTextContent());
            }
            System.out.println("Grid Size: " + java.util.Arrays.toString(gridSize));

            // Display Plane
            String displayPlane = doc.getElementsByTagName("DisplayPlane").item(0).getTextContent();
            System.out.println("Display Plane: " + displayPlane);

            // Additional Neighborhoods
            NodeList neighborhoodList = doc.getElementsByTagName("Neighborhood");
            for (int i = 0; i < neighborhoodList.getLength(); i++) {
                String neighborhood = neighborhoodList.item(i).getTextContent();
                System.out.println("Additional Neighborhood: " + neighborhood);
            }

            // Evolution Rule
            String evolutionRule = doc.getElementsByTagName("EvolutionRule").item(0).getTextContent();
            System.out.println("Evolution Rule: " + evolutionRule);

            // Initial Cells
            NodeList cellList = doc.getElementsByTagName("Cell");
            for (int i = 0; i < cellList.getLength(); i++) {
                String cell = cellList.item(i).getTextContent();
                System.out.println("Initial Cell: " + cell);
            }
            
            // Random initialization
            String randomInit = doc.getElementsByTagName("Random").item(0).getTextContent();
            System.out.println("Random Initialization: " + randomInit);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }

    public static void main(String[] args) 
    {   
        int rows = 11;
        int cols = 11;
        Random random = new Random();
        STRUCT_Grid_ND grid = new STRUCT_Grid_ND(rows, cols);

        for (int i = 0; i < rows * cols * 0.25; i++) {
            grid.getCell(random.nextInt(rows), random.nextInt(cols)).setCellValue(true);
        }
        GFX_GrilleGraphique Grid_2D = new GFX_GrilleGraphique(grid.getDimensions()[0], grid.getDimensions()[1], 12);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid.getCell(i, j).getCellValue()) {
                    Grid_2D.colorierCase(i, j, Color.BLACK);
                }
            }
        }
        int[] position = { 5, 5 };
        COMPTER.setSettings(grid, position);

        TOOLS_ReadFile readFile = new TOOLS_ReadFile("./configs/regle.txt");
        int result = readFile.createNode(ParseFile()).getValue();
        System.out.println("Result: " + result);


    }
}


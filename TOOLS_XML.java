import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TOOLS_XML extends JFrame {
    private JPanel contentPane;
    private JTextField dimensionField;
    private List<JTextField> gridSizeFields;
    private JTextField cutField;
    private JPanel neighborhoodPanel;
    private List<JTextField> neighborhoodFields;
    private JTextField evolutionRuleField;
    private JPanel initialCellsPanel;
    private JTextField randomField;
    private List<JTextField> initialCellFields;
    private JRadioButton randomButton;
    private JRadioButton listButton;
    private ButtonGroup initialGroup;
    private JPanel gridPanel;
    private boolean[][] cellStates;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                TOOLS_XML frame = new TOOLS_XML();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public TOOLS_XML() {
        setTitle("Game of Life Configurator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BorderLayout(10, 10));
        contentPane.add(formPanel, BorderLayout.CENTER);

        JPanel dimensionPanel = new JPanel();
        dimensionPanel.setLayout(new GridLayout(1, 2, 10, 10));
        formPanel.add(dimensionPanel, BorderLayout.NORTH);

        dimensionPanel.add(new JLabel("Dimension:"));
        dimensionField = new JTextField("3");
        dimensionPanel.add(dimensionField);

        JButton dimensionButton = new JButton("Set Dimensions");
        dimensionButton.addActionListener(e -> updateDimensions());
        dimensionPanel.add(dimensionButton);

        gridSizeFields = new ArrayList<>();

        JPanel gridSizePanel = new JPanel();
        gridSizePanel.setLayout(new GridLayout(0, 2, 10, 10));
        formPanel.add(gridSizePanel, BorderLayout.CENTER);

        JPanel extraPanel = new JPanel();
        extraPanel.setLayout(new GridLayout(2, 2, 10, 10));
        formPanel.add(extraPanel, BorderLayout.SOUTH);

        extraPanel.add(new JLabel("Cut:"));
        cutField = new JTextField("(37, :, :)");
        extraPanel.add(cutField);

        extraPanel.add(new JLabel("Evolution Rule:"));
        evolutionRuleField = new JTextField("ADD(MUL(3,SUB(MUL(2,4), 3)), COMPTER(G0))");
        extraPanel.add(evolutionRuleField);

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(2, 1, 10, 10));
        contentPane.add(sidePanel, BorderLayout.EAST);

        neighborhoodPanel = new JPanel();
        neighborhoodPanel.setLayout(new BoxLayout(neighborhoodPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPaneNeighborhood = new JScrollPane(neighborhoodPanel);
        scrollPaneNeighborhood.setBorder(BorderFactory.createTitledBorder("Custom Neighborhoods"));
        sidePanel.add(scrollPaneNeighborhood);

        JButton addNeighborhoodButton = new JButton("Add Neighborhood");
        addNeighborhoodButton.addActionListener(e -> addNeighborhoodField());
        sidePanel.add(addNeighborhoodButton);

        neighborhoodFields = new ArrayList<>();

        initialCellsPanel = new JPanel();
        initialCellsPanel.setLayout(new BoxLayout(initialCellsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPaneInitialCells = new JScrollPane(initialCellsPanel);
        scrollPaneInitialCells.setBorder(BorderFactory.createTitledBorder("Initial Cells"));
        contentPane.add(scrollPaneInitialCells, BorderLayout.WEST);

        JPanel initialTypePanel = new JPanel();
        initialTypePanel.setLayout(new GridLayout(1, 2, 10, 10));
        contentPane.add(initialTypePanel, BorderLayout.SOUTH);

        randomButton = new JRadioButton("Random");
        listButton = new JRadioButton("List of Cells");
        initialGroup = new ButtonGroup();
        initialGroup.add(randomButton);
        initialGroup.add(listButton);
        randomButton.setSelected(true);

        randomButton.addActionListener(e -> updateInitialCellsPanel());
        listButton.addActionListener(e -> updateInitialCellsPanel());

        initialTypePanel.add(randomButton);
        randomField = new JTextField("RANDOM(5)");
        initialTypePanel.add(randomField);

        initialTypePanel.add(listButton);
        JButton addCellButton = new JButton("Add Cell");
        addCellButton.addActionListener(e -> addCellField());
        initialTypePanel.add(addCellButton);

        initialCellFields = new ArrayList<>();

        JButton btnSave = new JButton("Save Configuration");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveConfiguration();
            }
        });
        contentPane.add(btnSave, BorderLayout.NORTH);

        gridPanel = new JPanel();
        contentPane.add(gridPanel, BorderLayout.CENTER);
    }

    private void updateDimensions() {
        try {
            int dimensions = Integer.parseInt(dimensionField.getText());
            if (dimensions > 0) {
                JPanel gridSizePanel = new JPanel();
                gridSizePanel.setLayout(new GridLayout(dimensions, 2, 5, 5));

                gridSizeFields.clear();
                for (int i = 0; i < dimensions; i++) {
                    JLabel label = new JLabel("Grid Size " + (i + 1) + ":");
                    JTextField textField = new JTextField("50");
                    gridSizeFields.add(textField);
                    gridSizePanel.add(label);
                    gridSizePanel.add(textField);
                }

                contentPane.add(gridSizePanel, BorderLayout.CENTER);
                contentPane.revalidate();
                contentPane.repaint();

                updateGridPanel();

            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for dimensions.");
        }
    }

    private void updateGridPanel() {
        gridPanel.removeAll();

        try {
            int rows = Integer.parseInt(gridSizeFields.get(0).getText());
            int cols = Integer.parseInt(gridSizeFields.get(1).getText());

            gridPanel.setLayout(new GridLayout(rows, cols));
            cellStates = new boolean[rows][cols];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    Cell cell = new Cell(i, j);
                    gridPanel.add(cell);
                }
            }

            contentPane.revalidate();
            contentPane.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addNeighborhoodField() {
        JTextField neighborhoodField = new JTextField();
        neighborhoodFields.add(neighborhoodField);
        neighborhoodPanel.add(neighborhoodField);
        neighborhoodPanel.revalidate();
        neighborhoodPanel.repaint();
    }

    private void addCellField() {
        JTextField cellField = new JTextField();
        initialCellFields.add(cellField);
        initialCellsPanel.add(cellField);
        initialCellsPanel.revalidate();
        initialCellsPanel.repaint();
    }

    private void updateInitialCellsPanel() {
        initialCellsPanel.removeAll();
        initialCellFields.clear();
        if (listButton.isSelected()) {
            addCellField();
        }
        initialCellsPanel.revalidate();
        initialCellsPanel.repaint();
    }

    private void saveConfiguration() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

                // Root element
                Document doc = docBuilder.newDocument();
                Element rootElement = doc.createElement("GameOfLife");
                doc.appendChild(rootElement);

                // Dimension
                Element dimension = doc.createElement("Dimension");
                dimension.appendChild(doc.createTextNode(dimensionField.getText()));
                rootElement.appendChild(dimension);

                // Grid Size
                Element gridSize = doc.createElement("GridSize");
                rootElement.appendChild(gridSize);
                for (JTextField field : gridSizeFields) {
                    Element size = doc.createElement("Size");
                    size.appendChild(doc.createTextNode(field.getText()));
                    gridSize.appendChild(size);
                }

                // Cut
                Element cut = doc.createElement("Cut");
                cut.appendChild(doc.createTextNode(cutField.getText()));
                rootElement.appendChild(cut);

                // Custom Neighborhoods
                Element customNeighborhoods = doc.createElement("CustomNeighborhoods");
                rootElement.appendChild(customNeighborhoods);
                for (JTextField field : neighborhoodFields) {
                    Element neighborhood = doc.createElement("Neighborhood");
                    neighborhood.appendChild(doc.createTextNode(field.getText()));
                    customNeighborhoods.appendChild(neighborhood);
                }

                // Evolution Rule
                Element evolutionRule = doc.createElement("EvolutionRule");
                evolutionRule.appendChild(doc.createTextNode(evolutionRuleField.getText()));
                rootElement.appendChild(evolutionRule);

                // Initial Cells
                Element initialCells = doc.createElement("InitialCells");
                rootElement.appendChild(initialCells);

                if (randomButton.isSelected()) {
                    Element random = doc.createElement("Random");
                    random.appendChild(doc.createTextNode(randomField.getText()));
                    initialCells.appendChild(random);
                } else {
                    for (JTextField field : initialCellFields) {
                        Element cell = doc.createElement("Cell");
                        cell.appendChild(doc.createTextNode(field.getText()));
                        initialCells.appendChild(cell);
                    }
                }

                // Write the content into an XML file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(fileToSave);

                transformer.transform(source, result);

                JOptionPane.showMessageDialog(this, "Configuration saved successfully!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Cell extends JPanel {
        private final int row;
        private final int col;

        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(Color.BLACK));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    toggleState();
                }
            });
        }

        private void toggleState() {
            cellStates[row][col] = !cellStates[row][col];
            setBackground(cellStates[row][col] ? Color.BLACK : Color.WHITE);
        }
    }
}

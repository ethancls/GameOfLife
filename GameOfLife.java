import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class GameOfLife extends JFrame {
    private final int GRID_SIZE = 150;
    private final int CELL_SIZE = 5;
    private boolean[][] cells = new boolean[GRID_SIZE][GRID_SIZE];
    private JPanel gridPanel;
    private Timer timer;

    public GameOfLife() {
        setTitle("Game of Life");
        setSize(GRID_SIZE * CELL_SIZE + 20, GRID_SIZE * CELL_SIZE + 60);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        gridPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int row = 0; row < GRID_SIZE; row++) {
                    for (int col = 0; col < GRID_SIZE; col++) {
                        if (cells[row][col]) {
                            g.setColor(Color.BLACK);
                        } else {
                            g.setColor(Color.WHITE);
                        }
                        g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        g.setColor(Color.GRAY);
                        g.drawRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        };

        gridPanel.setPreferredSize(new Dimension(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE));
        add(gridPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.start();
            }
        });
        controlPanel.add(startButton);

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
            }
        });
        controlPanel.add(stopButton);

        JButton randomButton = new JButton("Random");
        randomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                randomizeGrid();
                gridPanel.repaint();
            }
        });
        controlPanel.add(randomButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearGrid();
                gridPanel.repaint();
            }
        });
        controlPanel.add(clearButton);

        JButton sierpinskiButton = new JButton("Sierpinski");
        sierpinskiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearGrid();
                drawSierpinski(GRID_SIZE / 2, 0, GRID_SIZE - 1);
                gridPanel.repaint();
            }
        });
        controlPanel.add(sierpinskiButton);

        String[] logicGates = {"AND Gate", "OR Gate", "NOT Gate"};
        JComboBox<String> gateSelector = new JComboBox<>(logicGates);
        JButton insertGateButton = new JButton("Insert Gate");
        insertGateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedGate = (String) gateSelector.getSelectedItem();
                switch (selectedGate) {
                    case "AND Gate":
                        insertANDGate(10, 10);
                        break;
                    case "OR Gate":
                        insertORGate(10, 10);
                        break;
                    case "NOT Gate":
                        insertNOTGate(10, 10);
                        break;
                }
                gridPanel.repaint();
            }
        });
        controlPanel.add(gateSelector);
        controlPanel.add(insertGateButton);

        add(controlPanel, BorderLayout.SOUTH);

        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextGeneration();
                gridPanel.repaint();
            }
        });

        randomizeGrid();
    }

    private void randomizeGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col] = Math.random() < 0.2;
            }
        }
    }

    private void clearGrid() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                cells[row][col] = false;
            }
        }
    }

    private void drawSierpinski(int x, int y, int size) {
        if (size < 1) return;

        int mid = size / 2;
        for (int i = 0; i <= mid; i++) {
            cells[y + i][x - i] = true;
            cells[y + i][x + i] = true;
        }
        for (int i = -mid; i <= mid; i++) {
            cells[y + mid][x + i] = true;
        }

        drawSierpinski(x - mid / 2, y + mid, mid / 2);
        drawSierpinski(x + mid / 2, y + mid, mid / 2);
    }

    private void insertANDGate(int x, int y) {
        // Define the AND gate pattern (specific positions need to be tweaked for actual gate logic)
        cells[y][x] = true;
        cells[y][x + 1] = true;
        cells[y + 1][x] = true;
        cells[y + 1][x + 1] = true;
        cells[y + 2][x + 1] = true;
        cells[y + 2][x + 2] = true;
    }

    private void insertORGate(int x, int y) {
        // Define the OR gate pattern (specific positions need to be tweaked for actual gate logic)
        cells[y][x] = true;
        cells[y][x + 1] = true;
        cells[y + 1][x + 1] = true;
        cells[y + 1][x + 2] = true;
        cells[y + 2][x + 2] = true;
    }

    private void insertNOTGate(int x, int y) {
        // Define the NOT gate pattern (specific positions need to be tweaked for actual gate logic)
        cells[y][x] = true;
        cells[y + 1][x] = true;
        cells[y + 1][x + 1] = true;
        cells[y + 2][x + 1] = true;
        cells[y + 2][x + 2] = true;
    }

    private void nextGeneration() {
        boolean[][] newCells = new boolean[GRID_SIZE][GRID_SIZE];

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                int liveNeighbors = countLiveNeighbors(row, col);

                if (cells[row][col]) {
                    newCells[row][col] = liveNeighbors == 2 || liveNeighbors == 3;
                } else {
                    newCells[row][col] = liveNeighbors == 3;
                }
            }
        }

        cells = newCells;
    }

    private int countLiveNeighbors(int row, int col) {
        int count = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < GRID_SIZE && newCol >= 0 && newCol < GRID_SIZE) {
                    if (cells[newRow][newCol]) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameOfLife().setVisible(true);
            }
        });
    }
}

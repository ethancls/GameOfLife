import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

//Merci à StackOverflow pour sa précieuse contribution !

@SuppressWarnings("unused")
public class GrilleGraphique extends JPanel
{
	private int largeur, hauteur, taille_case;
	
	private List<ColoredPoint> casesAColorier;

	/**
	 * Constructeur.
	 * @param largeur La largeur (en nombre de cases) de la grille affichée.
	 * @param hauteur La hauteur (en nombre de cases) de la grille affichée.
	 */
	public GrilleGraphique(int largeur, int hauteur, int taille_case) 
	{
		this.largeur = largeur;
		this.hauteur = hauteur;
		this.taille_case = taille_case;
		casesAColorier = new ArrayList<>(25);

		JFrame window = new JFrame();
		window.setSize((largeur + 2) * taille_case, (hauteur + 4) * taille_case);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(this);
		window.setVisible(true);
	}

	@Override
	//Fonction d'affichage de la grille.
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		for (ColoredPoint fillCell : casesAColorier) 
		{
			int cellX = taille_case + (fillCell.getPoint().x * taille_case);
			int cellY = taille_case + (fillCell.getPoint().y * taille_case);
			g.setColor(fillCell.getColor());
			g.fillRect(cellX, cellY, taille_case, taille_case);
		}
		
		g.setColor(Color.BLACK);
		g.drawRect(taille_case, taille_case, largeur*taille_case, hauteur*taille_case);

		for (int i = taille_case; i <= largeur*taille_case; i += taille_case) {
			g.drawLine(i, taille_case, i, hauteur*taille_case+taille_case);
		}

		for (int i = taille_case; i <= hauteur*taille_case; i += taille_case) {
			g.drawLine(taille_case, i, largeur*taille_case+taille_case, i);
		}
	}

	/**
	 * Fonction permettant de colorier une case de la grille avec une couleur spécifique.
	 * @param x Abscisse de la case à colorier (entre 0 et largeur de grille - 1).
	 * @param y Ordonnée de la case à colorier (entre 0 et hauteur de grille - 1).
	 * @param color Couleur de la case à colorier.
	 */
	public void colorierCase(int x, int y, Color color) 
	{
		casesAColorier.add(new ColoredPoint(new Point(x, y), color));
		repaint();
	}
	
	/**
	 * Accesseur.
	 * @return Renvoie la largeur de la grille
	 */
	public int getLargeur()
	{
		return largeur;
	}
	
	/**
	 * Accesseur.
	 * @return Renvoie la hauteur de la grille
	 */
	public int getHauteur()
	{
		return hauteur;
	}
	
	/**
	 * Classe interne pour stocker un point avec une couleur.
	 */
	private static class ColoredPoint {
		private Point point;
		private Color color;

		public ColoredPoint(Point point, Color color) {
			this.point = point;
			this.color = color;
		}

		public Point getPoint() {
			return point;
		}

		public Color getColor() {
			return color;
		}
	}
}

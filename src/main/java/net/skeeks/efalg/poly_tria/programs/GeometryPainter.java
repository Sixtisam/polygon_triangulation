// This is a personal academic project. Dear PVS-Studio, please check it.

// PVS-Studio Static Code Analyzer for C, C++, C#, and Java: http://www.viva64.com
package net.skeeks.efalg.poly_tria.programs;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.skeeks.efalg.poly_tria.Edge;
import net.skeeks.efalg.poly_tria.core.Polygon;
import net.skeeks.efalg.poly_tria.core.Triangle;
import net.skeeks.efalg.poly_tria.core.Vertex;

/**
 * @author Samuel Keusch <samuel.keusch@students.fhnw.ch>
 *
 */
public class GeometryPainter {
	private final JFrame frame = new JFrame("GraphDrawer");
	private final Canvas canvas = new Canvas();
	private final ControlPanel controlPanel = new ControlPanel();
	private final InteractionListener interactionListener = new InteractionListener();
	public final static int BORDER = 10;

	public static final int SCALE = 3;

	public void start() {
		SwingUtilities.invokeLater(() -> {
			// Create and set up the window.
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			controlPanel.init();
			canvas.setPreferredSize(new Dimension(1900, 1100));
			canvas.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 10));
			frame.addKeyListener(interactionListener);
			canvas.addMouseMotionListener(controlPanel);
			canvas.addMouseListener(interactionListener);
			canvas.addMouseMotionListener(interactionListener);
			frame.add(controlPanel);
			frame.add(canvas);
			// Display the window.
			frame.setSize(1900, 1100);
			frame.setVisible(true);
		});
	}

	public volatile BiConsumer<Integer, Integer> mouseMoveConsumer = (a, b) -> {
	};
	public volatile BiConsumer<Integer, Integer> mouseShiftMoveConsumer = (a, b) -> {
	};
	public volatile BiConsumer<Integer, Integer> mouseCtrlMoveConsumer = (a, b) -> {
	};
	public volatile BiConsumer<Integer, Integer> mouseLeftClickConsumer = (a, b) -> {
	};
	public volatile BiConsumer<Integer, Integer> mouseRightClickConsumer = (a, b) -> {
	};
	public volatile Runnable spaceConsumer = () -> {
	};
	public volatile Runnable nextConsumer = () -> {
	};
	public volatile Runnable previousConsumer = () -> {
	};
	public volatile Runnable nConsumer = () -> {
		
	};

	public void setPolygons(Polygon[] polygons, Polygon[] holes) {
		SwingUtilities.invokeLater(() -> {
			canvas.polygons = polygons;
			canvas.holes = holes;
			canvas.repaint();
		});
	}

	public void setTriangles(List<Triangle> triangles) {
		SwingUtilities.invokeLater(() -> {
			canvas.triangles = triangles;
			canvas.repaint();
		});
	}

	public void setEdges(Edge[] edges) {
		SwingUtilities.invokeLater(() -> {
			canvas.edges = edges;
			canvas.repaint();
		});
	}

	public void setPoints(Vertex[] points) {
		SwingUtilities.invokeLater(() -> {
			canvas.points = points;
			canvas.repaint();
		});
	}

	public int[] translatePoint(int x, int y) {
		return new int[] { (x - BORDER) / SCALE, (canvas.getHeight() - y - BORDER) / SCALE };
	}

	public void close() {
		SwingUtilities.invokeLater(() -> {
			frame.setVisible(false);
			frame.dispose();
		});
	}

	public class InteractionListener implements MouseListener, MouseMotionListener, KeyListener {
		@Override
		public void mousePressed(MouseEvent e) {
			int[] p = translatePoint(e.getX(), e.getY());
			if (SwingUtilities.isLeftMouseButton(e)) {
				mouseLeftClickConsumer.accept(p[0], p[1]);
				canvas.repaint();
			} else if (SwingUtilities.isRightMouseButton(e)) {
				mouseRightClickConsumer.accept(p[0], p[1]);
				canvas.repaint();
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				spaceConsumer.run();
			}
			if(e.getKeyCode() == KeyEvent.VK_N) {
				nConsumer.run();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			int[] p = translatePoint(e.getX(), e.getY());
			if (e.isShiftDown()) {
				mouseShiftMoveConsumer.accept(p[0], p[1]);
			} else if (e.isControlDown()) {
				mouseCtrlMoveConsumer.accept(p[0], p[1]);
			} else {
				mouseMoveConsumer.accept(p[0], p[1]);
			}
			canvas.repaint();
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

	}

	public class Canvas extends JPanel {
		private static final long serialVersionUID = 1L;

		public volatile Polygon[] polygons = new Polygon[0];
		public volatile Polygon[] holes = new Polygon[0];
		public volatile Vertex[] points = new Vertex[0];
		public volatile Edge[] edges = new Edge[0];
		public volatile List<Triangle> triangles = new ArrayList<>();

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D graphic2d = (Graphics2D) g;
			graphic2d.addRenderingHints(Map.of(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
			graphic2d.setColor(Color.BLUE);
			graphic2d.translate(BORDER, getHeight() - BORDER);
			graphic2d.scale(SCALE, SCALE);

			graphic2d.setColor(Color.WHITE);
			for (Polygon polygon : polygons) {
				int[] xPoints = new int[polygon.vertices.length];
				int[] yPoints = new int[polygon.vertices.length];
				int i = 0;
				for (Vertex v : polygon.vertices) {
					xPoints[i] = v.x;
					yPoints[i] = -v.y;
					i++;
				}
				graphic2d.fillPolygon(xPoints, yPoints, xPoints.length);
			}
			
			graphic2d.setColor(new Color(0xE4E6E8));
			for(Polygon hole : holes) {
				int[] xPoints = new int[hole.vertices.length];
				int[] yPoints = new int[hole.vertices.length];
				int i = 0;
				for (Vertex v : hole.vertices) {
					xPoints[i] = v.x;
					yPoints[i] = -v.y;
					i++;
				}
				graphic2d.fillPolygon(xPoints, yPoints, xPoints.length);
			}

			graphic2d.setColor(Color.RED);
			for (Triangle t : triangles) {
				drawTriangle(t, graphic2d);
			}

			for (Vertex point : points) {
				if (point == null)
					continue;
				// draw a cross for the point
				graphic2d.setColor(point.type.color);
				graphic2d.drawLine(point.x, -(point.y + 3), point.x, -(point.y - 3));
				graphic2d.drawLine(point.x - 3, -point.y, point.x + 3, -(point.y));
			}
			graphic2d.setColor(Color.YELLOW);
			
			for (Edge edge : edges) {
				graphic2d.drawLine(edge.from.x, -edge.from.y, edge.to.x, -edge.to.y);
			}

		}

		private void drawTriangle(Triangle triangle, Graphics2D g) {
			g.drawPolygon(new int[] { triangle.p1.x, triangle.p2.x, triangle.p3.x },
					new int[] { -triangle.p1.y, -triangle.p2.y, -triangle.p3.y }, 3);
		}
	}

	private final JLabel helpLabel = new JLabel("");

	public String getHelpText() {
		return helpLabel.getText();
	}
	
	public void setHelpText(String text) {
		helpLabel.setText(text);
	}

	public class ControlPanel extends JPanel implements MouseMotionListener, ActionListener {
		private static final long serialVersionUID = 1L;

		private final JLabel xLabel = new JLabel("X");
		private final JLabel yLabel = new JLabel("Y");
		private final JButton nextButton = new JButton("Next");
		private final JButton previousButton = new JButton("Previous");

		public void init() {
			setBackground(Color.LIGHT_GRAY);
			setLayout(new FlowLayout(FlowLayout.LEADING));
			add(xLabel);
			add(yLabel);
			add(helpLabel);
			add(nextButton);
			add(previousButton);
			nextButton.addActionListener(this);
			nextButton.setFocusable(false);
			nextButton.setActionCommand("next");
			previousButton.addActionListener(this);
			previousButton.setActionCommand("previous");
			previousButton.setFocusable(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("next")) {
				nextConsumer.run();
			} else if (e.getActionCommand().equals("previous")) {
				previousConsumer.run();
			}
		}

		/**
		 * Called on mouse move on canvas
		 */
		@Override
		public void mouseMoved(MouseEvent e) {
			int[] p = translatePoint(e.getX(), e.getY());
			xLabel.setText("X: " + p[0]);
			yLabel.setText("Y: " + p[1]);
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}
	}
}

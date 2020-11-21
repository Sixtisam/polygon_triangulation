package net.skeeks.efalg.poly_tria;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.function.BiConsumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GeometryPainter {
	private final JFrame frame = new JFrame("GraphDrawer");
	private final Canvas canvas = new Canvas();
	private final ControlPanel controlPanel = new ControlPanel();
	private final MouseConsumer consumerListener = new MouseConsumer();
	public final static int BORDER = 10;

	public static final int SCALE = 3;

	public void start() {
		SwingUtilities.invokeLater(() -> {
			// Create and set up the window.
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
			frame.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			controlPanel.init();
			canvas.setPreferredSize(new Dimension(1000, 950));
			canvas.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 10));
			canvas.addMouseMotionListener(controlPanel);
			canvas.addMouseListener(consumerListener);
			canvas.addMouseMotionListener(consumerListener);
			frame.add(controlPanel);
			frame.add(canvas);
			// Display the window.
			frame.setSize(1000, 1000);
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

	public void setPolygons(Polygon[] polygons) {
		SwingUtilities.invokeLater(() -> {
			canvas.polygons = polygons;
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

	public class MouseConsumer implements MouseListener, MouseMotionListener {
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
		public volatile Edge[] edges = new Edge[0];
		public volatile Vertex[] points = new Vertex[0];

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			Graphics2D graphic2d = (Graphics2D) g;
			graphic2d.setColor(Color.BLUE);
			graphic2d.translate(BORDER, getHeight() - BORDER);
			graphic2d.scale(SCALE, SCALE);

			for (Polygon polygon : polygons) {
				int[] xPoints = new int[polygon.points.length];
				int[] yPoints = new int[polygon.points.length];
				int i = 0;
				for (Vertex v : polygon.points) {
					xPoints[i] = v.x;
					yPoints[i] = -v.y;
					i++;
				}
				graphic2d.drawPolygon(xPoints, yPoints, xPoints.length);
			}

			graphic2d.setColor(Color.YELLOW);
			for (Edge edge : edges) {
				graphic2d.drawLine(edge.start.x, -edge.start.y, edge.end.x, -edge.end.y);
			}

			graphic2d.setColor(Color.RED);
			for (Vertex point : points) {
				if (point == null)
					continue;
				// draw a cross for the point
				graphic2d.setColor(point.type.color);
				graphic2d.drawLine(point.x, -(point.y + 3), point.x, -(point.y - 3));
				graphic2d.drawLine(point.x - 3, -point.y, point.x + 3, -(point.y));
			}

		}
	}

	public class ControlPanel extends JPanel implements MouseMotionListener {
		private static final long serialVersionUID = 1L;

		private final JLabel xLabel = new JLabel("X");
		private final JLabel yLabel = new JLabel("Y");
		private final JLabel helpLabel = new JLabel("");

		public void init() {
			setBackground(Color.LIGHT_GRAY);
			setLayout(new FlowLayout(FlowLayout.LEADING));
			add(xLabel);
			add(yLabel);
			add(helpLabel);
		}

		public void setHelpText(String text) {
			helpLabel.setText(text);
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

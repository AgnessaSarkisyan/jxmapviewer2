package org.jxmapviewer.input;

import org.jxmapviewer.JXMapViewer;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

/**
 * Used to pan using press and drag mouse gestures
 * @author joshy
 */
public class PanMouseInputListener extends MouseInputAdapter
{
	private Point2D prev;
	private JXMapViewer viewer;
	private Cursor priorCursor;
	
	/**
	 * @param viewer the jxmapviewer
	 */
	public PanMouseInputListener(JXMapViewer viewer)
	{
		this.viewer = viewer;
	}

	@Override
	public void mousePressed(MouseEvent evt)
	{
		if (!SwingUtilities.isLeftMouseButton(evt))
			return;

		prev = new Point2D.Double(evt.getX(), evt.getY());
		priorCursor = viewer.getCursor();
		viewer.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
	}

	@Override
	public void mouseDragged(MouseEvent evt)
	{
		if (!SwingUtilities.isLeftMouseButton(evt))
			return;

		Point2D current = new Point2D.Double(evt.getX(), evt.getY());
		double x = viewer.getCenter().getX();
		double y = viewer.getCenter().getY();

		if (prev != null)
		{
			Point2D point = new Point2D.Double(prev.getX() - current.getX(), prev.getY() - current.getY());

			AffineTransform transform = new AffineTransform();
			transform.rotate(-viewer.getAngle());
			point = transform.transform(point, null);
			x += point.getX();
			y += point.getY();
		}

		int maxHeight = (int) (viewer.getTileFactory().getMapSize(viewer.getZoom()).getHeight() * viewer
				.getTileFactory().getTileSize(viewer.getZoom()));
		if (y > maxHeight)
		{
			y = maxHeight;
		}

		prev = current;
		viewer.setCenter(new Point2D.Double(x, y));
		viewer.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent evt)
	{
		if (!SwingUtilities.isLeftMouseButton(evt))
			return;

		prev = null;
		viewer.setCursor(priorCursor);
	}
}

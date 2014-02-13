package team2485.smartdashboard.extension;

/*
 * Licensed Materials - Property of IBM
 * 5724-Y33, 5724-Y34, 5724-Y35, 5724-Y36, 5724-Y37, 5724-Y38, 5724-Y39, 5724-Z55, 5724-Z56 
 * Â© Copyright IBM Corporation 1996, 2010. All Rights Reserved.
 *
 * Note to U.S. Government Users Restricted Rights: 
 * Use, duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 */

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A dial ring that allows to select an angle (goniometer).
 * The center of the dial ring has space to display another component,
 * whose size is controlled by the dial ring parameters.
 * 
 */
public class JDial extends JPanel
{
  // the inner panel of the dial that can be used for further contents
  JPanel contents;
  // the thickness of the dial
  int thickness = 25;
  // whether we fill the outer shape of the dial
  boolean drawOuter = false;
  // the base color, should be rather dark
  Color color = Color.gray;
  // the angle color
  Color angleColor = new Color(200, 0, 0);
  // the start angle
  int startAngle = 0;
  // the current angle
  int angle = 0;
  // show the angle color at 4 sides.
  boolean showAngleColorAt4Sides;

  // when drawing the outer shape, we have space for 4 labels
  JLabel label1;
  JLabel label2;
  JLabel label3;
  JLabel label4;

  // inner and outer ellipse
  transient Shape outerEllipse = new Ellipse2D.Float(0, 0, 1, 1);
  transient Shape innerEllipse = new Ellipse2D.Float(1, 1, 2, 2);
  transient Shape innerClipEllipse = new Ellipse2D.Float(1, 1, 2, 2);

  /**
   * Creates a new dial.
   * @param drawOuter Whether the dial fills the outer rectangular area.
   *   It should be false for inner dials when dials are nested.
   */
  public JDial(boolean drawOuter)
  {
    this.drawOuter = drawOuter;
    contents = new JPanel() {
       // allow only the inner circle to be active for events
       public boolean contains(int x, int y) {
         x += thickness;
         y += thickness;
         return innerEllipse.contains(x, y);
       }
    };
    contents.setLayout(new BorderLayout());
    contents.setForeground(Color.pink);
    contents.setBackground(Color.pink);
    setLayout(null);
    add(contents);
    if (drawOuter) {
      label1 = new JLabel("");
      label2 = new JLabel("");
      label3 = new JLabel("");
      label4 = new JLabel("");
      super.add(label1);
      super.add(label2);
      super.add(label3);
      super.add(label4);
    }
    enableEvents(AWTEvent.KEY_EVENT_MASK |
                 AWTEvent.MOUSE_WHEEL_EVENT_MASK |
                 AWTEvent.MOUSE_EVENT_MASK |
                 AWTEvent.MOUSE_MOTION_EVENT_MASK);
  }

  /**
   * Sets the thickness of the dial.
   */
  public void setThickness(int t)
  {
    thickness = t;
  }

  /**
   * Returns the thickness of the dial.
   */
  public int getThickness()
  {
    return thickness;
  }

  /**
   * Sets the color of the dial.
   * Since due the highlighting, the dial appears rather light, darker colors
   * are preferred.
   * The default color is gray.
   */
  public void setColor(Color c)
  {
    color = c;
  }

  /**
   * Returns the color of the dial.
   */
  public Color getColor()
  {
    return color;
  }

  /**
   * Sets the color of the angle area of the dial.
   * The default color is a light red.
   */
  public void setAngleColor(Color c)
  {
    angleColor = c;
  }

  /**
   * Returns the color of the angle area of the dial.
   */
  public Color getAngleColor()
  {
    return angleColor;
  }

  /**
   * Sets whether the angle color is not only shown from startAngle to
   * angle, but also from startAngle to -angle, and from startAngle+180
   * to -/- angle.
   * This makes only sense if the angle range is 0..90.
   */
  public void setShowAngleColorAt4Sides(boolean b)
  {
    showAngleColorAt4Sides = b;
  }

  /**
   * Sets the start angle of the dial in degree.
   * 0 degree is displayed on the left side of the dial.
   */
  public void setStartAngle(int a)
  {
    startAngle = a;
    repaintOnInteraction();
  }

  /**
   * Returns the start angle of the dial.
   */
  public int getStartAngle()
  {
    return startAngle;
  }

  /**
   * Sets the current angle of the dial in degree.
   */
  public void setAngle(int a)
  {
    angle = a;
    repaintOnInteraction();
  }

  /**
   * Returns the current angle of the dial in degree.
   */
  public int getAngle()
  {
    return angle;
  }

  /**
   * Returns the top left label.
   * If the outer rectangular area is not drawn, it returns null.
   */
  public JLabel getTopLeftLabel()
  {
    return label1;
  }

  /**
   * Returns the top right label.
   * If the outer rectangular area is not drawn, it returns null.
   */
  public JLabel getTopRightLabel()
  {
    return label2;
  }
  /**
   * Returns the bottom left label.
   * If the outer rectangular area is not drawn, it returns null.
   */
  public JLabel getBottomLeftLabel()
  {
    return label3;
  }
  /**
   * Returns the bottom right label.
   * If the outer rectangular area is not drawn, it returns null.
   */
  public JLabel getBottomRightLabel()
  {
    return label4;
  }

  /**
   * Returns the inner content of the dial.
   */
  public Component getContents()
  {
    if (contents.getComponentCount() > 0)
      return contents.getComponent(0);
    return null;
  }

  /**
   * Add a component displayed in the inner of the dial.
   */
  public Component add(Component c)
  {
    if (c == contents)
      super.add(c);
    else {
      contents.removeAll();
      contents.add(c, BorderLayout.CENTER);
    }
    return c;
  }

  /**
   * Moves and resizes the component.
   * @see Component#setBounds
   */
  public void reshape(int x, int y, int w, int h) 
  {
    // As of JDK1.5, the method Component.reshape is deprecated and calls
    // the method Component.setBounds. Overriding the deprecated variant
    // ensures that it works when either the deprecated, or the replacement
    // variant are called.

    if (w != h) {
      w = h = Math.min(w, h);
    }
    if (w < 2*thickness + 10) w = 2*thickness + 10;
    if (h < 2*thickness + 10) h = 2*thickness + 10;

    super.reshape(x, y, w, h);
    
    int margin = ((contents instanceof JDial) ? 2 : 0);
    int t = getThickness();
    contents.setBounds(t + margin,
                       t + margin,
                       w - 2 * (t + margin),
                       h - 2 * (t + margin));
    outerEllipse = new Ellipse2D.Float(0, 0, w, h);
    innerEllipse = new Ellipse2D.Float(t, t, w-2*t, h-2*t);
    innerClipEllipse = new Ellipse2D.Float(0, 0, w-2*t, h-2*t);

    if (drawOuter) {
      Dimension s = label1.getPreferredSize();
      label1.setBounds(15, 15, s.width, s.height);
      s = label2.getPreferredSize();
      label2.setBounds(w-15-s.width, 15, s.width, s.height);
      s = label3.getPreferredSize();
      label3.setBounds(15, h-15-s.height, s.width, s.height);
      s = label4.getPreferredSize();
      label4.setBounds(w-15-s.width, h-15-s.height, s.width, s.height);
    }
  }

  /**
   * Paint the dial.
   */
  public void paint(Graphics g) {
    Graphics2D dst = (Graphics2D)g;
    Object old = dst.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    dst.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                         RenderingHints.VALUE_ANTIALIAS_ON);
    try {
      super.paint(g);
    } finally {
      dst.setRenderingHint(RenderingHints.KEY_ANTIALIASING, old);
    }
  }

  /**
   * Paint the children of the component.
   */
  public void paintChildren(Graphics g) {
    Graphics2D dst = (Graphics2D)g;
    Object old = dst.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    dst.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                         RenderingHints.VALUE_ANTIALIAS_ON);
    Area shape = new Area(innerEllipse);
    Shape oldClip = dst.getClip();
    dst.setClip(shape);
    try {
      super.paintChildren(g);
    } finally {
      dst.setClip(oldClip);
      dst.setRenderingHint(RenderingHints.KEY_ANTIALIASING, old);
    }
  }

  /**
   * Paint the border of the component.
   */
  public void paintBorder(Graphics g) {
    Graphics2D dst = (Graphics2D)g;
    Object old = dst.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
    dst.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                         RenderingHints.VALUE_ANTIALIAS_ON);
    try {
      paintBorderImpl(dst);
    } finally {
      dst.setRenderingHint(RenderingHints.KEY_ANTIALIASING, old);
    }
  }

  private void paintBorderImpl(Graphics2D dst) {
    super.paintBorder(dst);
    // technically, the dial is part of the border, so that it always overlaps
    // the content

    int t = getThickness();
    Rectangle r = getBounds();
    dst.setColor(Color.black);
    dst.fillRect(0,0,r.width,r.height);
    Area shape = new Area(outerEllipse);
    shape.subtract(new Area(innerEllipse));

    Color c = color;
    dst.setColor(c);
    dst.fill(shape);

    int i = 0;
    int now = r.width - 7;
    int niw = r.width - 2 * t + 7;
    int noh = r.height - 7;
    int nih = r.height - 2 * t + 7;
    // draw the shading
    for (i = 0; i < 4; i++) {
      now = r.width - 7 - 2 * i;
      niw = r.width - 2 * t + 7 + 2 * i;
      noh = r.height - 7 - 2 * i;
      nih = r.height - 2 * t + 7 + 2 * i;
      if (now <= niw) break;
      if (noh <= nih) break;

      shape = new Area(new Ellipse2D.Float(2+i, 2+i, now, noh));
      shape.subtract(new Area(new Ellipse2D.Float(t-5-i, t-5-i, niw, nih)));
      c = new Color(Math.min(255, c.getRed() + 25),
                    Math.min(255, c.getGreen() + 25),
                    Math.min(255, c.getBlue() + 25));
      dst.setColor(c);
      dst.fill(shape);
    }
    // draw the highlight
    shape = new Area(new Ellipse2D.Float(2+i, 2+i, now, noh));
    shape.subtract(new Area(new Ellipse2D.Float(4+i, 4+i, now, noh)));
    dst.setColor(Color.white);
    dst.fill(shape);
    shape = new Area(new Ellipse2D.Float(t-2-i, t-2-i, niw, nih));
    shape.subtract(new Area(new Ellipse2D.Float(t-4-i, t-4-i, niw, nih)));
    dst.fill(shape);
    // draw the handle
    c = new Color(c.getRed()-25, c.getGreen()-25, c.getBlue()-25, 100);
    for (i = 3; i >= 0; i--) {
      if (i == 0)
        c = new Color(0,0,0); 
      else
        c = new Color(Math.max(0, c.getRed() - 40),
                      Math.max(0, c.getGreen() - 40),
                      Math.max(0, c.getBlue() - 40), 100);
      shape = new Area(new Ellipse2D.Float(0, 0, r.width, r.height));
      shape.subtract(new Area(new Ellipse2D.Float(t, t,
                                        r.width - 2 * t, r.height - 2 * t)));
      shape.intersect(new Area(getArrowPointFromAngle(r, 1+i)));
      dst.setColor(c);
      dst.fill(shape);
    }

    if (angleColor != null)
      drawDelta(dst, r);
    drawScale(dst, r);

    if (drawOuter)
      drawOuterArea(dst, r);
  }

  /**
   * Calculates the points of the indicator arrow.
   */
  private Shape getArrowPointFromAngle(Rectangle bounds, int size)
  {
    int radius = Math.max(bounds.width / 2, bounds.height / 2) + 1;
    double a = (startAngle + angle) * 2 * Math.PI / 360;
    double x0 = bounds.width / 2;
    double y0 = bounds.height / 2;
    double x1 = bounds.width / 2 - Math.cos(a) * radius;
    double y1 = bounds.height / 2 - Math.sin(a) * radius;
    double dx = x1 - x0;
    double dy = y1 - y0;
    double d = Math.sqrt(dx * dx + dy * dy);
    double odx = size * dy / d;
    double ody = -size * dx / d;

    GeneralPath gp = new GeneralPath();
    gp.moveTo((float)(x0 + odx), (float)(y0 + ody));
    gp.lineTo((float)(x0 - odx), (float)(y0 - ody));
    gp.lineTo((float)(x1 - odx), (float)(y1 - ody));
    gp.lineTo((float)(x1 + odx), (float)(y1 + ody));
    gp.closePath();
    return gp;
  }

  /**
   * Draws the delta area between start angle and end angle,
   */
  private void drawDelta(Graphics2D dst, Rectangle r)
  {
    float s = 180 - startAngle;
    float d = -angle;
    Area shape = new Area(outerEllipse);
    shape.subtract(new Area(innerEllipse));
    shape.intersect(new Area(new Arc2D.Float(0,0,r.width,r.height,s,d,Arc2D.PIE)));
    dst.setColor(new Color(angleColor.getRed(),
                           angleColor.getGreen(),
                           angleColor.getBlue(),
                           100));
    dst.fill(shape);

    if (showAngleColorAt4Sides) {
      d = angle;
      shape = new Area(outerEllipse);
      shape.subtract(new Area(innerEllipse));
      shape.intersect(new Area(new Arc2D.Float(0,0,r.width,r.height,s,d,Arc2D.PIE)));
      dst.setColor(new Color(angleColor.getRed(),
                             angleColor.getGreen(),
                             angleColor.getBlue(),
                             30));
      dst.fill(shape);
      s = -startAngle;
      shape = new Area(outerEllipse);
      shape.subtract(new Area(innerEllipse));
      shape.intersect(new Area(new Arc2D.Float(0,0,r.width,r.height,s,d,Arc2D.PIE)));
      dst.setColor(new Color(angleColor.getRed(),
                             angleColor.getGreen(),
                             angleColor.getBlue(),
                             30));
      dst.fill(shape);
      d = -angle;
      shape = new Area(outerEllipse);
      shape.subtract(new Area(innerEllipse));
      shape.intersect(new Area(new Arc2D.Float(0,0,r.width,r.height,s,d,Arc2D.PIE)));
      dst.setColor(new Color(angleColor.getRed(),
                             angleColor.getGreen(),
                             angleColor.getBlue(),
                             30));
      dst.fill(shape);
    }
  }

  /**
   * Draws the scale.
   */
  private void drawScale(Graphics2D dst, Rectangle r)
  {
    int radius = Math.max(r.width / 2, r.height / 2) + 1;

    dst.setColor(Color.black);
    dst.setStroke(new BasicStroke(1));
    int t = getThickness();
    Area clip = new Area(new Ellipse2D.Float(t/2, t/2, r.width-t, r.height-t));
    clip.subtract(new Area(innerEllipse));

    Shape oldClip = dst.getClip();
    dst.setClip(clip);
    try {
      for (int i = 0; i < 4; i++) {
        double a = i * 90 * 2 * Math.PI / 360;
        double x0 = r.width / 2;
        double y0 = r.height / 2;
        double x1 = r.width / 2 - Math.cos(a) * radius;
        double y1 = r.height / 2 - Math.sin(a) * radius;
        dst.drawLine((int)x0, (int)y0, (int)x1, (int)y1); 
      }
      clip = new Area(new Ellipse2D.Float(3*t/4, 3*t/4, r.width-3*t/2, r.height-3*t/2));
      clip.subtract(new Area(innerEllipse));
      dst.setClip(clip);
      for (int i = 0; i < 36; i++) {
        if (i == 0 || i == 9 || i == 18 || i == 27) continue;
        double a = i * 10 * 2 * Math.PI / 360;
        double x0 = r.width / 2;
        double y0 = r.height / 2;
        double x1 = r.width / 2 - Math.cos(a) * radius;
        double y1 = r.height / 2 - Math.sin(a) * radius;
        dst.drawLine((int)x0, (int)y0, (int)x1, (int)y1); 
      }
    } finally {
      dst.setClip(oldClip);
    }    
  }

  /**
   * Draws the outer area of the dial.
   */
  private void drawOuterArea(Graphics2D dst, Rectangle r)
  {
    Area shape = new Area(new Rectangle2D.Float(0, 0, r.width, r.height));
    shape.subtract(new Area(new Ellipse2D.Float(0, 0, r.width, r.height)));
    Color c = color;
    dst.setColor(c);
    dst.fill(shape);

    for (int i = 0; i < 4; i++) {
      int now = r.width - 7 - 2 * i;
      int noh = r.height - 7 - 2 * i;

      shape = new Area(new Rectangle2D.Float(2+i, 2+i, now, noh));
      shape.subtract(new Area(new Ellipse2D.Float(0, 0, r.width, r.height)));
      c = new Color(Math.min(255, c.getRed() + 25),
                    Math.min(255, c.getGreen() + 25),
                    Math.min(255, c.getBlue() + 25));
      dst.setColor(c);
      dst.fill(shape);
    }

    AffineTransform oldt = dst.getTransform();

    Rectangle lr = label1.getBounds();
    dst.transform(new AffineTransform(1,0,0,1,lr.x,lr.y));
    label1.paint(dst);
    dst.setTransform(oldt);
    lr = label2.getBounds();
    dst.transform(new AffineTransform(1,0,0,1,lr.x,lr.y));
    label2.paint(dst);
    dst.setTransform(oldt);
    lr = label3.getBounds();
    dst.transform(new AffineTransform(1,0,0,1,lr.x,lr.y));
    label3.paint(dst);
    dst.setTransform(oldt);
    lr = label4.getBounds();
    dst.transform(new AffineTransform(1,0,0,1,lr.x,lr.y));
    label4.paint(dst);
    dst.setTransform(oldt);
  }

  /**
   * Returns true if this contains the input point.
   */
  public boolean contains(int x, int y) {
    if (drawOuter)
      return super.contains(x, y);
    return outerEllipse.contains(x, y);
  }

  // ------------------------------------------------------------------------
  // Interaction

  private boolean dragging;
  private int initialDragAngle;
  private int startDragAngle;

  /**
   * Called when the mouse was clicked.
   */
  protected void processMouseEvent(MouseEvent e) {
    if (outerEllipse.contains(e.getX(), e.getY()) &&
        !innerEllipse.contains(e.getX(), e.getY())) { 
      int id = e.getID();
      switch (id) {
        case MouseEvent.MOUSE_PRESSED:
          dragging = true;
          initialDragAngle = angle;
          startDragAngle = getAngle(e.getX(), e.getY());
          repaintOnInteraction();
          break;
        case MouseEvent.MOUSE_RELEASED:
          if (dragging) {
            int stopDragAngle = getAngle(e.getX(), e.getY());
            angle = initialDragAngle + (stopDragAngle - startDragAngle);
            fireActionEvent("angle");
            repaintOnInteraction();
            dragging = false;
          }
          break;
        case MouseEvent.MOUSE_CLICKED:
          dragging = false;
          if (e.getButton() == MouseEvent.BUTTON1)
            angle++;
          else
            angle--;
          fireActionEvent("angle");
          repaintOnInteraction();
          break;
      }
    }
    super.processMouseEvent(e);
  }

  /**
   * Called when the mouse was moved or dragged.
   */
  protected void processMouseMotionEvent(MouseEvent e) {
    // don't need to check inner/outerElipse since we do only something when
    // dragging is true
    int id = e.getID();
    switch (id) {
      case MouseEvent.MOUSE_DRAGGED:
        if (dragging) {
          int stopDragAngle = getAngle(e.getX(), e.getY());
          angle = initialDragAngle + (stopDragAngle - startDragAngle);
          fireActionEvent("angle");
          repaintOnInteraction();
        }
        break;
    }
    super.processMouseEvent(e);
  }

  /**
   * Called when the mouse wheel was used.
   */
  protected void processMouseWheelEvent(MouseWheelEvent e) {
    if (outerEllipse.contains(e.getX(), e.getY()) &&
        !innerEllipse.contains(e.getX(), e.getY())) {
      int id = e.getID();
      switch (id) {
        case MouseEvent.MOUSE_WHEEL:
          int click = e.getWheelRotation();
          angle -= click;
          fireActionEvent("angle");
          repaintOnInteraction();
          break;
      }
    }
  }

  /**
   * Repaints this component when an interaction happened.
   */
  void repaintOnInteraction()
  {
    // normalize the angle
    while (angle >= 360) angle -= 360;
    while (angle < 0) angle += 360;
    // repaint
    Component c = getParent();
    if (c instanceof JPanel)
      c = ((JPanel)c).getParent();
    if (c instanceof JDial)
      ((JDial)c).repaintOnInteraction();
    else
      repaint();
  }

  /**
   * Returns the angle from a mouse position.
   */
  private int getAngle(int x, int y)
  {
    Rectangle r = getBounds();
    double x0 = r.width / 2;
    double y0 = r.height / 2;
    double dx = (x - x0);
    double dy = (y - y0);
    double a = Math.atan2(dy, dx);
    return (int)(a / (2 * Math.PI) * 360);
  }

  /**
   * Adds an action listener.
   * Action listeners are notified whenever the angle of this dial changed.
   */
  public void addActionListener(ActionListener l)
  {
    listenerList.add(ActionListener.class, l);
  }

  /**
   * Removes an action listener.
   */
  public void removeActionListener(ActionListener l)
  {
    listenerList.remove(ActionListener.class, l);
  }

  /**
   * Fires the action event of this control.
   */
  protected void fireActionEvent(String actionCommand)
  {
    // normalize the angle
    while (angle >= 360) angle -= 360;
    while (angle < 0) angle += 360;
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    ActionEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length-2; i>=0; i-=2) {
      if (listeners[i]==ActionListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new ActionEvent(this,
                              ActionEvent.ACTION_PERFORMED,
                              actionCommand);
        }
        ((ActionListener)listeners[i+1]).actionPerformed(e);
      }
    }
  }
}

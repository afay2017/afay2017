package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.*;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class AirTankWidget extends Widget {

    public static final String NAME = "Air Widget";
    public static final DataType[] TYPES = {DataType.NUMBER};

    private static final double MAX_VAL = 120, MIN_VAL = 0;
    private static final int MAX_DRAW_WIDTH = 135;

    private double value = 70;
    private int drawWidth = 0;
    private Color color;

    private BufferedImage airtank;
    private JLabel label;

    @Override
    public void init() {
        try {
            airtank = ImageIO.read(BackgroundLeft.class.getResourceAsStream("/team2485/smartdashboard/extension/res/Tank.png"));
        } catch (IOException e) {
        }

        final Dimension size = new Dimension(143, 90);
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);

        final BorderLayout layout = new BorderLayout(0, 0);
        this.setLayout(layout);

        this.add(new airtankPanel(), BorderLayout.CENTER);

        label = new JLabel("UNKNOWN");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setForeground(Color.white);
        this.add(label, BorderLayout.SOUTH);
    }

    @Override
    public void propertyChanged(final Property prprt) {
    }

    @Override
    public void setValue(Object o) {
        value = ((Number) o).doubleValue();

        if (value > 70) {
            color = Color.LIGHT_GRAY;
        } else if (value > 50) {
            color = Color.orange;
        } else {
            color = Color.red;
        }
        label.setText(String.format("%.1f PSI", value));
        if (value > MAX_VAL) {
            value = MAX_VAL;
            color = Color.GRAY;
            label.setForeground(Color.ORANGE);
            label.setFont(new Font("Ubuntu", Font.BOLD, 14));
        } else {
            label.setForeground(Color.LIGHT_GRAY);
            label.setFont(new Font("Ubuntu", Font.PLAIN, 14));
        }
        //label.setForeground(value > 40 ? Color.white : Color.red);
        drawWidth = (int) ((value - MIN_VAL) / (MAX_VAL - MIN_VAL) * MAX_DRAW_WIDTH);
        repaint();
    }

    private class airtankPanel extends JPanel {

        public airtankPanel() {
        }

        @Override
        protected void paintComponent(final Graphics gg) {

            final Graphics2D g = (Graphics2D) gg;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (drawWidth > 0) {
                g.setColor(color);
                g.fillRoundRect(5, 5, drawWidth, 50, 8, 8);
            }
            g.drawImage(airtank, 0, 0, this);

        }
    }
}

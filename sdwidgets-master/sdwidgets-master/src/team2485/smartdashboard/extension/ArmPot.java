package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.*;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ArmPot extends Widget {

    public static final String NAME = "ArmPot";
    public static final DataType[] TYPES = {DataType.NUMBER};

    private static final double MIN_VAL = 2000, MAX_VAL = 3000;
    public static int X = 270;
    public static int Y = 270;
    private double value = 0;
    private double dec = 0;
    private Color color;

    private BufferedImage arm;
    private JLabel label;
    private JPanel back;

    @Override
    public void init() {
        try {
            arm = ImageIO.read(BackgroundLeft.class.getResourceAsStream("/team2485/smartdashboard/extension/res/ARMIII.png"));
        } catch (IOException e) {
        }

        final Dimension size = new Dimension(X, Y);
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);

        final BorderLayout layout = new BorderLayout(0, 0);
        this.setLayout(layout);

        this.add(new airtanksPanel(), BorderLayout.CENTER);

        label = new JLabel("UNKNOWN");
        
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setBackground(Color.gray);
        label.setForeground(Color.DARK_GRAY);
        label.setOpaque(true);
        label.setFont(new Font("Ubuntu", Font.BOLD, 20));
        
        this.add(label, BorderLayout.SOUTH);
        //this.add(back);
    }

    @Override
    public void propertyChanged(final Property prprt) {
    }

    @Override
    public void setValue(Object o) {
        value = ((Number) o).doubleValue();
        dec = (value-2427)/4;
        //if (value == 0)
           // value = Math.random()*180;
        if ((value > 2759)&&(value < 2781)) {
            color = Color.GREEN;
        } else if ((value > 2730)&&(value < 2800)) {
            color = Color.orange;
        } else {
            color = Color.LIGHT_GRAY;
        }
        label.setText(String.format("%.1f Degrees", dec));
        label.setBackground(color);
        
        repaint();
    }

    private class airtanksPanel extends JPanel {

        public airtanksPanel() {
        }

        @Override
        protected void paintComponent(final Graphics gg) {

            final Graphics2D g = (Graphics2D) gg;
            
            g.setColor(Color.yellow);
            g.translate(X/2, Y/2);
                g.rotate((dec-90)*Math.PI/180);
                    g.setColor(Color.GRAY);
                    //g.fillRect(0, 0, 5, 30);
                    g.drawImage(arm, 0, 0, this);
                    g.fillOval(-7,-7,26,26);
                 g.rotate(-(dec-90)*Math.PI/180);
                 g.translate(-X/2, -Y/2);
             g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        }
    }
}

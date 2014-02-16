package team2485.smartdashboard.extension;

import edu.wpi.first.smartdashboard.gui.*;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FieldWidget extends Widget {
    public static final String NAME = "Field";
    public static final DataType[] TYPES = { DataType.NUMBER };

    private BufferedImage fieldLabel, fieldGradImage;
    private Shape clip;
    private FieldWidget.KillableThread animationThread;
    private double distance = 0;
    
    @Override
    public void init() {
        initComponents();

        // Load images
        try {
            this.fieldLabel     = ImageIO.read(FieldWidget.class.getResourceAsStream("/team2485/smartdashboard/extension/res/field.png"));
            this.fieldGradImage = ImageIO.read(FieldWidget.class.getResourceAsStream("/team2485/smartdashboard/extension/res/field-gradient.png"));
        } catch (IOException e) {
            System.err.println("Error loading field images.");
            e.printStackTrace();
        }

        this.clip = new Polygon(
            new int[] {  57, 232, 272, 272, 232,  57,  16,  16 },
            new int[] {  20,  20,  76, 479, 536, 536, 479,  76 },
            8);
    }

    public void setDistance(double dist) {
        final double newDistance = dist;

        this.valueField.setText(String.format("%.3f", newDistance));

        if (this.distance == 0) {
            // first time, so just set the distance
            this.distance = newDistance;
        }
        else {
            if (this.animationThread != null) this.animationThread.kill();

            // start animation thread
            this.animationThread = new KillableThread() {
                @Override
                public void run() {
                    try {
                        final double prevDistance = distance;

                        final int duration = 500, interval = 30,
                                chunks = duration / interval;
                        final double chunkDiff = (newDistance - prevDistance) / chunks;
                        for (int t = 0; t < duration; t += interval) {
                            if (this.killed) return;

                            distance += chunkDiff;
                            renderPanel.repaint();

                            Thread.sleep(interval);
                        }
                    } catch (InterruptedException e) { }
                }
            };
            this.animationThread.start();
        }
    }

    private void paintFieldComponent(final Graphics gg) {
        Graphics2D g = (Graphics2D)gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(this.fieldLabel, 0, 0, null);

        // draw gradient
        if (this.distance > 0 && this.distance <= 54) {
            int y = 20 + (int)((this.distance / 54.0) * 516.0);
            g.setClip(this.clip);
            g.drawImage(this.fieldGradImage, 16, y - 23, null);
        }
    }

    @Override
    public void propertyChanged(Property prprt) {
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        renderPanel = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                paintFieldComponent(g);
            }
        };
        controlsPanel = new javax.swing.JPanel();
        controlsInnerPanel = new javax.swing.JPanel();
        valueField = new javax.swing.JLabel();
        distanceConstLabel = new javax.swing.JLabel();
        modeLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(17, 17, 17));
        setOpaque(false);
        setLayout(new java.awt.BorderLayout());

        renderPanel.setMaximumSize(new java.awt.Dimension(288, 556));
        renderPanel.setMinimumSize(new java.awt.Dimension(288, 556));
        renderPanel.setOpaque(false);
        renderPanel.setPreferredSize(new java.awt.Dimension(288, 556));

        javax.swing.GroupLayout renderPanelLayout = new javax.swing.GroupLayout(renderPanel);
        renderPanel.setLayout(renderPanelLayout);
        renderPanelLayout.setHorizontalGroup(
            renderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 288, Short.MAX_VALUE)
        );
        renderPanelLayout.setVerticalGroup(
            renderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 556, Short.MAX_VALUE)
        );

        add(renderPanel, java.awt.BorderLayout.EAST);

        controlsPanel.setOpaque(false);

        controlsInnerPanel.setOpaque(false);

        valueField.setFont(new java.awt.Font("BoomBox 2", 0, 24)); // NOI18N
        valueField.setForeground(new java.awt.Color(255, 255, 255));
        valueField.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        valueField.setText("-");

        distanceConstLabel.setFont(new java.awt.Font("BoomBox 2", 0, 12)); // NOI18N
        distanceConstLabel.setForeground(new java.awt.Color(255, 255, 255));
        distanceConstLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        distanceConstLabel.setText("Distance");

        modeLabel.setFont(new java.awt.Font("BoomBox 2", 0, 12)); // NOI18N
        modeLabel.setForeground(new java.awt.Color(255, 255, 255));
        modeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        modeLabel.setText("Auto");

        javax.swing.GroupLayout controlsInnerPanelLayout = new javax.swing.GroupLayout(controlsInnerPanel);
        controlsInnerPanel.setLayout(controlsInnerPanelLayout);
        controlsInnerPanelLayout.setHorizontalGroup(
            controlsInnerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlsInnerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlsInnerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(valueField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(distanceConstLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                    .addComponent(modeLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        controlsInnerPanelLayout.setVerticalGroup(
            controlsInnerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlsInnerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(modeLabel)
                .addGap(1, 1, 1)
                .addComponent(distanceConstLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(valueField)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout controlsPanelLayout = new javax.swing.GroupLayout(controlsPanel);
        controlsPanel.setLayout(controlsPanelLayout);
        controlsPanelLayout.setHorizontalGroup(
            controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(controlsInnerPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        controlsPanelLayout.setVerticalGroup(
            controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlsPanelLayout.createSequentialGroup()
                .addContainerGap(302, Short.MAX_VALUE)
                .addComponent(controlsInnerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(163, 163, 163))
        );

        add(controlsPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel controlsInnerPanel;
    private javax.swing.JPanel controlsPanel;
    private javax.swing.JLabel distanceConstLabel;
    private javax.swing.JLabel modeLabel;
    private javax.swing.JPanel renderPanel;
    private javax.swing.JLabel valueField;
    // End of variables declaration//GEN-END:variables

    @Override
    public void setValue(Object o) {
        setDistance(((Number)o).doubleValue());
    }
    
    private class KillableThread extends Thread {
        protected boolean killed = false;
        public void kill() {
            killed = true;
        }
    }
}

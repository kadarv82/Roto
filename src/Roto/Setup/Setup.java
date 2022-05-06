package Roto.Setup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.IOException;

import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Roto.Basic.SwingConstans;

import Roto.Gui.ImagePanel;

import Roto.Utils.PropertyUtil;
import Roto.Utils.SystemUtil;

public class Setup extends JFrame implements ActionListener {

    private static final long serialVersionUID = -9172585683357577512L;

    private JTextPane jtSetup;
    private SystemUtil systemUtil;
    private PropertyUtil propertyUtil;
    private SwingConstans swc;
    private ImagePanel pnSetup;
    private JButton jbInstall;

    public Setup() {
        systemUtil = SystemUtil.getInstance();
        propertyUtil = PropertyUtil.getInstance();
        swc = SwingConstans.getInstance();
        createGUI();
    }


    private Point getCenterLocation() {
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        // Determine the new location of the window
        int w = getSize().width;
        int h = getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;

        return new Point(x, y);
    }

    public String getSuggestion() {
        StringBuffer suggestion = new StringBuffer();
        suggestion.append(propertyUtil.getLangText("Frame.Main.Suggestion") + ":\n");
        if (systemUtil.checkVersionWithSetupProperty()) {
            suggestion.append(propertyUtil.getLangText("Frame.Main.MeetsRequirement"));
        } else {
            suggestion.append(propertyUtil.getLangText("Frame.Main.NotMeetsRequirement"));
        }

        return suggestion.toString();
    }

    public void createGUI() {
        setSize(500, 400);
        setLocation(getCenterLocation());
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.WHITE);

        pnSetup = new ImagePanel(new ImageIcon(swc.viewerPath + "Roto.jpg").getImage());
        pnSetup.setBackground(Color.WHITE);
        pnSetup.setLayout(new BorderLayout());
        pnSetup.add(jtSetup = new JTextPane(), "Center");
        pnSetup.add(jbInstall = new JButton("Install Java"), "South");
        pnSetup.setBackground(Color.WHITE);

        add(pnSetup, "Center");

        StyledDocument doc = jtSetup.getStyledDocument();
        jtSetup.setEditable(false);
        jtSetup.setOpaque(false);

        //  Set alignment to be centered for all paragraphs
        MutableAttributeSet standard = new SimpleAttributeSet();
        //StyleConstants.setAlignment(standard, StyleConstants.ALIGN_CENTER);
        StyleConstants.setAlignment(standard, StyleConstants.ALIGN_LEFT);

        StyleConstants.setBold(standard, true);
        StyleConstants.setItalic(standard, true);
        StyleConstants.setFontSize(standard, 14);
        StyleConstants.setFontFamily(standard, "Arial");
        StyleConstants.setForeground(standard, Color.BLACK);

        doc.setParagraphAttributes(0, 0, standard, true);

        jtSetup.setText(systemUtil.getJavaInfo() + "\n" + getSuggestion());

        jbInstall.setEnabled(!systemUtil.checkVersionWithSetupProperty());
        jbInstall.addActionListener(this);

        setVisible(true);
    }

    public void installJava() {
        jbInstall.setEnabled(false);
        jtSetup.setText("");
        remove(pnSetup);
        add(new JLabel(new ImageIcon(swc.viewerPath + "Loading.gif")), "Center");
        setBackground(Color.WHITE);
        repaint();
        Properties setupProperty = propertyUtil.loadProperty(swc.setupPath + "Setup.properties");
        Runtime runtime = Runtime.getRuntime();
        String command = swc.setupPath + setupProperty.getProperty("Setup.File.Name");

        try {
            if (new File(command).exists()) {
                runtime.exec(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Timer timer = new Timer(5000, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ae) {
                System.gc();
                System.exit(0);
            }
        });
        timer.start();
    }

    public static void main(String[] args) {
        new Setup();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbInstall) {
            installJava();
        }
    }
}

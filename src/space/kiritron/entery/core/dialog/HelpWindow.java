// Copyright (c) 2020 Киритрон Стэйблкор.

package space.kiritron.entery.core.dialog;

import space.kiritron.entery.init;

import javax.swing.*;
import java.awt.*;

import static space.kiritron.entery.init.VER_APP;


@SuppressWarnings("serial")
public class HelpWindow extends JDialog {
    public HelpWindow(Frame owner) {
        super(owner, "О программе", false);
        final int sizeWidth = 1315;
        final int sizeHeight = 790;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height - sizeHeight) / 2;
        setSize(sizeWidth, sizeHeight);
        setBounds(locationX, locationY, sizeWidth, sizeHeight);

        JPanel panel = new JPanel(new BorderLayout());

        ImageIcon logo = new ImageIcon(init.class.getResource("res/help.png"));
        JLabel label_logo = new JLabel(logo);
        panel.add(label_logo, BorderLayout.CENTER);

        getContentPane().add(panel, BorderLayout.CENTER);
    }
}

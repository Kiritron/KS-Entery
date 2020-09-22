// Copyright (c) 2020 Киритрон Стэйблкор.

package space.kiritron.entery.core.dialog;

import space.kiritron.entery.init;

import javax.swing.*;
import java.awt.*;

import static space.kiritron.entery.init.VER_APP;


@SuppressWarnings("serial")
public class AboutProgWindow extends JDialog {
    public AboutProgWindow(Frame owner) {
        super(owner, "О программе", false);
        final int sizeWidth = 400;
        final int sizeHeight = 400;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height - sizeHeight) / 2;
        setSize(sizeWidth, sizeHeight);
        setBounds(locationX, locationY, sizeWidth, sizeHeight);

        JPanel panel = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel(new BorderLayout());

        ImageIcon logo = new ImageIcon(init.class.getResource("res/logo2.png"));
        JLabel label_logo = new JLabel(logo);
        panel.add(label_logo, BorderLayout.PAGE_START);

        ImageIcon author = new ImageIcon(init.class.getResource("res/author.png"));
        JLabel label_author = new JLabel(author);
        panel.add(label_author, BorderLayout.CENTER);

        ImageIcon logocef = new ImageIcon(init.class.getResource("res/logo3.png"));
        JLabel label_logocef = new JLabel(logocef);
        panel.add(label_logocef, BorderLayout.PAGE_END);

        JLabel version = new JLabel(VER_APP);
        version.setFont(new Font("Dialog", Font.BOLD, 20));
        version.setHorizontalAlignment(JLabel.CENTER);
        panel2.add(version, BorderLayout.CENTER);

        panel2.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.PAGE_START);

        getContentPane().add(panel, BorderLayout.PAGE_START);
        getContentPane().add(panel2, BorderLayout.CENTER);
    }
}

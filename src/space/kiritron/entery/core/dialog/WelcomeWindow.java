// Copyright (c) 2020 Киритрон Стэйблкор.

package space.kiritron.entery.core.dialog;

import space.kiritron.entery.init;

import javax.swing.*;
import java.awt.*;

import static space.kiritron.entery.init.VER_APP;


@SuppressWarnings("serial")
public class WelcomeWindow extends JDialog {
    public WelcomeWindow(Frame owner) {
        super(owner, "Добро пожаловать!", false);
        final int sizeWidth = 400;
        final int sizeHeight = 500;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height - sizeHeight) / 2;
        setSize(sizeWidth, sizeHeight);
        setBounds(locationX, locationY, sizeWidth, sizeHeight);

        JPanel panel = new JPanel(new BorderLayout());

        JLabel text = new JLabel();
        String textInLabel;
        textInLabel =
                        "<html><center style='margin: 10px'>" +
                                "<h3>Добро пожаловать в Энтери</h3>" +
                                "Похоже вы запустили данный продукт впервые. Мы благодарим вас за то, что вы установили его. Надеемся, что вам понравится." +
                                " Данный браузер развивается с уклоном на минимализм и стремится не содержать в себе лишние модули, которыми большинство не" +
                                " пользуется. Обратите внимание, что данный продукт в раннем доступе и некоторые функции ещё не реализованы, а что-то работает" +
                                " не так хорошо. Мы дадим вам знать, когда выйдет обновление с исправлениями и новыми функциями.<br><br>" +
                                "Мы рекомендуем открыть Справку в меню Дополнительно, на случай, если возникнут вопросы. Мы не даём гарантии, что справка" +
                                " сможет ответить на все ваши вопросы касательно Энтэри, но в этом случае в нашей группе ВК присутствует обратная связь. Мы" +
                                " будем рады ответить на ваши вопросы и помочь использовать Энтэри.<br><br>" +
                                "Приятного пользования." +
                        "</center></html>";
        text.setText(textInLabel);
        text.setFont(new Font("Dialog", Font.PLAIN, 14));
        text.setHorizontalAlignment(JLabel.CENTER);

        panel.add(text, BorderLayout.PAGE_START);

        getContentPane().add(panel, BorderLayout.CENTER);
    }
}

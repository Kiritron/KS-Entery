/*
 * Copyright 2020 Kiritron's Space
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.kiritron.entery.core.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * @author Киритрон Стэйблкор
 */

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
                                "Мы рекомендуем открыть Учебник в меню Справка, на случай, если возникнут вопросы. Мы не даём гарантии, что справка" +
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

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

package space.kiritron.entery.core.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

import space.kiritron.entery.init;

import static space.kiritron.entery.init.crosslightIcon;

/**
 * @author Мистер Рекс(MR.REX) и Киритрон Стэйблкор
 */

@SuppressWarnings("serial")
public class TabComponent extends JPanel {
	private final TabManager Panel;
	private final int Index;
	
	public TabComponent(final TabManager TabbedPanel, int Index) {
		super(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
        this.Panel = TabbedPanel;
        this.Index = Index;
        
        setOpaque(false);

        // Киритрон: Мистер Рекс заметил баг с первой вкладкой, когда она длиннее стандартного значения.
        // Возможно Label.setMaximumSize(MaxSize); игнорируется.
        // Я попытался решить эту проблему очередным костылём.
        // Этот же костыль устанавливается и в TabManager.
        JLabel Label = new JLabel() {
            public String getText() {
                int i = Panel.indexOfTabComponent(TabComponent.this);
                if (i != -1) {
                    return Panel.getTitleAt(i);
                } else {
                    // Сам костыль
                    try {
                        if (Panel.getTitleAt(i) != null) {
                            if (Panel.getTitleAt(i).length() > 17) {
                                return Panel.getTitleAt(i).substring(0, 18) + "...";
                            } else {
                                return Panel.getTitleAt(i);
                            }
                        } else {
                            return null;
                        }
                    } catch (Exception exp) {
                        return null;
                    }
                }
            }
        };
        
        add(Label);
        Label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
      
        JButton Button = new JButton(crosslightIcon);
        int Size = 10;
        Button.setPreferredSize(new Dimension(Size, Size));
        Button.setUI(new BasicButtonUI());
        Button.setFocusable(false);
        Button.setContentAreaFilled(false);
        Button.setBorder(BorderFactory.createEtchedBorder());
        Button.setBorderPainted(false);
        Button.setRolloverEnabled(true);
        Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent Event) {
				int Index = Panel.indexOfTabComponent(TabComponent.this);
	            if (Index != -1) Panel.CloseTab(Index);
			}
        });
        
        add(Button);
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
        
        Dimension MaxSize = new Dimension(128, this.getHeight());
        Label.setMaximumSize(MaxSize);
        this.setMaximumSize(MaxSize);
	}
}
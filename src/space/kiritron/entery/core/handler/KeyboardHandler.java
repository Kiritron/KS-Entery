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

package space.kiritron.entery.core.handler;

import org.cef.browser.CefBrowser;
import org.cef.handler.CefKeyboardHandlerAdapter;
import space.kiritron.entery.core.ui.ControlPanel;
import space.kiritron.entery.init;

import static space.kiritron.entery.core.MainFrame.tabManager;
import static space.kiritron.entery.init.AddressFromArgs;

/**
 * @author Киритрон Стэйблкор and The Chromium Embedded Framework Authors.
 */

public class KeyboardHandler extends CefKeyboardHandlerAdapter {
    // Киритрон: Необходимые переменные для работы блоков, где комбинации горячих клавиш.
    private boolean CTRL_DOWN = false;

    @Override
    public boolean onKeyEvent(CefBrowser browser, CefKeyEvent event) {
        ControlPanel CP = tabManager.controlPanels.get(tabManager.getSelectedIndex() + 1);

    	// Перезагрузка страницы на F5
    	if (event.windows_key_code == 0x74) {
            if (event.type == CefKeyEvent.EventType.KEYEVENT_RAWKEYDOWN) {
                CP.reloadPage();
            }
            return true;
        }

    	// Блок CTRL и других клавиш с его участием. Иначе говоря комбинации.
        // Киритрон: Вообще я хотел реализовать это более аккуратным способом, но прикол в том, что
        // этот способ конечно же не работал. А потому у нас тут странные блоки кода, которые
        // похожи на костыль и создают баги.
            if (event.windows_key_code == 0x11) { if (event.type == CefKeyEvent.EventType.KEYEVENT_RAWKEYDOWN) { CTRL_DOWN = true; } return true; }
                if (CTRL_DOWN) {
                    // Открытие новой вкладки на CTRL + T
                    if (event.windows_key_code == 0x54) {
                        if (event.type == CefKeyEvent.EventType.KEYEVENT_RAWKEYDOWN) {
                            if (AddressFromArgs == null) {
                                tabManager.OpenTab(init.HomePage);
                            } else {
                                tabManager.OpenTab(init.AddressFromArgs);
                            }
                            CTRL_DOWN = false;
                        }
                        return true;
                    }

                    // Открытие последней закрытой вкладки на CTRL + R
                    if (event.windows_key_code == 0x52) {
                        if (event.type == CefKeyEvent.EventType.KEYEVENT_RAWKEYDOWN) {
                            if (tabManager.cacheURL != null) {
                                tabManager.OpenTabFromTab(tabManager.cacheURL);
                                tabManager.cacheURL = null;
                                CTRL_DOWN = false;
                            }
                        }
                        return true;
                    }

                    // Закрытие текущей вкладки на CTRL + W
                    if (event.windows_key_code == 0x57) {
                        if (event.type == CefKeyEvent.EventType.KEYEVENT_RAWKEYDOWN) {
                            tabManager.CloseTab(tabManager.getSelectedIndex());
                            CTRL_DOWN = false;
                        }
                        return true;
                    }

                    // Увеличение масштаба просматриваемой страницы на CTRL + "+"
                    if ((event.windows_key_code == 0xBB) || (event.windows_key_code == 0x6B)) {
                        if (event.type == CefKeyEvent.EventType.KEYEVENT_RAWKEYDOWN) {
                            CP.PlusZoom(true);
                            CTRL_DOWN = false;
                        }
                        return true;
                    }

                    // Уменьшение масштаба просматриваемой страницы на CTRL + "-"
                    if ((event.windows_key_code == 0xBD) || (event.windows_key_code == 0x6D)) {
                        if (event.type == CefKeyEvent.EventType.KEYEVENT_RAWKEYDOWN) {
                            CP.PlusZoom(false);
                            CTRL_DOWN = false;
                        }
                        return true;
                    }

                    CTRL_DOWN = false;
                }
        return false;
    }
}

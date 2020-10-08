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

import java.awt.Frame;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.callback.CefContextMenuParams;
import org.cef.callback.CefMenuModel;
import org.cef.callback.CefMenuModel.MenuId;
import org.cef.handler.CefContextMenuHandler;

import space.kiritron.entery.core.dialog.ShowTextDialog;

/**
 * @author Киритрон Стэйблкор and The Chromium Embedded Framework Authors.
 */

public class ContextMenuHandler implements CefContextMenuHandler {
    private final Frame owner_;
    private Map<Integer, String> suggestions_ = new HashMap<Integer, String>();

    public ContextMenuHandler(Frame owner) {
        owner_ = owner;
    }

    @Override
    public void onBeforeContextMenu(
            CefBrowser browser, CefFrame frame, CefContextMenuParams params, CefMenuModel model) {
        model.clear();


        model.addItem(MenuId.MENU_ID_COPY, "Копировать");
        model.addItem(MenuId.MENU_ID_PASTE, "Вставить");
        model.addItem(MenuId.MENU_ID_CUT, "Вырезать");
        model.addItem(MenuId.MENU_ID_DELETE, "Удалить");
        model.addItem(MenuId.MENU_ID_UNDO, "Отменить");
        model.addItem(MenuId.MENU_ID_REDO, "Повторить");
        model.addSeparator();
        model.addItem(MenuId.MENU_ID_BACK, "Назад");
        model.setEnabled(MenuId.MENU_ID_BACK, browser.canGoBack());

        model.addItem(MenuId.MENU_ID_FORWARD, "Вперёд");
        model.setEnabled(MenuId.MENU_ID_FORWARD, browser.canGoForward());
        if (params.hasImageContents() && params.getSourceUrl() != null) {
            model.addSeparator();
            model.addItem(MenuId.MENU_ID_USER_FIRST, "Сохранить изображение...");
        }
        model.addSeparator();
        model.addItem(MenuId.MENU_ID_VIEW_SOURCE, "Посмотреть исходники страницы...");

        Vector<String> suggestions = new Vector<String>();
        params.getDictionarySuggestions(suggestions);

        int id = MenuId.MENU_ID_SPELLCHECK_SUGGESTION_0;
        for (String suggestedWord : suggestions) {
            model.addItem(id, suggestedWord);
            suggestions_.put(id, suggestedWord);
            if (++id > MenuId.MENU_ID_SPELLCHECK_SUGGESTION_LAST) break;
        }
    }

    @Override
    public boolean onContextMenuCommand(CefBrowser browser, CefFrame frame,
            CefContextMenuParams params, int commandId, int eventFlags) {
        switch (commandId) {
            case MenuId.MENU_ID_VIEW_SOURCE:
                ShowTextDialog visitor =
                        new ShowTextDialog(owner_, "Исходники \"" + browser.getURL() + "\"");
                browser.getSource(visitor);
                return true;
            case MenuId.MENU_ID_USER_FIRST:
                browser.startDownload(params.getSourceUrl());
                return true;
            default:
                if (commandId >= MenuId.MENU_ID_SPELLCHECK_SUGGESTION_0) {
                    String newWord = suggestions_.get(commandId);
                    if (newWord != null) {
                        System.err.println(
                                "replacing " + params.getMisspelledWord() + " with " + newWord);
                        browser.replaceMisspelling(newWord);
                        return true;
                    }
                }
                return false;
        }
    }

    @Override
    public void onContextMenuDismissed(CefBrowser browser, CefFrame frame) {
        suggestions_.clear();
    }
}

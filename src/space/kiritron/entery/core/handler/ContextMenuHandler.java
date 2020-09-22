// Copyright (c) 2014 The Chromium Embedded Framework Authors. All rights reserved.
// Copyright (c) 2020 Киритрон Стэйблкор.

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

        // Navigation menu
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

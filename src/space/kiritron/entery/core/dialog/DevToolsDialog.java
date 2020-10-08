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

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JDialog;

import org.cef.browser.CefBrowser;

/**
 * @author Киритрон Стэйблкор and The Chromium Embedded Framework Authors.
 */

@SuppressWarnings("serial")
public class DevToolsDialog extends JDialog {
    private final CefBrowser devTools_;
    public DevToolsDialog(Frame owner, String title, CefBrowser browser) {
        this(owner, title, browser, null);
    }

    public DevToolsDialog(Frame owner, String title, CefBrowser browser, Point inspectAt) {
        super(owner, title, false);

        setLayout(new BorderLayout());
        setSize(800, 600);
        setLocation(owner.getLocation().x + 20, owner.getLocation().y + 20);

        devTools_ = browser.getDevTools(inspectAt);
        add(devTools_.getUIComponent());

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e) {
                dispose();
            }
        });
    }

    @Override
    public void dispose() {
        devTools_.close(true);
        super.dispose();
    }
}

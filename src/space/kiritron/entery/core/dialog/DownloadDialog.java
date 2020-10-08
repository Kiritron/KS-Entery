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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.cef.browser.CefBrowser;
import org.cef.callback.CefBeforeDownloadCallback;
import org.cef.callback.CefDownloadItem;
import org.cef.callback.CefDownloadItemCallback;
import org.cef.handler.CefDownloadHandler;

/**
 * @author Киритрон Стэйблкор and The Chromium Embedded Framework Authors.
 */

@SuppressWarnings("serial")
public class DownloadDialog extends JDialog implements CefDownloadHandler {
    private final Frame owner_;
    private final Map<Integer, DownloadObject> downloadObjects_ =
            new HashMap<Integer, DownloadObject>();
    private final JPanel downloadPanel_ = new JPanel();
    private final DownloadDialog dialog_;

    public DownloadDialog(Frame owner) {
        super(owner, "Загрузки", false);
        setVisible(false);
        setSize(400, 300);

        owner_ = owner;
        dialog_ = this;
        downloadPanel_.setLayout(new BoxLayout(downloadPanel_, BoxLayout.Y_AXIS));
        add(downloadPanel_);
    }

    private class DownloadObject extends JPanel {
        private boolean isHidden_ = true;
        private final int identifier_;
        private JLabel fileName_ = new JLabel();
        private JLabel status_ = new JLabel();
        private JButton dlAbort_ = new JButton();
        private JButton dlRemoveEntry_ = new JButton("Удалить из списка");
        private CefDownloadItemCallback callback_;
        private Color bgColor_;

        DownloadObject(CefDownloadItem downloadItem, String suggestedName) {
            super();
            setOpaque(true);
            setLayout(new BorderLayout());
            setMaximumSize(new Dimension(dialog_.getWidth() - 10, 80));
            identifier_ = downloadItem.getId();
            bgColor_ = identifier_ % 2 == 0 ? Color.GRAY : Color.LIGHT_GRAY;
            setBackground(bgColor_);

            fileName_.setForeground(Color.DARK_GRAY);
            status_.setForeground(Color.DARK_GRAY);

            fileName_.setText(suggestedName);
            add(fileName_, BorderLayout.NORTH);

            status_.setAlignmentX(LEFT_ALIGNMENT);
            add(status_, BorderLayout.CENTER);

            JPanel controlPane = new JPanel();
            controlPane.setLayout(new BoxLayout(controlPane, BoxLayout.X_AXIS));
            controlPane.setOpaque(true);
            controlPane.setBackground(bgColor_);
            dlAbort_.setText("Отменить загрузку");
            dlAbort_.setEnabled(false);
            dlAbort_.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (callback_ != null) {
                        fileName_.setText("ОТМЕНЕНО - " + fileName_.getText());
                        callback_.cancel();
                    }
                }
            });
            controlPane.add(dlAbort_);

            dlRemoveEntry_.setEnabled(false);
            dlRemoveEntry_.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    DownloadObject removed = downloadObjects_.remove(identifier_);
                    if (removed != null) {
                        downloadPanel_.remove(removed);
                        dialog_.repaint();
                    }
                }
            });
            controlPane.add(dlRemoveEntry_);
            add(controlPane, BorderLayout.SOUTH);

            update(downloadItem, null);
        }

        // The method humanReadableByteCount() is based on
        // http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java
        String humanReadableByteCount(long bytes) {
            int unit = 1024;
            if (bytes < unit) return bytes + " B";

            int exp = (int) (Math.log(bytes) / Math.log(unit));
            String pre = "" + ("kMGTPE").charAt(exp - 1);
            return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
        }

        void update(CefDownloadItem downloadItem, CefDownloadItemCallback callback) {
            int percentComplete = downloadItem.getPercentComplete();
            String rcvBytes = humanReadableByteCount(downloadItem.getReceivedBytes());
            String totalBytes = humanReadableByteCount(downloadItem.getTotalBytes());
            String speed = humanReadableByteCount(downloadItem.getCurrentSpeed()) + "it/s";

            if (downloadItem.getReceivedBytes() >= 5 && isHidden_) {
                dialog_.setVisible(true);
                dialog_.toFront();
                owner_.toBack();
                isHidden_ = false;
            }
            Runtime.getRuntime().runFinalization();

            callback_ = callback;
            status_.setText(rcvBytes + " из " + totalBytes + " - " + percentComplete + "%"
                    + " - " + speed);
            dlAbort_.setEnabled(downloadItem.isInProgress());
            dlRemoveEntry_.setEnabled(!downloadItem.isInProgress() || downloadItem.isCanceled()
                    || downloadItem.isComplete());
            if (!downloadItem.isInProgress() && !downloadItem.isCanceled()
                    && !downloadItem.isComplete()) {
                fileName_.setText("ОШИБКА В ЗАГРУЗКЕ - " + fileName_.getText());
                callback.cancel();
            }
        }
    }

    @Override
    public void onBeforeDownload(CefBrowser browser, CefDownloadItem downloadItem,
            String suggestedName, CefBeforeDownloadCallback callback) {
        callback.Continue(suggestedName, true);

        DownloadObject dlObject = new DownloadObject(downloadItem, suggestedName);
        downloadObjects_.put(downloadItem.getId(), dlObject);
        downloadPanel_.add(dlObject);
    }

    @Override
    public void onDownloadUpdated(
            CefBrowser browser, CefDownloadItem downloadItem, CefDownloadItemCallback callback) {
        DownloadObject dlObject = downloadObjects_.get(downloadItem.getId());
        if (dlObject == null) return;
        dlObject.update(downloadItem, callback);
    }
}

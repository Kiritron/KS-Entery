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

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.network.CefCookieManager;

import space.kiritron.entery.init;
import space.kiritron.entery.core.MainFrame;
import space.kiritron.entery.core.dialog.DownloadDialog;

import static space.kiritron.entery.core.MainFrame.tabManager;
import static space.kiritron.entery.init.AddressFromArgs;
import static space.kiritron.entery.init.addIcon;

/**
 * @author Мистер Рекс(MR.REX) и Киритрон Стэйблкор
 */

public class TabManager extends JTabbedPane {
	private JPanel Content;
	private CefBrowser Browser;
	private MainFrame Frame;
	private ControlPanel Control;
	private CefClient Client;
	private DownloadDialog DownloadDialog;
	private CefCookieManager CookieManager;
	private boolean OSREnabled = false;
	private boolean TransparentEnabled = false;
	
	private int NewTabPosition = -1;
	private boolean NewTabProcess = false;
	private ImageIcon PlusIcon = addIcon;
	
	private ArrayList<JPanel> Tabs;
	private ArrayList<CefBrowser> Browsers;

	private HashMap<Integer, String> NamesOfTabs = new HashMap<>();

	private int SelectedTab = 0;

	public static String cacheURL = null;
	public static ArrayList<ControlPanel> controlPanels = new ArrayList<ControlPanel>(); // Киритрон: Необходимо для идентификации панелей

	private TabManager TM;

	public TabManager(MainFrame Frame, JPanel Content, CefBrowser Browser, ControlPanel Control,
			CefClient Client, boolean OSREnabled, boolean TransparentEnabled, DownloadDialog DownloadDialog, CefCookieManager CookieManager) {
		
		this.Content = Content;
		this.Browser = Browser;
		this.Frame = Frame;
		this.Control = Control;
		this.Client = Client;
		this.DownloadDialog = DownloadDialog;
		this.CookieManager = CookieManager;
		
		this.OSREnabled = OSREnabled;
		this.TransparentEnabled = TransparentEnabled;
		
		Tabs = new ArrayList<JPanel>();
		Browsers = new ArrayList<CefBrowser>();
		Content.add(this, BorderLayout.CENTER);

		TM = this;

		this.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
		this.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent Event) {
            	JPanel TabPanel = (JPanel) ((JTabbedPane) Event.getSource()).getSelectedComponent();
                SelectedTab = ((JTabbedPane) Event.getSource()).getSelectedIndex();
                if (SelectedTab == Tabs.size() && !NewTabProcess) {
					if (AddressFromArgs == null) {
						tabManager.OpenTab(init.HomePage);
					} else {
						tabManager.OpenTab(init.AddressFromArgs);
					}
				}
                if (TabPanel != null) {
                	CefBrowser BrowserObject = Browsers.get(SelectedTab);
                	//TabPanel.add(Control.ForBrowserObject(BrowserObject), BorderLayout.NORTH);
                	Frame.setBrowser(BrowserObject);

					// Киритрон: Перенос заголовка вкладки в заголовок окна.
					// Но здесь она обновляет заголовок с переключением вкладок.
					// ...и тут ещё один костыль для решения бага, который я уже
					// описал в последнем методе этого класса.
					if (!TM.getTitleAt(SelectedTab).contains("...")) {
						NamesOfTabs.put(getSelectedIndex(), TM.getTitleAt(SelectedTab));
					}

					if (TM.getTitleAt(SelectedTab).length() > 18) {
						TM.setTitleAt(SelectedTab, TM.getTitleAt(SelectedTab).substring(0, 19) + "...");
					}

					if (NamesOfTabs.get(SelectedTab) != null) {
						Frame.setTitle(NamesOfTabs.get(SelectedTab) + " — Энтэри");
					} else {
						Frame.setTitle(TM.getTitleAt(SelectedTab) + " — Энтэри");
					}
                }
            }
        });
		
		UpdateNewTabButton();
	}
	
	private void UpdateNewTabButton() {
		if (NewTabPosition != -1) this.removeTabAt(NewTabPosition);
		NewTabProcess = true;
		this.addTab(null, PlusIcon, null);
		NewTabProcess = false;
		NewTabPosition = this.getTabCount() - 1;
		grabFocus(); // Киритрон: Исправление бага с потерей фокуса окна, когда вкладка закрывается
	}
	
	public void OpenTab(String Url) {
		JPanel Tab = new JPanel(new BorderLayout());
		CefBrowser T_Browser = Client.createBrowser(Url, OSREnabled, TransparentEnabled, null);
		ControlPanel CP = new ControlPanel(Frame, T_Browser, DownloadDialog, CookieManager, Client);
		Tab.add(CP, BorderLayout.NORTH);
		Tab.add(T_Browser.getUIComponent(), BorderLayout.CENTER);
		controlPanels.add(CP);
		Browsers.add(T_Browser);
		Tabs.add(Tab);
		int Index = Tabs.size() - 1;
		this.addTab(Url, Tabs.get(Index));
		Index = this.getTabCount() - 1;
		this.setTabComponentAt(Index, new TabComponent(this, Index));
		UpdateNewTabButton();
	}

	// Киритрон: Добавил свой метод, чтобы загружать новые вкладки, если
	// этого просит веб-страница. Так же метод используется для загрузки
	// страниц из некоторых участков кода браузера.
	public void OpenTabFromTab(String Url) {
		JPanel Tab = new JPanel(new BorderLayout());
		CefBrowser T_Browser = Client.createBrowser(Url, OSREnabled, TransparentEnabled, null);
		ControlPanel CP = new ControlPanel(Frame, T_Browser, DownloadDialog, CookieManager, Client);
		Tab.add(CP, BorderLayout.NORTH);
		Tab.add(T_Browser.getUIComponent(), BorderLayout.CENTER);
		controlPanels.add(CP);
		Browsers.add(T_Browser);
		Tabs.add(Tab);
		int Index = Tabs.size() - 1;
		this.addTab(Url, Tabs.get(Index));
		Index = this.getTabCount() - 1;
		this.setTitleAt(Index, "Пустая страница..."); // Киритрон: Ставим предварительное имя вкладки, чтобы избежать появление бага.
		this.setTabComponentAt(Index, new TabComponent(this, Index));
		UpdateNewTabButton();
		TM.setSelectedIndex(Index - 1); // Киритрон: Фокусируемся на новой вкладке.
	}
	
	public void CloseTab(int Index) {
		if (this.getTabCount() <= 2) return;
		this.removeTabAt(Index);
		NewTabPosition--;
		Tabs.remove(Index);
		cacheURL = Browsers.get(Index).getURL(); // Киритрон: Сохраняем последнюю закрытую страницу, на случай, если пользователь
												 // решит к ней вернуться.
		Browsers.get(Index).close(true);
		Browsers.remove(Index);
		NamesOfTabs.remove(Index);
		UpdateNewTabButton();
	}
	
	public void UpdateTab(CefBrowser Browser, String Title, String Address) {
		int Index = -1, Counter = 0;
		for (CefBrowser BrowserFromTab : Browsers) {
			if (BrowserFromTab == Browser) {
				Index = Counter;
				break;
			}
			Counter++;
		}
		
		if (Index == -1) return;

		// Киритрон: Мистер Рекс заметил баг с первой вкладкой, когда она длиннее стандартного значения.
		// Возможно Label.setMaximumSize(MaxSize) в TabComponent игнорируется.
		// Я попытался решить эту проблему очередным костылём.
		// Этот же костыль устанавливается и в TabComponent.
		if (Title != null) {
			NamesOfTabs.put(this.SelectedTab, Title);
			Frame.setTitle(Title + " — Энтэри");

			// Сам костыль
			if (Tabs.size() == 1) {
				if (Title.length() > 14) {
					Title = Title.substring(0, 15) + "...";
				}
			}

			if (Title.contains("about:blank")) {
				Title = "Пустая страница";
			}

			this.setTitleAt(Index, Title);
		}
		if (Address != null) ((ControlPanel) Tabs.get(Index).getComponent(0)).setAddress(Browsers.get(Index), Address);
	}
}
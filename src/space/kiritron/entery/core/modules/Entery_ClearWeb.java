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

package space.kiritron.entery.core.modules;

import org.cef.network.CefRequest;
import space.kiritron.entery.init;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Киритрон Стэйблкор
 * @version 1.0
 */

/*
 * Модуль "Энтэри ЧистыйВеб"
 * ===
 * В данном модуле содержатся методы и сигнатуры для идентификации рекламных баннеров, трекеров
 * и другого нежелательного контента.
 */

public class Entery_ClearWeb {
    // Киритрон: Некоторые сигнатуры отключены, так как они не работают
    private static String yandex_analytics, yandex_analytics2, yandex_advert, yandex_advert2,
                          google_analytics_sign1, google_analytics_sign2, google_analytics_sign3, google_analytics_sign4, google_advert, google_advert2,
                          //sentry_bugtracker1, sentry_bugtracker2,
                          //bugsnag_bugtracker,
                          //hotjar_analytics,
                          banner, flash, advert1, advert2, advert3, advert4,
                          mailru_analytics, mailru_analytics2, mailru_advert,
                          tns_counter,
                          twitter_analytics,
                          adtng_advert,
                          wiki_analytics,
                          amazon_ad,
                          fandom_analytcs,
                          other_analytics1, other_analytics2;

    public static void init_signatures() {
        yandex_analytics = "mc.yandex.ru/metrika/";                       // Яндекс.Метрика
        yandex_analytics2 = "mc.yandex.ru/watch/";                        // Яндекс.Метрика
        yandex_advert = "yastatic.net";                                   // Яндекс.Директ
        yandex_advert2 = "an.yandex.ru";                                  // Яндекс.Директ

        google_analytics_sign1 = "google-analytics.com/ga.js";            // Google Аналитика
        google_analytics_sign2 = "google-analytics.com/analytics.js";     // Google Аналитика
        google_analytics_sign3 = "google-analytics.com/dc.js";            // Google Аналитика
        google_analytics_sign4 = "googletagservices.com/tag/";            // Google Аналитика
        google_advert = "googlesyndication.com";                          // Google Реклама
        google_advert2 = "googleads";                                     // Google Реклама

        //hotjar_analytics = "static.hotjar.com";                         // HotJar Аналитика

        banner = "banner";                                                // Сигнатура обыкновенного баннера

        flash = ".swf";                                                   // Сигнатура файла флеш плеера | Киритрон: По сути в Энтэри и так флеш не работает, но всё равно лучше такое блочить

        advert1 = "advert";                                               // Сигнатура рекламы
        advert2 = "/ads/";                                                // Сигнатура рекламы
        advert3 = "a.ad.gt";                                              // Сигнатура рекламы
        advert4 = "seg.ad.gt";                                            // Сигнатура рекламы

        mailru_analytics = "top-fwz1.mail.ru";                            // MAIL.RU Аналитика
        mailru_analytics2 = "https://r0.mail.ru/pixel/";                  // MAIL.RU Аналитика
        mailru_advert = "ad.mail.ru/static/";                             // MAIL.RU Реклама

        tns_counter = "tns-counter.ru";                                   // tns-counter Счётчик

        twitter_analytics = "syndication.twitter.com";                    // Twitter трекер

        adtng_advert = "adtng.";                                          // Рекламная сеть

        wiki_analytics = "beacon.wikia-services.com/__track/";            // Аналитика Wikia

        other_analytics1 = "sb.scorecardresearch.com/beacon.js";
        other_analytics2 = "secure.quantserve.com/quant.js";

        amazon_ad = "amazon-adsystem.com";

        fandom_analytcs = "services.fandom.com/ad-tag-manager/";
    }

    // Киритрон: Возвращает TRUE, если есть совпадение с базой сигнатур.
    public static boolean checkRequest(CefRequest request) {
        if (init.ClearWebStatus) {
            String request_url = request.getURL();
            if (request_url != null && !request_url.isEmpty()) {
                if (request_url.contains(yandex_analytics) || request_url.contains(yandex_analytics2) || request_url.contains(yandex_advert) || request_url.contains(yandex_advert2)) { return true; }
                if (request_url.contains(google_analytics_sign1) || request_url.contains(google_analytics_sign2) || request_url.contains(google_analytics_sign3) ||
                        request_url.contains(google_advert) || request_url.contains(google_advert2) || request_url.contains(google_analytics_sign4)) { return true; }
                //if (request_url.contains(sentry_bugtracker1) || request_url.contains(sentry_bugtracker2)) { return true; }
                //if (request_url.contains(bugsnag_bugtracker)) { return true; }
                if (request_url.contains(banner)) { return true; }
                if (request_url.contains(flash)) { return true; }
                if (request_url.contains(advert1) || request_url.contains(advert2) || request_url.contains(advert3) || request_url.contains(advert4)) { return true; }
                if (request_url.contains(mailru_analytics) || request_url.contains(mailru_analytics2) || request_url.contains(mailru_advert)) { return true; }
                if (request_url.contains(tns_counter)) { return true; }
                if (request_url.contains(twitter_analytics)) { return true; }
                if (request_url.contains(adtng_advert)) { return true; }
                if (request_url.contains(amazon_ad)) { return true; }
                if (request_url.contains(fandom_analytcs)) { return true; }
                if (request_url.contains(wiki_analytics)) { return true; }
                if (request_url.contains(other_analytics1) || request_url.contains(other_analytics2)) { return true; }
            }

            if (init.ClearWebKSDBStatus) {
                if (checkURL(request_url)) { return true; }
            }
        }

        return false;
    }

    // Киритрон: Тормозная вещь. По умолчанию отключено в настройках.
    public static boolean checkURL(String URL) {
        String KSDB = HTTPConn("https://kiritron.space/data/entery/clearweb/urls");

        if (URL.contains("http")) {
            if (URL.contains("https://")) {
                URL = URL.replace("https://", "");
            } else if (URL.contains("http://")) {
                URL = URL.replace("http://", "");
            } else {
                KSDB = null;
                return false;
            }

            if (URL.contains("/")) {
                URL = URL.substring(0, URL.indexOf("/"));
                if (KSDB.contains(URL)) {
                    KSDB = null;
                    return true;
                } else {
                    KSDB = null;
                    return false;
                }
            } else {
                KSDB = null;
                return false;
            }
        }

        KSDB = null;
        return false;
    }

    // Киритрон: Скопировано из библиотеки КС Дьюк, но
    // здесь не SSL отключения, так как ожидается, что
    // библиотека отключила его уже при инициализации.
    private static String HTTPConn(String url) {
        try {
            StringBuilder sb = new StringBuilder();
            URL pageURL = new URL(url);
            URLConnection uc = pageURL.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));
            try {
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
            } finally {
                br.close();
            }
            return sb.toString();
        } catch (IOException e) {
            return "ERROR";
        }
    }
}

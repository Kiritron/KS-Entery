package space.kiritron.entery.ks_libs.duke;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * Класс с методом получения и сравнения версий через HTTP/HTTPS.
 * @author Киритрон Стэйблкор
 * @version 2.0
 */

public class httpconn {
    /**
     * Проверка версии с помощью подключения к веб-серверу. Получает контент от веб сервера и сравнивает его с версией приложения.
     * @param URL Адрес в веб пространстве, по которому можно узнать актуальную версию приложения.
     * @param SSL_Verification Необходимо ли проверять SSL сертификаты.
     * @param VER_APP Версия этого приложения(Не та, что на сервере).
     * @return возвращает результат проверки. OK - Всё в порядке. DIFFERENCE_FINDED - Есть различия. Другой ответ - признак ошибки.
     */

    public static String checkVersion(String URL, boolean SSL_Verification, String VER_APP) {
        String ServerVersion = null;

        if (SSL_Verification == false) {
            SSLVerifDisable();
        }

        String out = HTTPConn(URL);

        if (!out.contains("ERROR")) {
            if (Check(VER_APP, out)) {
                ServerVersion = "OK";
            } else {
                ServerVersion = "DIFFERENCE_FINDED";
            }
        } else {
            ServerVersion = "GET_VERSION_CODE_ERROR";
        }

        return ServerVersion;
    }

    private static String HTTPConn(String url) {
        if (url != null) {
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
        } else {
            return "URL_EMPTY_ERROR";
        }
    }

    private static boolean Check(String VER_APP, String VER_FROM_SERVER) {
        if (VER_APP.equals(VER_FROM_SERVER)) {
            return true;
        } else {
            return false;
        }
    }

    private static void SSLVerifDisable() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) { /* Ничего не происходит */ }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) { /* Ничего не происходит */ }
                    } };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = new HostnameVerifier() { public boolean verify(String hostname, SSLSession session) { return true; } };

            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }
}

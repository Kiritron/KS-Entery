package space.kiritron.entery.ks_libs.pixel;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Класс со строками текущих дат и времени.
 * @author Киритрон Стэйблкор
 * @version 1.0
 */

public class GDate {
    static Calendar calendar = new GregorianCalendar();

    static int CurDay = calendar.get(Calendar.DAY_OF_MONTH);
    static int CurMonth = calendar.get(Calendar.MONTH) + 1;
    static int CurYear = calendar.get(Calendar.YEAR);

    static int CurHour = calendar.get(Calendar.HOUR_OF_DAY);
    static int CurMinutes = calendar.get(Calendar.MINUTE);
    static int CurSeconds = calendar.get(Calendar.SECOND);

    private static String CurDaySTR() {
        String A = Integer.toString(CurDay);
        int B = CurDay;

        if (B == 1) {
            A = "01";
        }

        if (B == 2) {
            A = "02";
        }

        if (B == 3) {
            A = "03";
        }

        if (B == 4) {
            A = "04";
        }

        if (B == 5) {
            A = "05";
        }

        if (B == 6) {
            A = "06";
        }

        if (B == 7) {
            A = "07";
        }

        if (B == 8) {
            A = "08";
        }

        if (B == 9) {
            A = "09";
        }

        return A;
    }

    private static String CurHourSTR() {
        String A = Integer.toString(CurHour);
        int B = CurHour;

        if (B == 1) {
            A = "01";
        }

        if (B == 2) {
            A = "02";
        }

        if (B == 3) {
            A = "03";
        }

        if (B == 4) {
            A = "04";
        }

        if (B == 5) {
            A = "05";
        }

        if (B == 6) {
            A = "06";
        }

        if (B == 7) {
            A = "07";
        }

        if (B == 8) {
            A = "08";
        }

        if (B == 9) {
            A = "09";
        }

        return A;
    }

    private static String CurMinutesSTR() {
        String A = Integer.toString(CurMinutes);
        int B = CurMinutes;

        if (B == 1) {
            A = "01";
        }

        if (B == 2) {
            A = "02";
        }

        if (B == 3) {
            A = "03";
        }

        if (B == 4) {
            A = "04";
        }

        if (B == 5) {
            A = "05";
        }

        if (B == 6) {
            A = "06";
        }

        if (B == 7) {
            A = "07";
        }

        if (B == 8) {
            A = "08";
        }

        if (B == 9) {
            A = "09";
        }

        return A;
    }

    private static String CurSecondsSTR() {
        String A = Integer.toString(CurSeconds);
        int B = CurSeconds;

        if (B == 1) {
            A = "01";
        }

        if (B == 2) {
            A = "02";
        }

        if (B == 3) {
            A = "03";
        }

        if (B == 4) {
            A = "04";
        }

        if (B == 5) {
            A = "05";
        }

        if (B == 6) {
            A = "06";
        }

        if (B == 7) {
            A = "07";
        }

        if (B == 8) {
            A = "08";
        }

        if (B == 9) {
            A = "09";
        }

        return A;
    }

    private static String CurMonthSTR() {
        String A = Integer.toString(CurMonth);
        int B = CurMonth;

        if (B == 1) {
            A = "01";
        }

        if (B == 2) {
            A = "02";
        }

        if (B == 3) {
            A = "03";
        }

        if (B == 4) {
            A = "04";
        }

        if (B == 5) {
            A = "05";
        }

        if (B == 6) {
            A = "06";
        }

        if (B == 7) {
            A = "07";
        }

        if (B == 8) {
            A = "08";
        }

        if (B == 9) {
            A = "09";
        }

        return A;
    }

    public static String GetCurDateAndTimeWithSeconds = CurDaySTR() + "." + CurMonthSTR() + "." + CurYear + " " + CurHourSTR() + ":" + CurMinutesSTR() + ":" + CurSecondsSTR();
    public static String GetCurDateAndTime = CurDaySTR() + "." + CurMonthSTR() + "." + CurYear + " " + CurHourSTR() + ":" + CurMinutesSTR();
    public static String GetDate = CurDaySTR() + "-" + CurMonthSTR() + "-" + CurYear;
    public static String GetTime = CurHourSTR() + ":" + CurMinutesSTR();
    public static String GetTimeWithSeconds = CurHourSTR() + ":" + CurMinutesSTR() + ":" + CurSecondsSTR();
    public static String GetDateNoDay = CurMonthSTR() + "-" + CurYear;
}


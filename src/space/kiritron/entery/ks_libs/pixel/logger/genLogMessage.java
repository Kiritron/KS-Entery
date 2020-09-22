package space.kiritron.entery.ks_libs.pixel.logger;

import space.kiritron.entery.ks_libs.pixel.GDate;

/**
 * Класс с методами для генерации вывода в консоль.
 * @author Киритрон Стэйблкор
 * @version 1.0
 */

public class genLogMessage {
    private static GDate TADClass = new GDate();

    private final static String INFO = "ИНФО";
    private final static String WARN = consoleColors.YELLOW + "ПРЕДУПРЕЖДЕНИЕ" + consoleColors.RESET;
    private final static String ERROR = consoleColors.RED + "ОШИБКА" + consoleColors.RESET;

    /**
     * Генерация вывода сообщения в консоль.
     * @param type Тип сообщения. 0 - без типа, 1 - Информация, 2 - Предупреждение, 3 - Ошибка.
     * @param timeWithDate true - если нужно сгенерировать сообщение не только, со временем, но и с датой. false - если нужно только время.
     * @param message Сообщение, которое нужно вывести в консоль.
     * @return Возвращает сгенерированное сообщение.
     */
    public static String gen(byte type, boolean timeWithDate, String message) {
        if (type == 1) {
            if (timeWithDate == false) {
                return "[" + TADClass.GetTimeWithSeconds + " " + INFO + "]: " + message;
            } else {
                return "[" + TADClass.GetCurDateAndTimeWithSeconds + " " + INFO + "]: " + message;
            }
        } else if (type == 2) {
            if (timeWithDate == false) {
                return "[" + TADClass.GetTimeWithSeconds + " " + WARN + "]: " + message;
            } else {
                return "[" + TADClass.GetCurDateAndTimeWithSeconds + " " + WARN + "]: " + message;
            }
        } else if (type == 3) {
            if (timeWithDate == false) {
                return "[" + TADClass.GetTimeWithSeconds + " " + ERROR + "]: " + message;
            } else {
                return "[" + TADClass.GetCurDateAndTimeWithSeconds + " " + ERROR + "]: " + message;
            }
        } else {
            if (timeWithDate == false) {
                return "[" + TADClass.GetTimeWithSeconds + "]: " + message;
            } else {
                return "[" + TADClass.GetCurDateAndTimeWithSeconds + "]: " + message;
            }
        }
    }

}

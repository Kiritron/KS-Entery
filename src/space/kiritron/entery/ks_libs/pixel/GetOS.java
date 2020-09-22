package space.kiritron.entery.ks_libs.pixel;

/**
 * Класс с методами, которые позволяют получить информацию об ОС.
 * @author Киритрон Стэйблкор и MR.REX
 * @version 1.1
 */

//Киритрон: Вношу изменения уже второй раз, так как немного обосрался с контролем версий и потерял
//новую версию данного класса.
public class GetOS {
    public static String Name = "os.name",
                         Arch = "os.arch",
                         Version = "os.version",
                         User = "user.name",
                         UserDir = "user.dir",
                         UserHome = "user.home";

    /**
     * Узнать имя ОС, на которой запущено данное ПО.
     * @return возвращает имя операционной системы.
     */
    public static String Get() {
        //Киритрон: Сохранено, чтобы не было проблем с совместимостью.
        return System.getProperty("os.name");
    }

    /**
     * Позволяет получить данные об ОС, на которой запущено данное ПО.
     * @param Type Какую информацию необходимо получить об ОС. Доступны Name, Arch, Version, User, UserDir, UserHome.
     * @return возвращает ту информацию об ОС, которую вы запросили.
     */
    public static String GetOther(String Type) {
        return System.getProperty(Type);
    }
}

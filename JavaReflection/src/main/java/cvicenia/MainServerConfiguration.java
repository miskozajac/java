package cvicenia;

import cvicenia.web.WebServer;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MainServerConfiguration {

    public static void main (String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        //initConfiguration musim volať aby sa aspon raz spravila instancia ServerConfiguration. Keby v ServerConfiguration necham
        //v getInstance new ServerConfiguration tak netreba volať túto metodu, server by fungoval ale zakaždym by sa vytvarala nova instancia ServerConfiguration
        initConfiguration();

        //ak zavolam initConfiguration druhy krat tak v sa vráti tá istá servConf zo ServerConfiguration teda druhy krat uz null nebude
        //initConfiguration();
        WebServer web = new WebServer();
        web.startServer();
    }

    private static void initConfiguration() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<ServerConfiguration> kon = ServerConfiguration.class.getDeclaredConstructor(int.class,String.class);
        kon.setAccessible(true);
        kon.newInstance(8080,"ahojte");
    }
}

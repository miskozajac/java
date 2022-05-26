/*
 *  MIT License
 *
 *  Copyright (c) 2020 Michael Pogrebinsky - Java Reflection - Master Class
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package cvicenia.polymorfizmus;

import cvicenia.polymorfizmus.database.DatabaseClient;
import cvicenia.polymorfizmus.http.HttpClient;
import cvicenia.polymorfizmus.logging.FileLogger;
import cvicenia.polymorfizmus.udp.UdpClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainPolymorfizm {

    public static void main(String[] args) throws Throwable {
        DatabaseClient databaseClient = new DatabaseClient();
        HttpClient httpClient1 = new HttpClient("123.456.789.0");
        HttpClient httpClient2 = new HttpClient("11.33.55.0");
        FileLogger fileLogger = new FileLogger();
        UdpClient udpClient = new UdpClient();

        String body = "Caute chuji";
        List<Class<?>> params = Arrays.asList(new Class<?>[]{String.class});

        spustVsetkyMetody(groupExecutors(Arrays.asList(databaseClient,httpClient1,httpClient2,fileLogger,udpClient),params),body);
    }

    private static void spustVsetkyMetody(Map<Object,Method> mapa, String requestBody) throws InvocationTargetException, IllegalAccessException {
        for (Map.Entry<Object,Method> item : mapa.entrySet()) {
            Object instancia = item.getKey();
            Method metoda = item.getValue();

            //invoke spusti metodu, je to jedno znajrozširenejšich pouziti java reflection, návratový typ je Object
            //mi pouzivame teraz len void a boolean metody
            Boolean result = (Boolean) metoda.invoke(instancia,requestBody);

            if (result != null && result.equals(Boolean.FALSE)) {
                System.out.println("Negative result. Aborting ...");
                return;
            }
        }
    }

    //metoda ktora zgrupy instancie v main s ich prislušnymi metodami
    private static Map<Object, Method> groupExecutors(List<Object> objekty, List<Class<?>> methodParameterTyps) {
        Map<Object, Method> mapInstanceToMethod = new HashMap<>();

        for (Object o : objekty) {
            Method[] metody = o.getClass().getDeclaredMethods();

            for (Method m : metody) {
                //equals na liste porovna itemy jedneho po druhom preto je vstupny parameter ako list a aj m.getParamTypes zaobalime do listu
                if (Arrays.asList(m.getParameterTypes()).equals(methodParameterTyps)) {
                    mapInstanceToMethod.put(o,m);
                }
            }
        }

        return mapInstanceToMethod;
    }
}

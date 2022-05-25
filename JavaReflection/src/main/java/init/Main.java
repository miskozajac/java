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

package init;

import game.Game;
import game.internal.TicTacToeGame;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tic Tac Toe
 * https://www.udemy.com/course/java-reflection-master-class
 */

public class Main {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        //ak odstranim public z TicTacToeGame klasy tak môžem hru zavolať:
        //Game hra = (Game) vytvorObjectRecursivne(Class.forName("game.internal.TicTacToeGame"));
        Game hra = vytvorObjectRecursivne(TicTacToeGame.class);
        hra.startGame();
    }

    public static <T> T vytvorObjectRecursivne(Class<?> klas) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> kon = vratPrvyConstructor(klas);

        List<Object> konArgs = new ArrayList<>();

        for (Class<?> arg : kon.getParameterTypes()) {
            Object hodnotaArgumentu = vytvorObjectRecursivne(arg);
            konArgs.add(hodnotaArgumentu);
        }

        kon.setAccessible(true);
        return (T) kon.newInstance(konArgs.toArray());
    }

    public static Constructor<?> vratPrvyConstructor(Class<?> klas) {
        Constructor<?>[] kons = klas.getDeclaredConstructors();
        if (kons.length == 0) {
            throw new IllegalStateException(String.format("Ziadny kons pre klasu %s",klas.getName()));
        }
        return kons[0];
    }
}

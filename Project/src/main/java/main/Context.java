package main;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Snach on 26.03.16.
 */
public class Context {
    @NotNull
    private final Map<Class, Object> contextMap = new HashMap<>();

    public void put(@NotNull Class clazz, @NotNull Object object){
        if (contextMap.containsKey(clazz))
            System.out.append("В Context уже есть класс " + clazz.getName() );
        else
            contextMap.put(clazz, object);
    }

    @Nullable
    public <T> T get(@NotNull Class<T> clazz){

        try {
            //noinspection unchecked
            return (T) contextMap.get(clazz);
        }
        catch (NullPointerException e) {
            System.out.append("В Context нет класса " + clazz.getName());
            return null;
        }
    }
}

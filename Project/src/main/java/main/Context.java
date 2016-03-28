package main;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.lang.IllegalStateException;

/**
 * Created by Snach on 26.03.16.
 */
public class Context {
    @NotNull
    private final Map<Class, Object> contextMap = new HashMap<>();
    @SuppressWarnings("ConstantNamingConvention")
    private static final Logger logger = new Logger(Context.class);

    public void put(@NotNull Class clazz, @NotNull Object object){
        try {
            contextMap.containsKey(clazz);
            contextMap.put(clazz, object);
        }
        catch (IllegalStateException e) {
            logger.log("В Context уже есть класс " + clazz.getName() );
        }
    }

    @Nullable
    public <T> T get(@NotNull Class<T> clazz){

        //noinspection unchecked
        if (contextMap.get(clazz) == null) {
            logger.log("В Context нет класса " + clazz.getName());
            return null;
        }
        else {
            //noinspection unchecked
            return (T) contextMap.get(clazz);
        }
    }
}

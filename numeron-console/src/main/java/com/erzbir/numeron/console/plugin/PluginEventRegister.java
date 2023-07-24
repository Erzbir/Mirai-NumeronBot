package com.erzbir.numeron.console.plugin;

import com.erzbir.numeron.annotation.Message;
import com.erzbir.numeron.console.exception.PluginIllegalException;
import com.erzbir.numeron.core.context.AppContext;
import com.erzbir.numeron.core.filter.annotation.MessageAnnotationChannelFilter;
import com.erzbir.numeron.core.handler.excute.EventMethodExecute;
import com.erzbir.numeron.utils.NumeronLogUtil;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.MessageEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Erzbir
 * @Date: 2023/6/30 11:59
 */
public class PluginEventRegister {
    public static final PluginEventRegister INSTANCE = new PluginEventRegister();

    private PluginEventRegister() {

    }

    public void register(PluginContext pluginContext) {
        EventChannel<BotEvent> eventEventChannel = GlobalEventChannel.INSTANCE
                .filterIsInstance(BotEvent.class);
        pluginContext.getClasses().forEach(t -> {
            Method[] declaredMethods = t.getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                if (!declaredMethod.isAnnotationPresent(Message.class) && !declaredMethod.isAnnotationPresent(com.erzbir.numeron.annotation.Event.class)) {
                    continue;
                }
                Message message = declaredMethod.getAnnotation(Message.class);
                com.erzbir.numeron.annotation.Event eventAnnotation = declaredMethod.getAnnotation(com.erzbir.numeron.annotation.Event.class);
                if (message != null) {
                    MessageAnnotationChannelFilter filter = new MessageAnnotationChannelFilter();
                    filter.setAnnotation(message);
                    try {
                        Constructor<?> constructor = t.getConstructor();
                        constructor.setAccessible(true);
                        Object object = constructor.newInstance();
                        AppContext.INSTANCE.addToContext(object);
                        EventMethodExecute.INSTANCE.execute(declaredMethod, object, eventEventChannel.parentScope(pluginContext.getPlugin()).filter(event -> event instanceof MessageEvent messageEvent && filter.filter(messageEvent)), message);
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                             InstantiationException e) {
                        NumeronLogUtil.logger.error("ERROR", e);
                        throw new PluginIllegalException(e);
                    }
                } else if (eventAnnotation != null) {
                    try {
                        EventMethodExecute.INSTANCE.execute(declaredMethod, pluginContext.getPlugin(), eventEventChannel, eventAnnotation);
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        NumeronLogUtil.logger.error("ERROR", e);
                        throw new PluginIllegalException(e);
                    }
                }
            }

        });
    }
}

package com.erzbir.numeron.core.handler.excute;

import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.MessageEvent;

import java.lang.reflect.Method;

/**
 * @author Erzbir
 * @Date: 2022/11/28 10:32
 * <p>消息处理方法</p>
 */
public class MessageMethodExecute implements MethodExecute, RegisterEventHandle {
    public static final MessageMethodExecute INSTANCE = new MessageMethodExecute();

    private MessageMethodExecute() {
    }

    @Override
    public void execute(Method method, Object bean, EventChannel<BotEvent> channel) {
        register(channel, MessageEvent.class, method, bean);
    }
}

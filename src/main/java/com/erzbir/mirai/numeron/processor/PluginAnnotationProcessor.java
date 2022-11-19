package com.erzbir.mirai.numeron.processor;

import com.erzbir.mirai.numeron.annotation.Processor;
import com.erzbir.mirai.numeron.interfaces.ChannelFilterInter;
import com.erzbir.mirai.numeron.interfaces.PluginRegister;
import kotlin.jvm.internal.Intrinsics;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.events.BotEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


/**
 * @author Erzbir
 * @Date: 2022/11/17 09:49
 */
@Processor
@Slf4j
public class PluginAnnotationProcessor implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {
    public static ApplicationContext context;
    public static EventChannel<BotEvent> channel;
    public static Bot bot;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext context) throws BeansException {
        PluginAnnotationProcessor.context = context;
    }

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        bot = MessageAnnotationProcessor.bot;
        channel = bot.getEventChannel();
        Intrinsics.checkNotNull(channel);
        log.info("开始过滤插件监听......");
        context.getBeansOfType(ChannelFilterInter.class).forEach((k, v) -> {
            log.info("扫瞄到" + k);
            channel = channel.filter(v::filter);
        });
        log.info("插件监听过滤完成");
        log.info("开始插件注册.......");
        context.getBeansOfType(PluginRegister.class).forEach((k, v) -> {
            log.info("扫瞄到" + k);
            v.register(bot, channel);
        });
        log.info("插件注册完毕");
    }
}

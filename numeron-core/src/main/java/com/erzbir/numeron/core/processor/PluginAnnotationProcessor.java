package com.erzbir.numeron.core.processor;

import com.erzbir.numeron.core.entity.NumeronBot;
import com.erzbir.numeron.core.filter.PluginChannelFilterInter;
import com.erzbir.numeron.core.handler.PluginRegister;
import com.erzbir.numeron.core.utils.MiraiLogUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.events.BotEvent;

/**
 * @author Erzbir
 * @Date: 2022/11/17 09:49
 * <p>
 * 这个类的逻辑和作用和MessageAnnotationProcessor的逻辑类似, 这个类用来给插件注册和过滤
 * </p>
 */
@SuppressWarnings("unused")
public class PluginAnnotationProcessor implements Processor {
    public static EventChannel<BotEvent> channel;
    public static Bot bot;

    private static void registerPlugin(AppContext context) {
        context.getBeansWithInter(PluginRegister.class).forEach((k, v) -> {
            MiraiLogUtil.info("扫瞄到插件 " + k);
            v.register(bot, channel);
        });
    }

    private static void registerFilter(AppContext context) {
        context.getBeansWithInter(PluginChannelFilterInter.class).forEach((k, v) -> {
            MiraiLogUtil.info("扫瞄到过滤器 " + k);
            channel = channel.filter(v::filter);
        });
    }

    @Override
    public void onApplicationEvent() {
        AppContext context = AppContext.INSTANT;
        bot = NumeronBot.INSTANCE.getBot();
        channel = bot.getEventChannel();
        MiraiLogUtil.info("开始过滤插件监听......");
        // 扫瞄插件过滤器
        registerFilter(context);
        MiraiLogUtil.info("插件监听过滤完成\n");
        MiraiLogUtil.info("开始注册插件监听.......");
        // 扫瞄插件
        registerPlugin(context);
        MiraiLogUtil.info("插件监听注册完毕\n");
    }
}
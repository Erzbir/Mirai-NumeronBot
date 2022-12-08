package com.erzbir.mirai.numeron.plugins.qqmanage.action;

import com.erzbir.mirai.numeron.configs.GlobalConfig;
import com.erzbir.mirai.numeron.plugins.Plugin;
import com.erzbir.mirai.numeron.plugins.PluginRegister;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

/**
 * @author Erzbir
 * @Date: 2022/11/27 13:07
 * 黑名单检测
 */
@Plugin
@SuppressWarnings("unused")
public class ScanBlack implements PluginRegister {

    @Override
    public void register(Bot bot, EventChannel<BotEvent> channel) {
        bot.getEventChannel().filter(f -> GlobalConfig.isOn && f instanceof GroupMessageEvent event
                        && GlobalConfig.blackList.contains(event.getSender().getId())
                        && event.getGroup().getBotPermission().getLevel() != 0)
                .subscribeAlways(GroupMessageEvent.class, event -> ((NormalMember) event.getSender()).kick("黑名单用户"));
    }
}
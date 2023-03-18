package com.erzbir.mirai.numeron.plugins.qqmanage.action;

import com.erzbir.mirai.numeron.entity.BlackList;
import com.erzbir.mirai.numeron.handler.Plugin;
import com.erzbir.mirai.numeron.handler.PluginRegister;
import com.erzbir.mirai.numeron.menu.Menu;
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
@Menu(name = "黑名单检测")
@SuppressWarnings("unused")
public class ScanBlack implements PluginRegister {

    @Override
    public void register(Bot bot, EventChannel<BotEvent> channel) {
        channel.filter(f -> f instanceof GroupMessageEvent event
                        && BlackList.INSTANCE.contains(event.getSender().getId())
                        && event.getGroup().getBotPermission().getLevel() != 0)
                .subscribeAlways(GroupMessageEvent.class, event -> ((NormalMember) event.getSender()).kick("黑名单用户"));
    }
}
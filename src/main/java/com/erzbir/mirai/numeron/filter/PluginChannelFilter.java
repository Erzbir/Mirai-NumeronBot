package com.erzbir.mirai.numeron.filter;

import com.erzbir.mirai.numeron.configs.GlobalConfig;
import com.erzbir.mirai.numeron.configs.entity.BlackList;
import com.erzbir.mirai.numeron.configs.entity.GroupList;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.GroupMemberEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;

/**
 * @author Erzbir
 * @Date: 2022/11/17 09:43
 * 插件消息事件过滤类
 */
@Filter
@SuppressWarnings("unused")
public class PluginChannelFilter implements PluginChannelFilterInter {
    @Override
    public Boolean filter(BotEvent event) {
        if (event instanceof MessageEvent event1) {
            if (event1 instanceof GroupMessageEvent event2) {
                return GlobalConfig.isOn
                        && GroupList.INSTANCE.contains(event2.getGroup().getId())
                        && !BlackList.INSTANCE.contains(event2.getSender().getId());
            } else {
                return GlobalConfig.isOn
                        && !BlackList.INSTANCE.contains(((MessageEvent) event).getSender().getId());
            }
        } else if (event instanceof GroupMemberEvent event1) {
            return !BlackList.INSTANCE.contains(event1.getMember().getId());
        } else {
            return GlobalConfig.isOn;
        }
    }
}

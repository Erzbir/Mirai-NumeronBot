package com.erzbir.numeron.plugin.onoff;

import com.erzbir.numeron.annotation.Command;
import com.erzbir.numeron.annotation.Listener;
import com.erzbir.numeron.annotation.Message;
import com.erzbir.numeron.api.entity.WhiteService;
import com.erzbir.numeron.core.bot.NumeronBot;
import com.erzbir.numeron.filter.FilterRule;
import com.erzbir.numeron.filter.MessageRule;
import com.erzbir.numeron.filter.PermissionType;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.events.MessageEvent;

/**
 * @author Erzbir
 * @Date: 2022/11/27 09:26
 * <p>
 * 开关机命令
 * </p>
 */
@Listener
@SuppressWarnings("unused")
public class OnOffCommandHandle {

    @Command(
            name = "开关机",
            dec = "关机",
            help = "/shutdown",
            permission = PermissionType.WHITE
    )
    @Message(
            text = "/shutdown",
            filterRule = FilterRule.NONE,
            messageRule = MessageRule.EQUAL,
            permission = PermissionType.WHITE
    )
    private void shutdown(MessageEvent e) {
        if (NumeronBot.INSTANCE.isEnable()) {
            e.getSubject().sendMessage("关机");
            NumeronBot.INSTANCE.shutdown();
        }
        if (!NumeronBot.INSTANCE.isEnable()) {
            NumeronBot.INSTANCE.getBot().getEventChannel().subscribe(MessageEvent.class, event -> {
                if ((WhiteService.INSTANCE.exist(event.getSender().getId()) || event.getSender().getId() == NumeronBot.INSTANCE.getMaster()) && event.getMessage().contentToString().equals("/launch")) {
                    e.getSubject().sendMessage("开机");
                    NumeronBot.INSTANCE.launch();
                    return ListeningStatus.STOPPED;
                } else {
                    return ListeningStatus.LISTENING;
                }
            });
        }
    }

    @Command(
            name = "开关机",
            dec = "开机",
            help = "/launch",
            permission = PermissionType.MASTER
    )
    public void launch() {
    }
}

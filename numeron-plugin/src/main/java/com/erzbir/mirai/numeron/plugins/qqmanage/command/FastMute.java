package com.erzbir.mirai.numeron.plugins.qqmanage.command;

import com.erzbir.mirai.numeron.entity.NumeronBot;
import com.erzbir.mirai.numeron.filter.message.MessageRule;
import com.erzbir.mirai.numeron.filter.permission.PermissionType;
import com.erzbir.mirai.numeron.filter.rule.FilterRule;
import com.erzbir.mirai.numeron.handler.Command;
import com.erzbir.mirai.numeron.listener.Listener;
import com.erzbir.mirai.numeron.listener.massage.UserMessage;
import com.erzbir.mirai.numeron.utils.ConfigCreateUtil;
import net.mamoe.mirai.event.events.UserMessageEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Erzbir
 * @Date: 2023/2/22 23:19
 */
@Listener
@SuppressWarnings("unused")
public class FastMute {
    private final List<Long> group = new ArrayList<>();

    {
        String confFile = NumeronBot.INSTANCE.getFolder() + "plugin-configs/qqmanage" + "/config.conf";
        ConfigCreateUtil.createFile(confFile);
        try (BufferedReader in = new BufferedReader(new FileReader(confFile))) {
            String s;
            while ((s = in.readLine()) != null) {
                group.add(Long.valueOf(s));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Command(name = "打卡解/禁言", dec = "打卡禁言", help = "#打卡禁言")
    @UserMessage(text = "#打卡禁言", filterRule = FilterRule.BLACK, messageRule = MessageRule.EQUAL, permission = PermissionType.ALL)
    private void fastMute(UserMessageEvent event) {
        System.out.println(group);
        group.forEach(t -> Objects.requireNonNull(NumeronBot.INSTANCE.getBot().getGroup(t)).getSettings().setMuteAll(true));
    }

    @Command(name = "打卡解/禁言", dec = "打卡解禁", help = "#打卡禁言")
    @UserMessage(text = "#打卡解禁", filterRule = FilterRule.BLACK, messageRule = MessageRule.EQUAL, permission = PermissionType.ALL)
    private void fastUnMute(UserMessageEvent event) {
        group.forEach(t -> Objects.requireNonNull(NumeronBot.INSTANCE.getBot().getGroup(t)).getSettings().setMuteAll(false));
    }
}

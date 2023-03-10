package com.erzbir.mirai.numeron.plugins.rss.listner;

import com.erzbir.mirai.numeron.filter.message.MessageRule;
import com.erzbir.mirai.numeron.filter.permission.PermissionType;
import com.erzbir.mirai.numeron.filter.rule.FilterRule;
import com.erzbir.mirai.numeron.handler.Command;
import com.erzbir.mirai.numeron.listener.Listener;
import com.erzbir.mirai.numeron.listener.massage.Message;
import com.erzbir.mirai.numeron.plugins.rss.api.EditApi;
import com.erzbir.mirai.numeron.plugins.rss.api.ViewApi;
import com.erzbir.mirai.numeron.plugins.rss.config.RssConfig;
import net.mamoe.mirai.event.events.MessageEvent;

/**
 * @author Erzbir
 * @Date: 2023/3/10 10:48
 */
@Listener
public class ConfigCommand {
    @Command(
            name = "修改订阅名",
            dec = "#rename [id] [name]",
            help = "#sub https://xxx.xxx",
            permission = PermissionType.ADMIN
    )
    @Message(
            text = "^#rename\\s+?\\d+?\\s+?\\S+",
            messageRule = MessageRule.REGEX,
            filterRule = FilterRule.BLACK,
            permission = PermissionType.ADMIN
    )
    private void rename(MessageEvent event) {
        String[] s = event.getMessage().contentToString().replaceFirst("^#rename\\s+?\\d+?\\s+?\\S+", "").split("\\s+");
        EditApi.editName(s[0], s[1]);
    }

    @Command(
            name = "修改订阅链接",
            dec = "#url [id] [url]",
            help = "#url https://xxx.xxx",
            permission = PermissionType.ADMIN
    )
    @Message(
            text = "^#url\\s+?\\d+?\\s+?(http[s]*://.*)",
            messageRule = MessageRule.REGEX,
            filterRule = FilterRule.BLACK,
            permission = PermissionType.ADMIN
    )
    private void renameUrl(MessageEvent event) {
        String[] s = event.getMessage().contentToString().replaceFirst("^#url\\s+?\\d+?\\s+?(https*://.*)", "").split("\\s+");
        EditApi.editUrl(s[0], s[1]);
    }

    @Command(
            name = "查看指定订阅配置",
            dec = "#list [id]",
            help = "#list 1",
            permission = PermissionType.ADMIN
    )
    @Message(
            text = "^#list\\s+?\\d+",
            messageRule = MessageRule.REGEX,
            filterRule = FilterRule.BLACK,
            permission = PermissionType.ADMIN
    )
    private void list(MessageEvent event) {
        String id = event.getMessage().contentToString().replaceFirst("#list\\s+?", "");
        event.getSubject().sendMessage(ViewApi.viewRss(id));
    }

    @Command(
            name = "查看所有订阅配置列表",
            dec = "#list all",
            help = "#list all",
            permission = PermissionType.ADMIN
    )
    @Message(
            text = "#list all",
            filterRule = FilterRule.BLACK,
            permission = PermissionType.ADMIN
    )
    private void listAll(MessageEvent event) {
        event.getSubject().sendMessage(ViewApi.viewAllRss());
    }

    @Command(
            name = "查看当前配置",
            dec = "#config rss",
            help = "#config rss",
            permission = PermissionType.ADMIN
    )
    @Message(
            text = "#config rss",
            filterRule = FilterRule.BLACK,
            permission = PermissionType.ADMIN
    )
    private void echoConfig(MessageEvent event) {
        event.getSubject().sendMessage(ViewApi.viewAllConfig());
    }

    @Command(
            name = "保存当前配置",
            dec = "#config save",
            help = "#config save",
            permission = PermissionType.ADMIN
    )
    @Message(
            text = "#config save",
            filterRule = FilterRule.BLACK,
            permission = PermissionType.MASTER
    )
    private void saveConfig(MessageEvent event) {
        event.getSubject().sendMessage(String.valueOf(RssConfig.getInstance().save()));
    }
}

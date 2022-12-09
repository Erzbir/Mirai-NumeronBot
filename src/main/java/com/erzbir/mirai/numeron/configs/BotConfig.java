package com.erzbir.mirai.numeron.configs;

import com.erzbir.mirai.numeron.configs.entity.BlackList;
import com.erzbir.mirai.numeron.configs.entity.GroupList;
import com.erzbir.mirai.numeron.configs.entity.IllegalList;
import com.erzbir.mirai.numeron.configs.entity.WhiteList;
import com.erzbir.mirai.numeron.utils.MiraiLogUtil;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author Erzbir
 * @Date: 2022/11/16 21:14
 * <p>
 * 配置类
 * </p>
 */
@Configuration
@Slf4j
@ComponentScan(basePackages = "com.erzbir.mirai.numeron")
//@PropertySource (value = "classpath:application.properties", encoding = "utf-8")
public class BotConfig {
    private static final String botName = "Numeron";
    private static final String OS = System.getProperty("os.name");
    private static final String HOME = System.getenv("HOME");
    private static final String deviceInfo = "device.json";
    private static final String mainDir = "erzbirnumeron/"; // 文件存储目录
    private static final String configDir = mainDir + "config/"; // 配置文件目录
    private static final String configName = "BotConfig.properties"; // 配置文件名
    private static final String botDir = mainDir + "bots/"; // bot目录
    private static Long master; // 主人
    private static Long account; // 帐号
    private static String password; // 密码
    private static Bot bot; // 唯一bot实例
    private static String WORKDIR; // 工作目录
    private static BotConfiguration.HeartbeatStrategy heartbeatStrategy = BotConfiguration.HeartbeatStrategy.STAT_HB;
    private static BotConfiguration.MiraiProtocol miraiProtocol = BotConfiguration.MiraiProtocol.ANDROID_PAD;

    static {
        init();
        MiraiLogUtil.info("开始载入数据库数据");
        MiraiLogUtil.info("违禁词列表: " + IllegalList.INSTANCE);
        MiraiLogUtil.info("启用群列表: " + GroupList.INSTANCE);
        MiraiLogUtil.info("黑名单列表: " + BlackList.INSTANCE);
        MiraiLogUtil.info("白名单列表: " + WhiteList.INSTANCE);
        MiraiLogUtil.info("载入数据成功\n");
    }

    public static Bot getBot() {
        return bot;
    }

    private static void init() {
        FileInputStream fileInputStream = null;
        Properties properties = new Properties();
        try {
            fileInputStream = new FileInputStream(configDir + configName);
            properties.load(fileInputStream);
        } catch (IOException ignored) {

        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        MiraiLogUtil.info("加载机器人配置");
        if (!properties.isEmpty() && properties.getProperty("enable").equals("true")) {
            master = Long.parseLong(properties.getProperty("master"));
            account = Long.parseLong(properties.getProperty("account"));
            password = properties.getProperty("password");
            heartbeatStrategy = BotConfiguration.HeartbeatStrategy.valueOf((properties.getProperty("heartbeatStrategy")));
            miraiProtocol = BotConfiguration.MiraiProtocol.valueOf((properties).getProperty("protocol"));
            MiraiLogUtil.info("机器人配置加载成功");
            return;
        } else {
            MiraiLogUtil.info("帐号配置为空或者未启用, 将手动输入");
        }
        Scanner scan = new Scanner(System.in);
        while (account == null) {
            try {
                System.out.print("输入帐号: ");
                account = scan.nextLong();
            } catch (IllegalArgumentException e) {
                System.out.println("请输入数字帐号!!!!");
            }
        }
        scan.nextLine();
        while (password == null) {
            System.out.print("输入密码: ");
            password = scan.nextLine();
        }
        while (master == null) {
            System.out.print("输入主人QQ: ");
            master = scan.nextLong();
        }
        scan.close();
        MiraiLogUtil.info("配置成功, 将保存配置....");
        save();
    }

    private static void save() {
        MiraiLogUtil.info("开始保存配置......");
        FileOutputStream outputStream = null;
        Properties properties;
        File file = new File(configDir);
        if (!file.exists()) {
            if (!file.mkdir()) {
                return;
            }
        }
        try {
            outputStream = new FileOutputStream(configDir + configName);
            properties = new Properties();
            properties.setProperty("master", String.valueOf(master));
            properties.setProperty("account", String.valueOf(account));
            properties.setProperty("password", password);
            properties.setProperty("protocol", miraiProtocol.name());
            properties.setProperty("heartbeatStrategy", heartbeatStrategy.name());
            properties.setProperty("enable", "true");
            properties.store(outputStream, null);
            MiraiLogUtil.info("保存成功\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getInfo() {
        return "\tName: " + botName + "\n\n" +
                "\tOS: " + OS + "\n\n" +
                "\tHOME: " + HOME + "\n\n" +
                "\tMaster: " + master + "\n\n" +
                "\tIllegalList: " + IllegalList.INSTANCE + "\n\n" +
                "\tGroupList: " + GroupList.INSTANCE + "\n\n" +
                "\tBlackList: " + BlackList.INSTANCE + "\n\n" +
                "\tWhiteList: " + WhiteList.INSTANCE + "\n\n";
    }

    public static boolean isMaster(Long id) {
        return Objects.equals(id, master);
    }

    public static String getWORKDIR() {
        return WORKDIR;
    }

    public static Long getMaster() {
        return master;
    }

    @Bean
    Bot bot() {
        WORKDIR = botDir + account;
        File file = new File(WORKDIR);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return bot;
            }
        }
        bot = BotFactory.INSTANCE.newBot(account, password, new BotConfiguration() {
            {
                setWorkingDir(new File(WORKDIR)); // 工作目录
                setHeartbeatStrategy(heartbeatStrategy); // 心跳策略
                setProtocol(miraiProtocol); // 登陆协议
                fileBasedDeviceInfo(deviceInfo); // 文件保存的名字
            }
        });
        return bot;
    }
}

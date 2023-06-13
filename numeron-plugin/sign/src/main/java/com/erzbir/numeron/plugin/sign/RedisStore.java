package com.erzbir.numeron.plugin.sign;

import com.erzbir.numeron.api.NumeronImpl;
import com.erzbir.numeron.utils.ConfigCreateUtil;
import com.erzbir.numeron.utils.NumeronLogUtil;
import kotlin.jvm.functions.Function1;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Jedis;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Erzbir
 * @Date: 2022/11/30 10:32
 */
public final class RedisStore {
    private static final Object key = new Object();
    private static volatile RedisStore INSTANCE;
    private final Jedis client;
    private String host;
    private int port;
    private String user;
    private String password;

    private RedisStore() {
        Properties properties = new Properties();
        String confFile = NumeronImpl.INSTANCE.getPluginWorkDir() + "plugin-configs/sql/redisconfig.properties";
        try {
            ConfigCreateUtil.createFile(confFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (FileReader fileReader = new FileReader(confFile)) {
            properties.load(fileReader);
            host = Objects.requireNonNullElse(properties.getProperty("host"), "localhost");
            port = Integer.parseInt(Objects.requireNonNullElse(properties.getProperty("port"), "6379"));
            user = properties.getProperty("user");
            password = properties.getProperty("password");
        } catch (IOException e) {
            NumeronLogUtil.logger.error("ERROR", e);
        }
        client = new Jedis(host, port);
        if (!user.isEmpty() || !password.isEmpty()) {
            client.auth(user, password);
        }
    }

    public static RedisStore getInstance() {
        if (INSTANCE == null) {
            synchronized (key) {
                if (INSTANCE == null) {
                    INSTANCE = new RedisStore();
                }
            }
        }
        return INSTANCE;
    }

    public void set(String key, String value, long seconds) {
        if (seconds == -1L) {
            client.set(key, value);
        } else {
            client.setex(key, seconds, value);
        }

    }

    private List<String> toList(Set<String> keys) {
        List<String> list = new ArrayList<>();
        if (keys != null) {
            keys.forEach(v -> list.add(client.get(v)));
        }
        return list;
    }


    public void del(String key) {
        client.del(key);
    }

    public String get(String key) {
        return client.get(key);
    }

    public @NotNull List<String> getPic() {
        return toList(client.keys("pic_*"));
    }

    public @NotNull List<String> getFile() {
        return toList(client.keys("file_*"));
    }

    public @NotNull List<String> getPlain() {
        return toList(client.keys("plain_*"));
    }


    public void removePic(String filename) throws Exception {
        Stream<String> stringStream;
        try {
            stringStream = client.keys("pic_*").stream().filter(v -> Objects.equals(v, filename));
        } catch (Exception e) {
            throw new Exception(e);
        }
        stringStream.forEach(client::del);
    }

    public void removeFile(@NotNull String filename) throws Exception {
        Stream<String> stringStream;
        try {
            stringStream = client.keys("file_*").stream().filter(v -> Objects.equals(v, filename));
        } catch (Exception e) {
            throw new Exception(e);
        }
        stringStream.forEach(client::del);
    }

    public void removePlain(String filename) throws Exception {
        Stream<String> stringStream;
        try {
            stringStream = client.keys("plain_*").stream().filter(v -> Objects.equals(v, filename));
        } catch (Exception e) {
            throw new Exception(e);
        }
        stringStream.forEach(client::del);

    }

    public void useClient(Function1<Jedis, Object> function1) {
        function1.invoke(client);
    }
}

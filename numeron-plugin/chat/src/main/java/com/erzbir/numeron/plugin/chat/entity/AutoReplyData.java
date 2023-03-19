package com.erzbir.numeron.plugin.chat.entity;

import com.erzbir.numeron.core.utils.SqlUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.HashMap;

/**
 * @author Erzbir
 * @Date: 2022/11/25 17:21
 */
public final class AutoReplyData {
    public static final AutoReplyData INSTANCE = new AutoReplyData();

    static {
        String sql = """
                CREATE TABLE IF NOT EXISTS CHAT(
                KEY TEXT PRIMARY KEY NOT NULL,
                ANSWER TEXT,
                OP_ID BIGINT NOT NULL,
                OP_TIME TEXT NOT NULL
                )
                """;
        String findAll = "SELECT * from CHAT";
        ResultSet resultSet = null;
        try {
            SqlUtil.executeUpdateSQL(sql);
            resultSet = SqlUtil.getResultSet(findAll);
            while (resultSet.next()) {
                String key = resultSet.getString("KEY");
                String answer = resultSet.getString("ANSWER");
                INSTANCE.getData().put(key, answer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private final HashMap<String, String> data = new HashMap<>();

    private AutoReplyData() {
    }

    public void add(String key, String answer, Long id) {
        addD(key, answer);
        new Thread(() -> {
            try {
                addS(key, answer, id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void remove(String key, String answer) throws SQLException {
        removeD(key);
        new Thread(() -> {
            try {
                removeS(key, answer);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        removeS(key, answer);
    }

    private void addD(String key, String answer) {
        data.put(key, answer);
    }

    private void removeD(String key) {
        data.remove(key);
    }

    private void addS(String key, String answer, Long id) throws SQLException {
        String sql = "INSERT INTO CHAT(KEY, ANSWER, OP_ID, OP_TIME) " +
                "VALUES('" + key + "', '" + answer + "', " + id + ", '" + LocalTime.now() + "' " + ")";
        if (exist(key)) {
            sql = "UPDATE CHAT SET ANSWER = '" + answer + "' WHERE KEY = '" + answer + "'";
        }
        SqlUtil.executeUpdateSQL(sql);
    }

    private void removeS(String key, String answer) throws SQLException {
        if (!exist(key)) {
            return;
        }
        String sql = "DELETE FROM CHAT WHERE KEY = '" + key + "' and ANSWER = '" + answer + "'";
        SqlUtil.executeUpdateSQL(sql);
    }

    public String getAnswer(String key) {
        return data.get(key);
    }

    public boolean exist(String key) throws SQLException {
        String sql = "SELECT * FROM CHAT WHERE KEY = '" + key + "'";
        ResultSet resultSet = SqlUtil.getResultSet(sql);
        return resultSet == null;
    }

    public HashMap<String, String> getData() {
        return data;
    }
}

package com.erzbir.mirai.numeron.controller;

import com.erzbir.mirai.numeron.configs.GlobalConfig;
import com.erzbir.mirai.numeron.entity.IllegalList;

import java.util.Objects;

/**
 * @author Erzbir
 * @Date: 2022/11/13 17:30
 */
public class IllegalManager implements Action {
    private static IllegalManager INSTANCE;

    private IllegalManager() {

    }

    public static IllegalManager getInstance() {
        return Objects.requireNonNullElseGet(INSTANCE, () -> INSTANCE = new IllegalManager());
    }

    @Override
    public void add(Object s, String name, Long op_id) {
        IllegalList.INSTANCE.add((String) s, op_id);
    }

    @Override
    public String query(Object s) {
        if (s.equals("0")) {
            return GlobalConfig.blackList.toString();
        }
        if (GlobalConfig.illegalList.contains((String) s)) {
            return "在黑名单中";
        }
        return "查无此人";
    }

    @Override
    public void remove(Object s) {
        IllegalList.INSTANCE.remove((String) s);
    }
}
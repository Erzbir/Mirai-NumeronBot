package com.erzbir.mirai.numeron.controller;

import com.erzbir.mirai.numeron.interfaces.ActionFactory;

/**
 * @author Erzbir
 * @Date: 2022/11/13 23:13
 */
public class WhiteListActionFactory implements ActionFactory {
    public static final WhiteListActionFactory INSTANCE = new WhiteListActionFactory();
    private static final WhiteListAction INSTANCE1 = new WhiteListAction();

    private WhiteListActionFactory() {
    }

    @Override
    public Action build() {
        return INSTANCE1;
    }
}

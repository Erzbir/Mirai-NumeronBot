package com.erzbir.mirai.numeron.controller;

import com.erzbir.mirai.numeron.interfaces.ActionFactory;

/**
 * @author Erzbir
 * @Date: 2022/11/13 17:35
 */
public class BlackListActionFactory implements ActionFactory {
    public static final BlackListActionFactory INSTANCE = new BlackListActionFactory();
    private static final BlackListAction INSTANCE1 = new BlackListAction();

    private BlackListActionFactory() {
    }

    @Override
    public Action build() {
        return INSTANCE1;
    }

}

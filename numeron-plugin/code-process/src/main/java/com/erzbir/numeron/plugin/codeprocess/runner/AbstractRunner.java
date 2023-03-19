package com.erzbir.numeron.plugin.codeprocess.runner;

import com.erzbir.numeron.plugin.codeprocess.runway.RunCode;

/**
 * @author Erzbir
 * @Date: 2022/11/30 08:20
 */
public abstract class AbstractRunner {
    protected RunCode runCode;

    protected abstract RunCode getRunCode();

    protected abstract void setRunCode(RunCode runCode);
}

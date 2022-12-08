package com.erzbir.mirai.numeron.plugins.codeprocess.runway;

import com.erzbir.mirai.numeron.configs.GlobalConfig;
import com.erzbir.mirai.numeron.plugins.codeprocess.CodeUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * @author Erzbir
 * @Date: 2022/11/30 08:17
 */
public class RunJs extends RunCode {
    private static final Object key = new Object();
    private static final String codeDir = GlobalConfig.workDir + "/botCode/js";
    private static volatile RunJs INSTANCE;

    public static RunJs getInstance() {
        if (INSTANCE == null) {
            synchronized (key) {
                if (INSTANCE == null) {
                    INSTANCE = new RunJs();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public String execute(String code) throws IOException, ExecutionException, InterruptedException {
        File file;
        if (!(file = new File(codeDir)).exists()) {
            file.mkdirs();
        }
        String filename = UUID.randomUUID().toString().replace("-", "") + ".js";
        Path of = Path.of(codeDir, filename);
        try (FileWriter fileWriter = new FileWriter(of.toFile())) {
            fileWriter.write(code);
            fileWriter.flush();
        }
        Process process;
        String result;
        try {
            process = Runtime.getRuntime().exec(String.format("node %s", filename), null, file);
            result = CodeUtils.readResult(process, 30, "js");
        } catch (Exception e) {
            result = e.getMessage();
        } finally {
            new Thread(() -> {
                try {
                    Files.delete(of);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        return result;
    }
}
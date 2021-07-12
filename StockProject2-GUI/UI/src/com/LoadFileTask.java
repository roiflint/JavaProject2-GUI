package com;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class LoadFileTask extends Task<Boolean> {
    private final String file;
    public LoadFileTask(String fileName){
        this.file = fileName;
    }

    @Override
    protected Boolean call() throws Exception {
        updateProgress(0,100.0);
        updateMessage("Loading file...");
        Thread.sleep(350);
        updateProgress(5,100.0);
        Thread.sleep(350);
        updateProgress(10,100.0);
        Thread.sleep(350);
        return Main.eng.LoadXML(this.file,this::updateProgress,this::updateMessage).getSuccess();
    }
}

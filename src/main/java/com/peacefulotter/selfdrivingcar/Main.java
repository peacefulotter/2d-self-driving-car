package com.peacefulotter.selfdrivingcar;

import com.peacefulotter.selfdrivingcar.scenarios.Classic;
import com.peacefulotter.selfdrivingcar.scenarios.ModelParent;
import javafx.application.Application;

public class Main
{
    public static void main(String[] args) {
        Application.launch(ModelParent.class, args);
    }
}

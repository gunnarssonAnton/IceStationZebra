package org.example.controller;

import org.example.view.CompilationView;
import org.example.view.ExecutionView;

public class ExecutionViewController {
    ExecutionView view = new ExecutionView();

    public ExecutionView getView(){
        return this.view;
    }
}

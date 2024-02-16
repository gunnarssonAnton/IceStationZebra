package org.example.Utility;

import io.reactivex.rxjava3.core.Observable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessHandler {
    public static Observable<String> getOutput(String[] cmd) {
        return Observable.create(emitter -> {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(cmd);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                emitter.onNext(line);
            }
            // Waiting for the command to complete
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                emitter.onComplete();
            } else {
                emitter.onError(new Throwable("Exit:" + exitVal));
            }
        });
    }
}

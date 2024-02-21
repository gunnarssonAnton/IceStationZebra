package org.example.Utility;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class ProcessHandler {
    public static Observable<String> getOutput(String[] cmd) {
        return Observable.<String>create(emitter -> {
                    ProcessBuilder builder = new ProcessBuilder(cmd);
                    builder.redirectErrorStream(true);

                    Process process = builder.start();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while (!emitter.isDisposed() && (line = reader.readLine()) != null) {
                            emitter.onNext(line);
                        }
                        if (!emitter.isDisposed()) {
                            int exitVal = process.waitFor();
                            if (exitVal == 0) {
                                emitter.onComplete();
                            } else {
                                emitter.onError(new Throwable("Process exited with error code: " + exitVal));
                            }
                        }
                    } catch (Exception e) {
                        emitter.onError(e);
                    } finally {
                        process.destroy();
                    }
                })
                .subscribeOn(Schedulers.io());
    }
}

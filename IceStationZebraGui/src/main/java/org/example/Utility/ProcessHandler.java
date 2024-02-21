package org.example.Utility;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessHandler {
    private final Observable<String> stdout;
    private final Observable<String> stderr;
    private final Completable completion;

    private ProcessHandler(Observable<String> stdout, Observable<String> stderr, Completable completion) {
        this.stdout = stdout;
        this.stderr = stderr;
        this.completion = completion;
    }

    public static ProcessHandler construct(String[] cmd) {
        Observable<String> stdout = Observable.<String>create(emitter -> {
            try {
                ProcessBuilder builder = new ProcessBuilder(cmd);
                Process process = builder.start();

                new Thread(() -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null && !emitter.isDisposed()) {
                            emitter.onNext(line);
                        }
                        emitter.onComplete();
                    } catch (IOException e) {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    }
                }).start();
            } catch (IOException e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io());

        Observable<String> stderr = Observable.<String>create(emitter -> {
            try {
                ProcessBuilder builder = new ProcessBuilder(cmd);
                Process process = builder.start();

                new Thread(() -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                        String line;
                        while ((line = reader.readLine()) != null && !emitter.isDisposed()) {
                            emitter.onNext(line);
                        }
                        emitter.onComplete();
                    } catch (IOException e) {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    }
                }).start();
            } catch (IOException e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io());

        Completable completion = Completable.create(emitter -> {
            try {
                ProcessBuilder builder = new ProcessBuilder(cmd);
                Process process = builder.start();

                new Thread(() -> {
                    try {
                        int exitVal = process.waitFor();
                        if (exitVal == 0) {
                            emitter.onComplete();
                        } else {
                            emitter.onError(new Throwable("Process exited with error code: " + exitVal));
                        }
                    } catch (InterruptedException e) {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    } finally {
                        process.destroy();
                    }
                }).start();
            } catch (IOException e) {
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io());

        return new ProcessHandler(stdout, stderr, completion);
    }

    public Observable<String> getStdout() {
        return this.stdout;
    }

    public Observable<String> getStderr() {
        return this.stderr;
    }

    public Completable getCompletion() {
        return this.completion;
    }
}

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
        final Process process;
        try {
            process = new ProcessBuilder(cmd).start();
        } catch (IOException e) {
            // Handle the error appropriately. For simplicity, we'll just print the stack trace here.
            e.printStackTrace();
            // Return a default ProcessHandler instance or throw a custom exception as appropriate.
            return new ProcessHandler(Observable.empty(), Observable.empty(), Completable.complete());
        }

        Observable<String> stdout = Observable.create(emitter -> {
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
        });

        Observable<String> stderr = Observable.create(emitter -> {
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
        });

        Completable completion = Completable.create(emitter -> {
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
            }
        });

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

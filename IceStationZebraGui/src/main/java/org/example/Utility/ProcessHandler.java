package org.example.Utility;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

import java.io.*;

public class ProcessHandler {
    private final Observable<String> stdout;
    private final Observable<String> stderr;
    private final Completable completion;
    private final PublishSubject<String> stdin;
    private ProcessHandler(Observable<String> stdout, Observable<String> stderr, Completable completion,PublishSubject<String> stdin) {
        this.stdout = stdout;
        this.stderr = stderr;
        this.completion = completion;
        this.stdin = stdin;
    }
    public static ProcessHandler construct(String[] cmd) {
        final Process process;
        try {
            process = new ProcessBuilder(cmd).start();
        } catch (IOException e) {
            // Handle the error appropriately. For simplicity, we'll just print the stack trace here.
            e.printStackTrace();
            // Return a default ProcessHandler instance or throw a custom exception as appropriate.
            return new ProcessHandler(Observable.empty(), Observable.empty(), Completable.complete(), PublishSubject.create());
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

        PublishSubject<String> stdin = PublishSubject.create();
        stdin.subscribe(data -> {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
                writer.write(data);
                System.out.println("Sent " + data + " to stdin");
                writer.flush(); // Ensure data is sent to the process
            } catch (IOException e) {
                e.printStackTrace(); // Handle exception
            }
        }, error -> {
            // Handle any errors if needed
            System.err.println("Error writing to process stdin: " + error.getMessage());
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

        return new ProcessHandler(stdout, stderr, completion, stdin);
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

    public void stdin(String command) {
        this.stdin.onNext(command);
    }
}

package org.example.Utility;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import org.example.files.FileIO;

import java.awt.*;
import java.io.*;
import java.util.function.Consumer;

public class ProcessHandler {
    private PublishSubject<TerminalMessage> terminalSubject;
    private int exit;
    private Consumer<ProcessHandler> onCompleteCallback;
    private final Observable<String> stdout;
    private final Observable<String> stderr;
    private final Completable completion;
    private final PublishSubject<String> stdin;
    private final StringBuilder log = new StringBuilder("");
    private ProcessHandler(Observable<String> stdout, Observable<String> stderr, Completable completion,PublishSubject<String> stdin) {
        this.stdout = stdout;
        this.stderr = stderr;
        this.completion = completion;
        this.stdin = stdin;

    }
    private void addToLog(String line){
        this.log.append(line + "\n");
    }
    public void setOnComplete(Consumer<ProcessHandler> onCompleteCallback) {
        this.onCompleteCallback = onCompleteCallback;
    }
    public static ProcessHandler internal(String[] cmd, PublishSubject<TerminalMessage> terminal){
        ProcessHandler handler =  ProcessHandler.construct(cmd);
        handler.terminalSubject = terminal;

        // stdout
        handler.stdout.subscribeOn(Schedulers.io()).subscribe(out -> {
            handler.terminalSubject.onNext(new TerminalMessage(out, Color.WHITE));
            handler.addToLog(out);
        },throwable -> {
            handler.terminalSubject.onNext(new TerminalMessage(throwable.getMessage(),Color.RED));
            handler.addToLog(throwable.getMessage());
        },() -> {
            handler.terminalSubject.onNext(new TerminalMessage("ProcessHandler stdout exited normally",Color.green));
        });

        // stderr
        handler.stderr.subscribeOn(Schedulers.io()).subscribe(err ->{
            handler.terminalSubject.onNext(new TerminalMessage(err, Color.orange));
            handler.addToLog(err);
        },throwable -> {
            handler.terminalSubject.onNext(new TerminalMessage(throwable.getMessage(), Color.red));
            handler.addToLog(throwable.getMessage());
        },() -> {
            handler.terminalSubject.onNext(new TerminalMessage("ProcessHandler stderr exited normally",Color.green));
        });

        // Completion
        handler.completion.subscribeOn(Schedulers.io()).subscribe(() -> {
            handler.exit = 0;
            if (handler.onCompleteCallback != null)
                handler.onCompleteCallback.accept(handler);
            handler.addToLog("--> END <--");
            FileIO file = new FileIO("logs/",Generate.generateRandomString(12) + ".txt");
        },throwable -> {
            handler.exit = Integer.parseInt((throwable.getMessage().split(":"))[1].trim());
            if (handler.onCompleteCallback != null)
                handler.onCompleteCallback.accept(handler);
        });
        return handler;
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
    public static ProcessHandler construct(String[] cmd) {
        final Process process;
        try {
            process = new ProcessBuilder(cmd).start();
        } catch (IOException e) {
            e.printStackTrace();
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
                writer.write(data);
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
}

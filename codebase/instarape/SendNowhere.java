package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SendNowhere {
    ServerSocket ss;
    Socket s;
    int serverBytesReceived = 0;
    int clientBytesReceived = 0;

    public void startServer() throws IOException {
            try (ServerSocket serverSocket = new ServerSocket(6000)) {
                System.out.println("Server listening on port " + serverSocket.getLocalPort());

                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Client connected from " + clientSocket.getInetAddress());

                    ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                    Packet receivedItem = (Packet) objectInputStream.readObject();
                    System.out.println("Received: " + receivedItem);

                } catch (ClassNotFoundException e) {
                    System.out.println("Class not found: " + e.getMessage());
                }
            } catch (IOException e) {
                System.out.println("Server exception: " + e.getMessage());
            }
    }


    public <T extends Serializable> void send(T obj) throws IOException, InterruptedException {
        // Small delay to ensure the server starts first
        try {
            Thread.sleep(2000);
            try (Socket socket = new Socket("127.0.0.1", 6000)) {
                System.out.println("Connected to server at localhost:6000");

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                objectOutputStream.writeObject(obj);
                System.out.println("Sent item to server: " + obj);

            } catch (IOException e) {
                System.out.println("Client error: " + e.getMessage());
            }
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted: " + e.getMessage());
        }
    }


}
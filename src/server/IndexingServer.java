/**
 * Rohan krishna Ramkhumar
 * Indexing server
 * Used for well...Registry and peer checkin
 */
package server;

import util.PeerQueue;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IndexingServer {

    private static Hashtable<String, ArrayList<Peer>> index;//Hash table to store locations/registry
    private static int port = 51740;//port number
    private static int numThreads = 4;
    private static PeerQueue<Socket> peerQueue;

    public static Hashtable<String, ArrayList<Peer>> getIndex() {
        return index;
    }

    /**
     * Method Server
     * Starts and creates the server socket. The Server class from this package (server) pulls from this
     *
     * @throws IOException
     */
    private static void server() throws IOException {

        @SuppressWarnings("resource")//Please suppress warnings, they are SO NOT REQUIRED
                ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            System.out.println("\nWaiting for peer...");//Listens for peers to connect if you need to/want to change topology
            Socket socket = serverSocket.accept();
            synchronized (peerQueue) {
                peerQueue.add(socket);
            }
        }
    }

    /**
     * income
     * Used to handle incoming connection threads
     *
     * @throws IOException
     */
    private static void income() throws IOException {//Peeks incoming peer connection and oh god stops the bloody threads

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (peerQueue.peek() == null)
                continue;
            synchronized (peerQueue) {
                Socket socket = peerQueue.poll();
                Server s = new Server(socket);
                executor.execute(s);
            }
        }
    }

    /**
     * Method Registry
     * Contains the location, directory address etc. All the stuff that's taken down by the peers it connects to so that it knows
     * That X peer hass the file
     *
     * @param peerId
     * @param numFiles
     * @param fileNames
     * @param directory
     * @param address
     * @param port
     */
    public static void registry(int peerId, int numFiles, ArrayList<String> fileNames, String directory, String address, int port) {
        for (String fileName : fileNames) {
            if (index.containsKey(fileName)) {
                index.get(fileName).add(new Peer(peerId, numFiles, fileNames, directory, address, port));
            } else {
                index.put(fileName, new ArrayList<Peer>());
                index.get(fileName).add(new Peer(peerId, numFiles, fileNames, directory, address, port));
            }

        }
    }

    /**
     * Valid port number check, Main method
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        index = new Hashtable<String, ArrayList<Peer>>();
        peerQueue = new PeerQueue<Socket>();
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (Exception e) {
                System.out.println("Put a valid port number");
            }
        }
/**
 * Server start thread
 */
        new Thread() {
            public void run() {
                try {
                    server();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
/**
 * File Download Thread
 */
        new Thread() {
            public void run() {
                try {
                    income();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

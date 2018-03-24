/**
 * Rohan krishna Ramkhumar
 * Literally all this does is try and get locations from the sockets on the client side
 */
package client;

import util.Util;

import java.io.*;
import java.net.Socket;

public class Server extends Thread {

    private String directory;
    private Socket socket;

    /**
     * Constructor Server
     * Assigns the socket and directory. Default Constructor
     *
     * @param socket
     * @param directory
     */
    public Server(Socket socket, String directory) {
        this.directory = directory;
        this.socket = socket;
    }

    /**
     * Runnable area that gets the file location and well the file download
     */
    public void run() {
        try {
            DataInputStream dIn = new DataInputStream(socket.getInputStream());
            String fileName = dIn.readUTF();
            InputStream in = new FileInputStream(directory + "/" + fileName);
            OutputStream out = socket.getOutputStream();
            Util.copy(in, out);
            dIn.close();
            out.close();
            in.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }
}

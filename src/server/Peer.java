/**
 * Rohan Krishna Ramkhumar
 * Peer class for initializing peers
 */
package server;

import java.util.ArrayList;

public class Peer {
    private int peerId;
    private int numFiles;
    private ArrayList<String> fileNames;
    private String directory;
    private String address;
    private int port;


    public Peer(int peerId, int numFiles, ArrayList<String> fileNames, String directory, String address, int port) {
        // TODO auto-generated Constructor stub
        this.peerId = peerId;
        this.numFiles = numFiles;
        this.fileNames = new ArrayList<String>();
        this.fileNames.addAll(fileNames);
        this.directory = directory;
        this.address = address;
        this.port = port;
    }

    /**
     * Getter and Setter methods: Names are self explanatory
     */

    public int getPeerId() {
        return peerId;
    }

    public String getDirectory() {
        return directory;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * File lookup Method
     *
     * @param fileName
     * @return if file exists
     */

    public Boolean searchFile(String fileName) {
        for (String fn : fileNames) {
            if (fn.equals(fileName)) {
                return true;
            }
        }
        return false;
    }
}

/**
 * Rohan Krishna Ramkhumar
 * Server class for indexing files from connected peers
 */
package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {

    private static int id = 0;

    private ArrayList<Peer> peerList;
    private Socket socket;

    /**
     * Method getUnique ID aka Peer ID
     *
     * @return UID
     */
    private int getUniqueId() {
        synchronized (this) {
            return ++id;
        }
    }

    public void newPeerList() {
        peerList = new ArrayList<Peer>();
    }//creates a new peerlist on every connection


    public Boolean addAllPeer(ArrayList<Peer> peers) {//Adds all peers
        peerList.addAll(peers);
        return true;
    }

    public Server(Socket socket) {
        this.socket = socket;
    }//Server Socket

    /**
     * Method search
     *
     * @param fileName
     * @return Peer number location of file
     */
    public Boolean search(String fileName) {
        newPeerList();
        return (IndexingServer.getIndex().containsKey(fileName))
                ? addAllPeer(IndexingServer.getIndex().get(fileName))//Check and return
                : false;
    }

    /**
     * File host running, this is where the Higher level magic happens
     */
    public void run() {

        try {

            DataInputStream dIn = new DataInputStream(socket.getInputStream());//File recieve
            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());//File send

            byte option = dIn.readByte();

            switch (option) {
                case 0:
                    int peerId = getUniqueId();//UID/ peer ID

                    Boolean end = false;
                    ArrayList<String> fileNames = new ArrayList<String>();
                    int numFiles = 0, port = 0;
                    String directory = null, address = null;

                    while (!end) {
                        byte messageType = dIn.readByte();

                        switch (messageType) {
                            case 1:
                                numFiles = dIn.readInt();
                                System.out.println("\nPeer " + peerId + " registering with " + numFiles + " files:");
                                break;
                            case 2:
                                for (int i = 0; i < numFiles; i++) {
                                    fileNames.add(dIn.readUTF());
                                    //System.out.println(fileNames.get(i));
                                }
                                break;
                            case 3:
                                directory = dIn.readUTF();//Reads file directory location
                                break;
                            case 4:
                                address = dIn.readUTF();//Reads Address
                                break;
                            case 5:
                                port = dIn.readInt();//reads port
                                break;
                            default:
                                end = true;
                        }
                    }

                    synchronized (this) {
                        IndexingServer.registry(peerId, numFiles, fileNames, directory, address, port);//The registry of files noted whenever the peer connects
                    }


                    dOut.writeInt(peerId);
                    dOut.flush();
                    socket.close();
                    break;
                case 1:
                    String fileName = dIn.readUTF();//Reads file name to be found
                    Boolean b = search(fileName);//Searching the file
                    //TODO: see if I can do this with wait and notify, or find a better time and took it out from the overall time
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (b) {
                        dOut.writeByte(1);
                        dOut.writeInt(peerList.size());//Neighbhor list. this code can be modified to have as many neighbhors as possible
                        dOut.flush();
                        for (Peer p : peerList) {
                            dOut.writeUTF(p.getAddress() + ":" + p.getPort() + ":" + p.getPeerId());//Where you're getting your files from
                            dOut.flush();
                        }
                    } else {
                        dOut.writeByte(0);
                        dOut.flush();
                    }
                    socket.close();
                    break;
                default:
                    System.out.println("Not an option");//This should never happen, ever. Please don't break my code

            }
        } catch (IOException ioe) {//Standard IOException as required
            ioe.printStackTrace();
        }

    }
}

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;

public class SongHandler implements Runnable {
    private static final int PORT = 1111;
    private static String messageIn;
    private static DatagramSocket datagramSocket;
    private static DatagramPacket outPacket;
    private InetAddress clientAddress;
    // int clientPort;
    private static byte[] buffer;

    public SongHandler(String mIn, InetAddress cAdd) {
        messageIn = mIn;
        try {
            clientAddress = cAdd;
            datagramSocket = new DatagramSocket(PORT);
        }
        catch(SocketException et) {
            et.printStackTrace();
        }
    }

    public void run() {
        JsonParser parser = new JsonParser();
        String songID = parser.parse(messageIn).getAsString();
        SongDispatcher songdispatcher = new SongDispatcher();
        long i = 0;
        long j = 0;
        int filesize = 0;
        try {
            filesize = songdispatcher.getFileSize(j);
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch (IOException et) {
            et.printStackTrace();
        }
            while(8192*i < filesize) {
            try
            {
                buffer = songdispatcher.getSongChunk(j, i).getBytes();
                outPacket = new DatagramPacket(buffer, buffer.length, clientAddress, PORT);
                datagramSocket.send(outPacket);
                System.out.print("Music fragment " +i+ " sent back");
            }
            catch(IOException ioEx)
            {
                ioEx.printStackTrace();
                break;
            }
            catch(Exception e) {
                e.printStackTrace();
                break;
            }
            i++;
        }
    }
}
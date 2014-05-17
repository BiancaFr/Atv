package drawsomethinggame;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Prof. Dr. Plinio Vilela -- vilela@ydoo.com.br
 */
public class SocketReceiver extends Observable implements Runnable {
    private ServerSocket myServerSocket;
    private Socket mySocket;
    private String myIp;
    private int myPort;
    boolean serverListening = false;

    public SocketReceiver(int myPort) {
        try {
            if (myServerSocket == null) {
                this.myPort = myPort;
                this.myServerSocket = new ServerSocket(this.myPort);
            }
            new Thread(this).start();

        } catch (IOException ex) {
            if (ex instanceof java.net.BindException) {
                return;
            }
            Logger.getLogger(SocketReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getMyIPAddress(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(SocketReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    @Override
    public void run() {
        DataInputStream input;
        BufferedReader br;
        String tag;
        String value;
        if (!serverListening) {
            while (true) {
                try {
                    mySocket = myServerSocket.accept(); //Este comando trava o sistema esperando vir algo!
                    serverListening = true;
                    new Thread(this).start();
                } catch (IOException ex) {
                    Logger.getLogger(SocketReceiver.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            try {
                input = new DataInputStream(mySocket.getInputStream());
                br = new BufferedReader(new InputStreamReader(input));
                while (true) {
                    tag = br.readLine();
                    value = br.readLine();
                    if (tag == null || value == null) {
                        return;
                    }
                    setChanged();
                    notifyObservers(tag + "\n" + value);
                }
            } catch (IOException ex) {
                Logger.getLogger(SocketReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }// run
}//class

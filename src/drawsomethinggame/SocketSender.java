package drawsomethinggame;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Prof. Dr. Plinio Vilela - plinio@ft.unicamp.br
 * 
 */
public class SocketSender {    
    private Socket socket;
    private PrintStream output;
    
    public SocketSender(String ip, int port){
        this.connect(ip, port);        
    }

    private boolean connect(String ip, int port) {
        try {
            socket = new Socket(ip, port);   
            output = new PrintStream(socket.getOutputStream());
            return true;
        } catch (UnknownHostException ex) {
            Logger.getLogger(SocketSender.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(SocketSender.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean sendMessage(String tag, String value) throws IOException {
        output.print(tag+ "\n" + value +"\n");
        return true;
    }
}

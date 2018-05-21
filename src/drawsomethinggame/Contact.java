package drawsomethinggame;

import java.io.IOException;

/**
 * @author Prof. Dr. Plinio Vilela - plinio@ft.unicamp.br
 * 
 */
public class Contact {
	public String name;
	public String ip;
	public int port;
        private SocketSender ss;
	public Contact(String name, String ip, int port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
                ss = new SocketSender(ip,port);
	}
        
        public void sendMessage(String tag, String value) throws IOException{
            ss.sendMessage(tag, value);
        }
}

package drawsomethinggame;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class ReceiveAndBroadcast implements Observer {

    ArrayList<Contact> vContacts;
    //SocketSender socketSender;

    public ReceiveAndBroadcast(int port) {
        vContacts = new ArrayList<Contact>();
        SocketReceiver sr = new SocketReceiver(port);
        sr.addObserver(this);
        System.out.println(sr.getMyIPAddress());
        //socketSender = new SocketSender();
    }

    @Override
    public void update(Observable o, Object arg) {
        Scanner in = new Scanner((String) arg);
        String tag = in.nextLine();
        //String tag = in.next();

        if (tag.equals("register")) {
            String name = in.next();
            String ip = in.next();
            String port = in.next();
            vContacts.add(new Contact(name, ip, Integer.parseInt(port)));
        } else {
            String value = in.nextLine();
            for (Contact c : vContacts) {
                try {
                    c.sendMessage(tag, value);
                } catch (Exception ex) {
                    vContacts.remove(c);
                }
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Reimondo
 */
public class Server extends UnicastRemoteObject implements Services {

    private ArrayList<Notifiable> clientList = null;
    private int noOfRegistredClients = 0;
    private int totNoOfRegistred = 0;

    public Server() throws RemoteException {
        super();
        this.clientList = new ArrayList<Notifiable>();
    }

    @Override
    public String getHelp() throws RemoteException {
        return "Type /help for help, /nick newnick to change your nick, /who to "
                + "se all users, /register to get messages from other users, /deregister to"
                + " block messages from other, /quit do disconnect (allways deregister first)";
    }

    @Override
    public String setNick(String newNick) throws RemoteException {
        refreshClientList();
        if (newNick.equals("/nick")) {
            return "Name invalid or taken";
        }
        String temp = newNick.replace("/nick ", "");
        temp = temp.replace(" ", "");

        /* First we need to se that the name is unique */
        for (int i = 0; i < noOfRegistredClients; i++) {
            System.out.println("Jämför inkommande med: " + clientList.get(i).getName());
            if (clientList.get(i).getName().equalsIgnoreCase(temp)) {
                System.out.println("Vafan är detta!?" + temp);
                return "Name invalid or taken";

            }
        }
        if (temp.length() == 0) {
            return "Name invalid or taken";
        }
        return temp;
    }

    @Override
    synchronized public String getUsers() throws RemoteException {
        refreshClientList();
        String user = "Users: ";
        for (int i = 0; i < noOfRegistredClients; i++) {
            if (i == 0) {
                user = user + clientList.get(i).getName();
            } else {
                user = user + ", " + clientList.get(i).getName();
            }
        }
        return user;
    }

    @Override
    synchronized public void putMessage(String msg) throws RemoteException {
        refreshClientList();
        for (Notifiable n : clientList) {
            n.printMessage(msg);
        }
    }

    @Override
    synchronized public void registerClient(Notifiable n) throws RemoteException {
        refreshClientList();
        clientList.add(n);
        this.noOfRegistredClients = noOfRegistredClients + 1;
        totNoOfRegistred = totNoOfRegistred + 1;
        n.setName("unnamed" + totNoOfRegistred);
    }

    @Override
    synchronized public void deRegisterClient(Notifiable n) throws RemoteException {
        refreshClientList();
        clientList.remove(n);
        this.noOfRegistredClients = noOfRegistredClients - 1;
    }

    @Override
    synchronized public boolean isRegistred(Notifiable e) throws RemoteException {
        refreshClientList();
        for (int i = 0; i < clientList.size(); i++) {
            if (clientList.get(i).equals(e)) {
                return true;
            }
        }
        return false;
    }

    synchronized private void refreshClientList() {
        for (int i = 0; i < noOfRegistredClients; i++) {
            try {
                if (clientList.get(i).getName().equals(null)) {
                }
            } catch (RemoteException ex) {
                clientList.remove(i);
                noOfRegistredClients = noOfRegistredClients - 1;
                i = i - 1;
            }
        }
    }

    public static void main(String args[]) {
        if (args.length == 0) {
            System.out.println("usage: java Server 2a");
            System.exit(0);
        }
        try {
            Server ser = new Server();
            Naming.rebind(args[0], ser);
            System.out.println("Server running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

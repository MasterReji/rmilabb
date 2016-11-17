/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.*;
import java.util.Scanner;

/**
 *
 * @author Reimondo
 */
public class Client extends UnicastRemoteObject implements Notifiable {

    private Services service;
    private String name;

    public Client(Services service) throws RemoteException {
        super();
        this.service = service;
    }

    @Override
    public void setName(String newName) throws RemoteException {
        this.name = newName;
    }

    @Override
    synchronized public String getName() throws RemoteException {
        return this.name;
    }

    @Override
    synchronized public void printMessage(String msg) throws RemoteException {
        System.out.println(msg);
    }

    public static void main(String args[]) {
        Client client;
        if (args.length != 1) {
            System.out.println("usage: java NotifiableImpl <host>");
            System.exit(0);
        }
        try {
            String url = "rmi://" + args[0] + "/2a";
            Services services = (Services) Naming.lookup(url);
            client = new Client(services);
            client.runClient();

        } catch (NotBoundException nbe) {
            System.out.println(nbe.toString());
        } catch (MalformedURLException | RemoteException e) {
        }
    }

    private void runClient() throws RemoteException {
        Scanner scan = new Scanner(System.in);
        String str;
        boolean running = true;
        try {
            while (running) {
                str = scan.nextLine();
                if (str.equals("/help")) {
                    System.out.println(service.getHelp());
                } else if (str.equals("/quit")) {
                    if (service.isRegistred(this)) {
                        System.out.println("unregister first!");
                    } else {
                        System.out.println("Disconnecting and terminating process..");
                        System.exit(0);
                    }
                } else if (str.equals("/who")) {
                    System.out.println(service.getUsers());
                } else if (str.equals("/register")) {
                    service.registerClient(this);
                } else if (str.equals("/unregister")) {
                    service.deRegisterClient(this);
                } else if (str.startsWith("/nick ")) {
                    if (service.setNick(str).equals("Name invalid or taken")) {
                        System.out.println("Name invalid or taken");
                        continue;
                    } else {
                        this.name = service.setNick(str);
                    }
                } else if (str.startsWith("/")) {
                    System.out.println("not a valid command");
                } else if (service.isRegistred(this)) {
                    service.putMessage(name + ": " + str);
                }
            }
        } finally {
            service.deRegisterClient(this);
        }
    }
}

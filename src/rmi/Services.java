/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.*;
import java.util.ArrayList;

/**
 *
 * @author Reimondo
 */
public interface Services extends Remote{


    public String getHelp() throws RemoteException;
    public String setNick(String newNick) throws RemoteException;
    public String getUsers() throws RemoteException;
    public void putMessage(String msg) throws RemoteException;

    public boolean isRegistred(Notifiable n) throws RemoteException;
    public void registerClient(Notifiable n) throws RemoteException;
    public void deRegisterClient(Notifiable n) throws RemoteException;

}

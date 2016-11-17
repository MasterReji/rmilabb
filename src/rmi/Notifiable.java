/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.*;
/**
 *
 * @author Reimondo
 */
public interface Notifiable extends Remote{

    public void printMessage(String msg) throws RemoteException;
    public String getName() throws RemoteException;
    public void setName(String newName) throws RemoteException;

}

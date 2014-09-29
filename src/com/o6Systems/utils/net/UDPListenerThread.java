/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.o6Systems.utils.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import java.net.InetSocketAddress;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fercis
 */
public class UDPListenerThread implements Runnable{
     public final int IN_BUFFER_LENGTH = 1024;
    
    DatagramSocket inSocket;
    byte[] inBuffer = new byte[IN_BUFFER_LENGTH];
    ArrayList<NetworkPacketObserver> observers;
    public UDPListenerThread(int port, boolean nonBlocking){
        try {
            inSocket = new DatagramSocket(null);
            inSocket.setBroadcast(true);
            inSocket.setReuseAddress(nonBlocking);
            InetSocketAddress socketAdd = new InetSocketAddress("127.0.0.1",port);
            inSocket.bind(socketAdd);
            
        } catch (SocketException ex) {
            Logger.getLogger(UDPListenerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        observers = new ArrayList<NetworkPacketObserver>();
        
    }
    
    public UDPListenerThread(int port){
        new UDPListenerThread(port,false);
    }
    
    public void addObserver(NetworkPacketObserver observerToBeAdded){
        observers.add(observerToBeAdded);
    }
    
    @Override
    public void run() {
        
        while(true)
        {
           DatagramPacket receivePacket = new DatagramPacket(inBuffer, inBuffer.length);
         try {
             inSocket.receive(receivePacket);
         } catch (IOException ex) {
             Logger.getLogger(UDPListenerThread.class.getName()).log(Level.SEVERE, null, ex);
         }

           String remoteHost = receivePacket.getAddress().toString();
             int remotePort = receivePacket.getPort();
             String protocol = "udp";
             int readLength = receivePacket.getLength();

             for(int i=0; i<observers.size();i++){
                 observers.get(i).onPacketReceived(remoteHost,remotePort,protocol,inBuffer,readLength);
             }

        } 
    }
    
   
}

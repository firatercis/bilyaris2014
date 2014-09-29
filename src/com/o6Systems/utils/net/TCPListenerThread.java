/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.o6Systems.utils.net;

import java.io.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fercis
 */
public class TCPListenerThread implements Runnable{
    
    public final int IN_BUFFER_LENGTH = 1024;
    
    ServerSocket inSocket;
    byte[] inBuffer;
    
    private ArrayList<NetworkPacketObserver> observers;
    
    
    public TCPListenerThread(int port){
        try {
            inSocket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(TCPListenerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        observers = new ArrayList<NetworkPacketObserver>();
        inBuffer = new byte[IN_BUFFER_LENGTH];
        
        // Test ile ilgili kisim TODO: kaldirilacak.
       // TCPClientTest tester = new TCPClientTest("11.61.5.213",port,4000);
       // new Thread(tester).start();
        
        
    }
    
    public void addObserver(NetworkPacketObserver observerToBeAdded){
        observers.add(observerToBeAdded);
    }
    
    public static void sendPacketTCP(byte[] bytes, String destinationAddress, int destinationPort){
        Socket clientSocket;
        try {
            clientSocket = new Socket(destinationAddress,destinationPort);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream()); 
            outToServer.write(bytes);
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(TCPListenerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    @Override
    public void run() {
  
        while(true){
            Socket connectionSocket;
            try {
                connectionSocket = inSocket.accept();
                InputStream inStream = connectionSocket.getInputStream();
           
                int readLength = inStream.read(inBuffer);

                String remoteHost = connectionSocket.getInetAddress().toString();
                int remotePort = connectionSocket.getPort();
                String protocol = "tcp";
                
                for(int i=0; i<observers.size();i++){
                    observers.get(i).onPacketReceived(remoteHost,remotePort,protocol,inBuffer,readLength);
                }
                
                
                // TODO: kaldirilacak.
                // Print the received bytes
                 System.out.println("Bytes Received");
                for(int i=0; i<readLength; i++){
                    System.out.printf("0x%02X",(byte)inBuffer[i]);
                }
               
            } catch (IOException ex) {
                Logger.getLogger(TCPListenerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
    
}

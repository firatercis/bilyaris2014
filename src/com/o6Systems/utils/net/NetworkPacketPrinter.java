/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.o6Systems.utils.net;

/**
 *
 * @author fercis
 */
public class NetworkPacketPrinter implements NetworkPacketObserver {

    @Override
    public void onPacketReceived(String remoteHost, int remotePort, String protocol, byte[] dataBytes, int dataLength) {
        
        System.out.println("Packet Received from: " + remoteHost);
         System.out.println("Port: " + remotePort);
        System.out.println("Length: " + dataLength);
        System.out.println("Protocol :" + protocol);
      
        for(int i=0; i<dataLength; i++){
            System.out.printf("0x%02X\r\n",(byte)dataBytes[i]);
        }
    }
    
}

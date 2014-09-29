package com.o6Systems.utils.net;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author fercis
 */
public interface NetworkPacketObserver {
    
    public void onPacketReceived(String remoteHost, int remotePort, String protocol,byte[] dataBytes,int dataLength);
    
}

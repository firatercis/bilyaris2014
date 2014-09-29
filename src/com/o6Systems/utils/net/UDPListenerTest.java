/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.o6Systems.utils.net;

import com.aselsan.util.net.NetworkPacketPrinter;
import com.aselsan.util.net.UDPListenerThread;

/**
 *
 * @author fercis
 */
public class UDPListenerTest {
    public static void main(String[] args){
        
        UDPListenerThread udpThread = new UDPListenerThread(42030,true);
        udpThread.addObserver(new NetworkPacketPrinter());
        udpThread.run();
    }
}

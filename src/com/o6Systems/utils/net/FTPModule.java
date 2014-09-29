/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.o6Systems.utils.net;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
import org.apache.commons.net.util.TrustManagerUtils;

/**
 *
 * @author fercis
 */
public class FTPModule {
    
     public static int SYSTEM_OK = 0;
     public static int ERROR_FTP = 1;
     
    private static FTPModule instance = null;
     
     public static FTPModule getInstance(){
         
         if(instance == null){
             instance = new FTPModule();
         }
         
         return instance;
     }
     
    private FTPModule(){
        
        
    }
    
    public int getFileFromFtp(String remoteHost,String userName, String password,String remoteFileName, String localFileName){
        
        String[] ftpCommand = new String[5];
            int iArgumentCounter = 0;
            
            
            boolean bAnonymous = false;
            
            if(userName == null){
                bAnonymous = true;
                ftpCommand[iArgumentCounter] = "-A";
                iArgumentCounter++;
            }
 
                
            ftpCommand[iArgumentCounter] = remoteHost;
            iArgumentCounter++;
            
            if(!bAnonymous){
                ftpCommand[iArgumentCounter] = userName;
                iArgumentCounter++;
                ftpCommand[iArgumentCounter] = password;
                iArgumentCounter++;
            }
            
            ftpCommand[iArgumentCounter] = remoteFileName;
            iArgumentCounter++;
            ftpCommand[iArgumentCounter] = localFileName;
            
            try{
                executeFtpCommand(ftpCommand);
            }
            catch(Exception e){
                return ERROR_FTP;
            }
            return SYSTEM_OK;
        
    }
    
    private int executeFtpCommand(String[] args) throws UnknownHostException
    {
        boolean storeFile = false, binaryTransfer = false, error = false, listFiles = false, listNames = false, hidden = false;
        boolean localActive = false, useEpsvWithIPv4 = false, feat = false, printHash = false;
        boolean mlst = false, mlsd = false;
        boolean lenient = false;
        long keepAliveTimeout = -1;
        int controlKeepAliveReplyTimeout = -1;
        int minParams = 5; // listings require 3 params
        String protocol = null; // SSL protocol
        String doCommand = null;
        String trustmgr = null;
        String proxyHost = null;
        int proxyPort = 80;
        String proxyUser = null;
        String proxyPassword = null;
        String username = null;
        String password = null;

        int base = 0;
        
        // Print the command
        
        System.out.println("----------FTP MODULE CALL----------");
        
        for(int i=0; i<args.length; i++){
            System.out.println(args[i]);
        }
        
        System.out.println("--------------------");
        //
        
        
        for (base = 0; base < args.length; base++)
        {
            if (args[base].equals("-s")) {
                storeFile = true;
            }
            else if (args[base].equals("-a")) {
                localActive = true;
            }
            else if (args[base].equals("-A")) {
                username = "anonymous";
                password = System.getProperty("user.name")+"@"+InetAddress.getLocalHost().getHostName();
            }
            else if (args[base].equals("-b")) {
                binaryTransfer = true;
            }
            else if (args[base].equals("-c")) {
                doCommand = args[++base];
                minParams = 3;
            }
            else if (args[base].equals("-d")) {
                mlsd = true;
                minParams = 3;
            }
            else if (args[base].equals("-e")) {
                useEpsvWithIPv4 = true;
            }
            else if (args[base].equals("-f")) {
                feat = true;
                minParams = 3;
            }
            else if (args[base].equals("-h")) {
                hidden = true;
            }
            else if (args[base].equals("-k")) {
                keepAliveTimeout = Long.parseLong(args[++base]);
            }
            else if (args[base].equals("-l")) {
                listFiles = true;
                minParams = 3;
            }
            else if (args[base].equals("-L")) {
                lenient = true;
            }
            else if (args[base].equals("-n")) {
                listNames = true;
                minParams = 3;
            }
            else if (args[base].equals("-p")) {
                protocol = args[++base];
            }
            else if (args[base].equals("-t")) {
                mlst = true;
                minParams = 3;
            }
            else if (args[base].equals("-w")) {
                controlKeepAliveReplyTimeout = Integer.parseInt(args[++base]);
            }
            else if (args[base].equals("-T")) {
                trustmgr = args[++base];
            }
            else if (args[base].equals("-PrH")) {
                proxyHost = args[++base];
                String parts[] = proxyHost.split(":");
                if (parts.length == 2){
                    proxyHost=parts[0];
                    proxyPort=Integer.parseInt(parts[1]);
                }
            }
            else if (args[base].equals("-PrU")) {
                proxyUser = args[++base];
            }
            else if (args[base].equals("-PrP")) {
                proxyPassword = args[++base];
            }
            else if (args[base].equals("-#")) {
                printHash = true;
            }
            else {
                break;
            }
        }

        int remain = args.length - base;
        if (username != null) {
            minParams -= 2;
        }
        

        String server = args[base++];
        int port = 0;
        String parts[] = server.split(":");
        if (parts.length == 2){
            server=parts[0];
            port=Integer.parseInt(parts[1]);
        }
        if (username == null) {
            username = args[base++];
            password = args[base++];
        }

        String remote = null;
        if (args.length - base > 0) {
            remote = args[base++];
        }

        String local = null;
        if (args.length - base > 0) {
            local = args[base++];
        }

        final FTPClient ftp;
        if (protocol == null ) {
            if(proxyHost !=null) {
                System.out.println("Using HTTP proxy server: " + proxyHost);
                ftp = new FTPHTTPClient(proxyHost, proxyPort, proxyUser, proxyPassword);
            }
            else {
                ftp = new FTPClient();
            }
        } else {
            FTPSClient ftps;
            if (protocol.equals("true")) {
                ftps = new FTPSClient(true);
            } else if (protocol.equals("false")) {
                ftps = new FTPSClient(false);
            } else {
                String prot[] = protocol.split(",");
                if (prot.length == 1) { // Just protocol
                    ftps = new FTPSClient(protocol);
                } else { // protocol,true|false
                    ftps = new FTPSClient(prot[0], Boolean.parseBoolean(prot[1]));
                }
            }
            ftp = ftps;
            if ("all".equals(trustmgr)) {
                ftps.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
            } else if ("valid".equals(trustmgr)) {
                ftps.setTrustManager(TrustManagerUtils.getValidateServerCertificateTrustManager());
            } else if ("none".equals(trustmgr)) {
                ftps.setTrustManager(null);
            }
        }

        if (printHash) {
            ftp.setCopyStreamListener(createListener());
        }
        if (keepAliveTimeout >= 0) {
            ftp.setControlKeepAliveTimeout(keepAliveTimeout);
        }
        if (controlKeepAliveReplyTimeout >= 0) {
            ftp.setControlKeepAliveReplyTimeout(controlKeepAliveReplyTimeout);
        }
        ftp.setListHiddenFiles(hidden);

        // suppress login details
        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));

        try
        {
            int reply;
            if (port > 0) {
                ftp.connect(server, port);
            } else {
                ftp.connect(server);
            }
            System.out.println("Connected to " + server + " on " + (port>0 ? port : ftp.getDefaultPort()));

            // After connection attempt, you should check the reply code to verify
            // success.
            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                return 1;
            }
        }
        catch (IOException e)
        {
            if (ftp.isConnected())
            {
                try
                {
                    ftp.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
            System.err.println("Could not connect to server.");
            e.printStackTrace();
            return 1;
        }

__main:
        try
        {
            if (!ftp.login(username, password))
            {
                ftp.logout();
                error = true;
                break __main;
            }

            System.out.println("Remote system is " + ftp.getSystemType());

            if (binaryTransfer) {
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
            } else {
                // in theory this should not be necessary as servers should default to ASCII
                // but they don't all do so - see NET-500
                ftp.setFileType(FTP.ASCII_FILE_TYPE);
            }

            // Use passive mode as default because most of us are
            // behind firewalls these days.
            if (localActive) {
                ftp.enterLocalActiveMode();
            } else {
                ftp.enterLocalPassiveMode();
            }

            ftp.setUseEPSVwithIPv4(useEpsvWithIPv4);

            if (storeFile)
            {
                InputStream input;

                input = new FileInputStream(local);

                ftp.storeFile(remote, input);

                input.close();
            }
            else if (listFiles)
            {
                if (lenient) {
                    FTPClientConfig config = new FTPClientConfig();
                    config.setLenientFutureDates(true);
                    ftp.configure(config );
                }

                for (FTPFile f : ftp.listFiles(remote)) {
                    System.out.println(f.getRawListing());
                    System.out.println(f.toFormattedString());
                }
            }
            else if (mlsd)
            {
                for (FTPFile f : ftp.mlistDir(remote)) {
                    System.out.println(f.getRawListing());
                    System.out.println(f.toFormattedString());
                }
            }
            else if (mlst)
            {
                FTPFile f = ftp.mlistFile(remote);
                if (f != null){
                    System.out.println(f.toFormattedString());
                }
            }
            else if (listNames)
            {
                for (String s : ftp.listNames(remote)) {
                    System.out.println(s);
                }
            }
            else if (feat)
            {
                // boolean feature check
                if (remote != null) { // See if the command is present
                    if (ftp.hasFeature(remote)) {
                        System.out.println("Has feature: "+remote);
                    } else {
                        if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                            System.out.println("FEAT "+remote+" was not detected");
                        } else {
                            System.out.println("Command failed: "+ftp.getReplyString());
                        }
                    }

                    // Strings feature check
                    String []features = ftp.featureValues(remote);
                    if (features != null) {
                        for(String f : features) {
                            System.out.println("FEAT "+remote+"="+f+".");
                        }
                    } else {
                        if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                            System.out.println("FEAT "+remote+" is not present");
                        } else {
                            System.out.println("Command failed: "+ftp.getReplyString());
                        }
                    }
                } else {
                    if (ftp.features()) {
//                        Command listener has already printed the output
                    } else {
                        System.out.println("Failed: "+ftp.getReplyString());
                    }
                }
            }
            else if (doCommand != null)
            {
                if (ftp.doCommand(doCommand, remote)) {
//                  Command listener has already printed the output
//                    for(String s : ftp.getReplyStrings()) {
//                        System.out.println(s);
//                    }
                } else {
                    System.out.println("Failed: "+ftp.getReplyString());
                }
            }
            else
            {
                OutputStream output;

                output = new FileOutputStream(local);

                ftp.retrieveFile(remote, output);

                output.close();
            }

            ftp.noop(); // check that control connection is working OK

            ftp.logout();
        }
        catch (FTPConnectionClosedException e)
        {
            error = true;
            System.err.println("Server closed connection.");
            e.printStackTrace();
        }
        catch (IOException e)
        {
            error = true;
            e.printStackTrace();
        }
        finally
        {
            if (ftp.isConnected())
            {
                try
                {
                    ftp.disconnect();
                }
                catch (IOException f)
                {
                    // do nothing
                }
            }
        }

        return error ? 1 : 0;
    } // end main

    private static CopyStreamListener createListener(){
        return new CopyStreamListener(){
            private long megsTotal = 0;
//            @Override
            public void bytesTransferred(CopyStreamEvent event) {
                bytesTransferred(event.getTotalBytesTransferred(), event.getBytesTransferred(), event.getStreamSize());
            }

//            @Override
            public void bytesTransferred(long totalBytesTransferred,
                    int bytesTransferred, long streamSize) {
                long megs = totalBytesTransferred / 1000000;
                for (long l = megsTotal; l < megs; l++) {
                    System.err.print("#");
                }
                megsTotal = megs;
            }
        };
    }
    
}

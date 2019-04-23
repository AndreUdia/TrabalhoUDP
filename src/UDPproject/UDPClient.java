/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UDPproject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author andre
 */
public class UDPClient {
    
    public static void main (String [] args){
        
        try {
            DatagramSocket ds = new DatagramSocket();
            
            int i = 8;
            byte[] b = String.valueOf(i).getBytes();
            InetAddress  ia = InetAddress.getLocalHost();
            DatagramPacket dp = new DatagramPacket(b,b.length,ia,9999);
            ds.send(dp);
            
            byte[] b1 = new byte[1024];
            DatagramPacket dp1 = new DatagramPacket(b1, b1.length);
            ds.receive(dp1);
            
            String str = new String(dp1.getData(),0,dp1.getLength());
            System.out.println("result is " + str);
        } catch (IOException ex) {
            System.out.println("ERRO: " + ex.getMessage());
        }
    }
}

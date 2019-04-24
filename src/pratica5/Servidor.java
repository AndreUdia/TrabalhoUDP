package pratica5;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor extends Thread{
    
    private static DatagramSocket s = null;
    private List<Integer> listaDePortas;
    private List<InetAddress> listaDeAddress;
    
    public Servidor(){
        try {
            s = new DatagramSocket(4001);
            listaDePortas = new ArrayList<>();
            listaDeAddress = new ArrayList<>();
        } catch (SocketException exception) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
    
    public static void main(String[] args)  {
        Servidor s = new Servidor();
        s.start();
    }
        
    @Override
    public void run(){
        try {
            DatagramSocket s = new DatagramSocket(4000);
            DatagramPacket recebe = new DatagramPacket(new byte[512],512);
            System.out.println("___Servidor ativo!_______________________");
            System.out.println("Servidor aguardando mensagem");
            
            while(true){
                s.receive(recebe);
                System.out.print("Mensagem recebida: ");
                System.out.println(recebe.getAddress());
                System.out.println(recebe.getPort());
                
                if(!listaDePortas.contains(recebe.getPort())) {
                    listaDePortas.add(recebe.getPort());
                    listaDeAddress.add(recebe.getAddress());
                }
                
                for (int i = 0; i < listaDePortas.size(); i++) {
                    if (listaDePortas.get(i) != recebe.getPort()) {
                        DatagramPacket resp = new DatagramPacket(recebe.getData(), recebe.getLength(), listaDeAddress.get(i), listaDePortas.get(i));
                        s.send(resp);
                    }
                }
                System.out.println();
                
                //String msgrecebida = "";
                //for(int i=0 ; i<recebe.getLength() ; i++)
                //    System.out.print((char)recebe.getData()[i]);
                //msgrecebida += (char)recebe.getData()[i];
                //System.out.println();
                //recebe.setAddress.getByAddress("10.10.80.0");
                //DatagramPacket resp = new DatagramPacket(recebe.getData(),recebe.getLength(),recebe.getAddress(),recebe.getPort());
                //DatagramPacket resp = new DatagramPacket(recebe.getData(),recebe.getLength(),recebe.getAddress(),recebe.getPort());
                //broadcast(msgrecebida,InetAddress.getByName("255.255.255.255"));
                //s.send(resp);
            }
        } catch (SocketException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    /*public static void broadcast( // aqui é criado um broadcast para enviar a mensagem para todos na rede
      String broadcastMessage, InetAddress address) throws IOException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);
 
        byte[] buffer = broadcastMessage.getBytes();
 
        DatagramPacket packet 
          = new DatagramPacket(buffer, buffer.length, address, 4545);
        socket.send(packet);
        System.out.println(buffer);
        socket.close();
        
    }
    
    // com esse método sou capaz de ver todos os ips conectados
    
    static List<InetAddress> listAllBroadcastAddresses() throws SocketException {
    List<InetAddress> broadcastList = new ArrayList<>();
    Enumeration<NetworkInterface> interfaces 
      = NetworkInterface.getNetworkInterfaces();
    while (interfaces.hasMoreElements()) {
        NetworkInterface networkInterface = interfaces.nextElement();
 
        if (networkInterface.isLoopback() || !networkInterface.isUp()) {
            continue;
        }
 
        networkInterface.getInterfaceAddresses().stream() 
          .map(a -> a.getBroadcast())
          .filter(Objects::nonNull)
          .forEach(broadcastList::add);
    }
    return broadcastList;
    } */   
}
package pratica5;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Cliente {
    
    private static DatagramSocket socket = null;
    
    public static void main(String[] args)  {
        try {
            broadcast("teste", InetAddress.getByName("192.168.1.255"));
        } catch (UnknownHostException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            DatagramSocket s = new DatagramSocket();
            InetAddress dest = InetAddress.getByName("localhost"); //getByAddress("127.0.0.1");
            String envio;
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("> ");
            envio = teclado.readLine();
            while(!envio.equalsIgnoreCase("")){
                byte[] buffer = envio.getBytes();
                DatagramPacket msg = new DatagramPacket(buffer, buffer.length, dest, 4545);
                s.send(msg);
                DatagramPacket resposta = new DatagramPacket(new byte[512],512);
                s.receive(resposta);
                String msgrecebida = "";
                for(int i=0 ; i<resposta.getLength() ; i++)
                    msgrecebida += (char)resposta.getData()[i];
                    //System.out.print((char)resposta.getData()[i]);
                System.out.println();
                System.out.println("> " + msgrecebida);
                
                for(InetAddress i : listAllBroadcastAddresses()){
                    System.out.println(msgrecebida);
                    System.out.println(i);
                    broadcast(msgrecebida,i);
                }
                broadcast(msgrecebida,InetAddress.getByName("192.168.1.255"));
                envio = teclado.readLine();
            }
            s.close();
        } catch (IOException ex) {System.out.println("ERRO: " + ex.getMessage());}
    }
    public static void broadcast( // aqui é criado um broadcast para enviar a mensagem para todos na rede
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
    }
    
    // Usando Multicast
    
    public class MulticastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];
 
    public void run() {
        try {
            socket = new MulticastSocket(4545);
            InetAddress group = InetAddress.getByName("230.0.0.0");
            socket.joinGroup(group);
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(
                        packet.getData(), 0, packet.getLength());
                if (" ".equals(received)) {
                    break;
                }
            }
            socket.leaveGroup(group);
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        }   
    }
    
    public class MulticastPublisher {
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;
 
    public void multicast(
      String multicastMessage) throws IOException {
        socket = new DatagramSocket();
        group = InetAddress.getByName("230.0.0.0");
        buf = multicastMessage.getBytes();
 
        DatagramPacket packet 
          = new DatagramPacket(buf, buf.length, group, 4545);
        socket.send(packet);
        socket.close();
        }
    }
    
}

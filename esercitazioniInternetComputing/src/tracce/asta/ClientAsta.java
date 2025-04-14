package tracce.asta;

import java.util.*;
import java.net.*;
import java.io.*;

public class ClientAsta {
    public static void main(String[] args) {
        try {
            String server_ip = InetAddress.getLocalHost().getHostAddress();
            TreeMap<String, Integer> aste = new TreeMap<>();
            new Thread(() -> {
                while (true) {
                    try{
                        MulticastSocket multicastSocket = new MulticastSocket(5000);
                        multicastSocket.joinGroup(InetAddress.getByName("230.0.0.1"));
                        byte[] buf = new byte[200];
                        DatagramPacket packet = new DatagramPacket(buf, buf.length);
                        System.out.println("Waiting for packets...");
                        multicastSocket.receive(packet);
                        System.out.println("Packet received: " + new String(packet.getData()));
                        String message = new String(packet.getData(), 0, packet.getLength());
                        StringTokenizer st = new StringTokenizer(message);
                        StringBuilder sb = new StringBuilder();
                        String infoAsta = sb.append(st.nextToken()).append(st.nextToken()).toString();
                        int portAsta = Integer.parseInt(st.nextToken());
                        aste.put(infoAsta, portAsta);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }).start();
            while(true){
                System.out.println("Menu: ");
                System.out.println("Scegliere un numero: ");
                System.out.println("1) Stampa aste disponibili");
                System.out.println("2) Unisciti ad un'asta");
                System.out.println("3) Esci.");
                Scanner sc = new Scanner(System.in);
                int choice = sc.nextInt();
                sc.nextLine();
                switch(choice){
                    case 1:
                        System.out.println(aste.toString());
                        break;
                    case 2:
                        for(int i = 0; i<aste.size(); i++) {
                            System.out.print(i + ") " + aste.keySet().toArray()[i] + " ");
                            System.out.print('\n');
                        }
                        System.out.println("Scegliere un numero: ");
                        int choice2 = sc.nextInt();
                        sc.nextLine();
                        if(choice2 < aste.size()){
                            String asteInfo = (String) aste.keySet().toArray()[choice2];
                            System.out.println(aste.get(asteInfo));
                            Socket socketOfferta = new Socket(server_ip, aste.get(asteInfo));
                            System.out.println("Quanto vuoi offrire?");
                            int amount = sc.nextInt();
                            sc.nextLine();
                            System.out.println("Scrivi il tuo codice fiscale: ");
                            String cf = sc.nextLine();
                            StringTokenizer st2 = new StringTokenizer(asteInfo);
                            Integer IDAsta = Integer.parseInt(st2.nextToken());
                            Offerta offerta = new Offerta(cf, IDAsta, amount);
                            PrintWriter out = new PrintWriter(socketOfferta.getOutputStream(), true);
                            out.println(offerta.toString());
                            BufferedReader in = new BufferedReader(new InputStreamReader(socketOfferta.getInputStream()));
                            boolean outcome = Boolean.parseBoolean(in.readLine());
                            if(outcome){
                                System.out.println("Offerta elaborata correttamente.");
                                new Thread(() -> {
                                    try {
                                        Socket outcomeSocket = new Socket(server_ip, 4000);
                                        BufferedReader in2 = new BufferedReader(new InputStreamReader(outcomeSocket.getInputStream()));
                                        System.out.println(in2.readLine());
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }).start();
                                break;
                            }else{
                                System.out.println("Qualcosa è andato storto.");
                                break;
                            }
                        }else{
                            System.out.println("Scelta non disponibile.");
                            break;
                        }
                    case 3:
                        return;
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}

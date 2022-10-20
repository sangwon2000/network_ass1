package assignment1;
import java.net.*;
import java.security.MessageDigest;
import java.util.Scanner;

public class Peer {
	
	
	static private int port;
	static private NetworkInterface NI;
	static private MulticastSocket socket;
	
	static private InetAddress multiAddress;
	static private String userName;
	static private Thread receiver;
	
	
	
	public static void main(String[] args) throws Exception {
		
		
		if(args.length == 0) {
			System.out.println("please input port.");
			return;
		}
		else {
			System.out.println("welcome to chat program!");
			
			port = Integer.parseInt(args[0]);
			NI = NetworkInterface.getByName("le0");
			socket = new MulticastSocket(port);
			
			multiAddress = null;
			userName = "command";
			receiver = new Thread(new Receiver(socket));
			receiver.start();
		}
		
		Scanner scan = new Scanner(System.in);
		while(true) {
			
			String input = scan.nextLine();
			
			if(input.charAt(0) == '#') {
				
				String[] words = input.split(" ");
				
				if(words.length == 3 && words[0].equalsIgnoreCase("#JOIN")) {
					if(multiAddress != null)
						System.out.println("already join chat room.");
					else join(words[1], words[2]);
				}
				
				else if(words.length == 1 && words[0].equalsIgnoreCase("#EXIT")) {
					if(multiAddress == null)
						System.out.println("already exit chat room.");
					else exit();
				}
				
				else System.out.println("wrong command.");
			}
			
			else if(multiAddress != null) send(userName + ": " + input);
			
			else System.out.println("you are not in chat room.");
			
		}
	
	}
	
//--------------------------------------------------------------------------------	
		
	private static InetAddress chatNameToAddress(String name) throws Exception {
		MessageDigest sh = MessageDigest.getInstance("SHA-512");
		sh.update(name.getBytes());
		byte[] byteData = sh.digest();
		
		int x = byteData[byteData.length-1] & 0xff;
		int y = byteData[byteData.length-2] & 0xff;
		int z = byteData[byteData.length-3] & 0xff;
		
		return InetAddress.getByName("225." + x + "." + y + "." + z); 
	}
	
	private static void join(String chatName, String userName) throws Exception {
		multiAddress = chatNameToAddress(chatName);
		Peer.userName = userName;
		socket.joinGroup(new InetSocketAddress(multiAddress,0), NI);
		
		System.out.println("*** join " + chatName + "(" + multiAddress.getHostAddress() + ") ***");
	}
	
	private static void exit() throws Exception {
		socket.leaveGroup(new InetSocketAddress(multiAddress,0), NI);
		userName = "command";
		multiAddress = null;
		
		System.out.println("*** exit ***");
		
	}
	
	private static void send(String msg) throws Exception {
		for(int i=1; i<1000; i++) {
			if(i == port) continue;
			
			byte[] msgByte = (msg).getBytes();
			DatagramPacket packet = new DatagramPacket(msgByte, msgByte.length, multiAddress, i);
			socket.send(packet);
		}
	}
}

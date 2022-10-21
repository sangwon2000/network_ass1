package assignment1;
import java.net.*;

public class Receiver implements Runnable{

	private MulticastSocket socket;
	
	public Receiver(MulticastSocket socket) {
		this.socket = socket;
	}
	
	
	public void run() {
		while(true) {
			
			try {
				
				byte[] msgByte = new byte[512];

				DatagramPacket packet = new DatagramPacket(msgByte, msgByte.length);
				socket.receive(packet);
				
				String msg = new String(msgByte);
				int nullIndex = msg.indexOf(0);
				if(nullIndex == -1) System.out.print(msg);
				else System.out.print(msg.substring(0,nullIndex)); 
				
			}
			catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			};
		}
	}
}

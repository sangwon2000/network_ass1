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
				byte[] msgByte = new byte[1024];
				DatagramPacket packet = new DatagramPacket(msgByte, msgByte.length);
				socket.receive(packet);
				
				String msg = new String(msgByte);
				System.out.println(msg);
			}
			catch(Exception e) {
			
			};
		}
	}
}

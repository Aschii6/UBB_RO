import java.net.*;
import java.io.*;


public class Client {

	private static final String SERVER_ADDRESS = "127.0.0.1";
	private static final int SERVER_PORT = 1235;
	private static final int UNSIGNED_SHORT_MAX_VALUE = 65535;
	private static final int UNSIGNED_SHORT_MIN_VALUE = 0;

	public static void main(String[] args) {
		Socket socket = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(System.in));
			socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
			int a = readUnsignedShort("n1 = ", reader);
			int b = readUnsignedShort("n2 = ", reader);

			int[] v1 = new int[50];
			int[] v2 = new int[50];

			for (int i = 0; i < a; i++){
				v1[i] = readUnsignedShort("Dati elem ptr primul sir: ", reader);
			}

			for (int j = 0; j < b; j++){
				v2[j] = readUnsignedShort("Dati elem ptr al doilea sir: ", reader);
			}

			writeIntegersToSocket(a, v1, b, v2, socket);
			readIntegersArrayFromSocket(socket);
		} catch (IOException e) {
			System.err.println("Cautgh exception " + e.getMessage());
		} finally {
			closeStreams(socket, reader);
		}
	}

	private static void readIntegersArrayFromSocket(Socket c) throws IOException {
		DataInputStream socketIn = new DataInputStream(c.getInputStream());
		int s = socketIn.readUnsignedShort();
		int[] v = new int[50];

		for (int i = 0 ; i < s; i++){
			v[i] = socketIn.readUnsignedShort();
			System.out.printf(v[i] + " ");
		}
	}

	private static void writeIntegersToSocket(int a, int[] v1, int b, int[] v2, Socket c) throws IOException {
		DataOutputStream socketOut = new DataOutputStream(c.getOutputStream());

		socketOut.writeShort(a);
		for (int i = 0; i < a; i++)
			socketOut.writeShort(v1[i]);

		socketOut.writeShort(b);
		for (int i = 0; i < b; i++)
			socketOut.writeShort(v2[i]);

		socketOut.flush();
	}

	private static int readUnsignedShort(String message, BufferedReader reader) throws IOException {
		int unsignedShortNumber = 0;
		System.out.print(message);
		try {
			unsignedShortNumber = Integer.parseInt(reader.readLine());
			if (unsignedShortNumber < UNSIGNED_SHORT_MIN_VALUE || unsignedShortNumber > UNSIGNED_SHORT_MAX_VALUE) {
				throw new IllegalArgumentException("The given number must be unsigned short [0..65535]!");
			}
		} catch (NumberFormatException e) {
			System.err.println("The given input is not an integer!");
		}
		return unsignedShortNumber;
	}

	private static void closeStreams(Socket socket, BufferedReader reader) {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				System.err.println("Could not close socket!");
			}
		}
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				System.err.println("Could not close reader!");
			}
		}
	}
}

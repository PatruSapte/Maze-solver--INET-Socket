import java.net.*;
import java.io.*;
import java.util.Arrays;

    // A client for our multithreaded EchoServer.
    public class client_java
    {
        public static void main(String[] args)
        {
            Socket socket = null;
            int PORT = 12345;

           // Create the socket connection to the EchoServer.
            try
            {
                socket = new Socket("localhost", PORT);
            }        
            catch(UnknownHostException uhe)
            {
                // Host unreachable
                System.out.println("Unknown Host");
                socket = null;
            }
            catch(IOException ioe)
            {
                // Cannot connect to port on given host
                System.out.println("Cant connect to server at 5000. Make sure it is running.");
                socket = null;
            }

            if(socket == null)
                System.exit(-1);
            try
            {
                File file = new File(args[0]);
                byte[] bytes = new byte[1];
              
                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                
                OutputStream out = socket.getOutputStream();
		PrintWriter dout = new PrintWriter(out, true);
		String name = args[0];
	//	System.out.println("Sending  " + name);
		dout.print(name);
		dout.flush();

                int count;
			
                while ((count = bis.read(bytes)) > 0) {
                    out.write(bytes, 0, count);
                }
                out.flush();
                out.close();
                fis.close();
                bis.close();
                
                InputStream ins = socket.getInputStream();
                byte[] buffer = new byte[1];
                
            }  
            catch(Exception ioe)
            {
                System.out.println("Exception during communication. Server probably closed connection.");
            }
            finally
            {
                try
                {
                    // Close the socket before quitting
                    socket.close();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }                
            }        
        }
    }

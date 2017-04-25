package com.wssong.server;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.AccessControlException;
import java.util.Random;

public class CoreSerivceServer {

    private static final String CATALINA_HOME = "catalina.home";

    private static int port = 9529;

    /**
     * The shutdown command string we are looking for.
     */
    private static String shutdown = "SHUTDOWN";

    @SuppressWarnings("unused")
	private ApplicationContext ctx;

    // private static String RMI_HOSTNAME = "java.rmi.server.hostname";
    // private static String RMI_IPADRESS = "192.168.1.7";

    public static void main(String[] args) {
        try{
            String command = "start";
            if (args.length > 0) {
                command = args[args.length - 2];
                port = Integer.parseInt(args[args.length - 1]);
            }
//            URL[] urls = new URL[]{new URL("file://../sample-service/target/classes")};
//            URLClassLoader loader = new URLClassLoader(urls,CoreSerivceServer.class.getClassLoader());
//            
            
            CoreSerivceServer dc = new CoreSerivceServer();
            
            if (command.equals("start")) {
                dc.start();
            } else if (command.equals("stop")) {
                dc.stop();
            }
        }catch(Exception e)
        {
            System.out.println("core service start fail.");
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("core service  Starting...");
        long t1 = System.nanoTime();
       // System.setProperty(CATALINA_HOME, System.getProperty("user.dir"));
        // System.setProperty(RMI_HOSTNAME, RMI_IPADRESS);
        ctx = new ClassPathXmlApplicationContext("classpath*:spring/*.xml");
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ctx  
                .getAutowireCapableBeanFactory(); 
        // Start Data center RMI service;
        // establish to Memcache Server;
        // establish to Database server;
        // System.out.println(System.getProperty("user.dir"));
        // System.setProperty(CATALINA_HOME, System.getProperty("user.dir"));
        long t2 = System.nanoTime();
        System.out.println("core service Server startup in " + ((t2 - t1) / 1000000) + " ms");
        await();
    }

    public void stop() {
        // ctx.
        stopServer();
    }

    public void stopServer() {
        // Stop the existing server
        try {
            String hostAddress = InetAddress.getByName("localhost").getHostAddress();
            Socket socket = new Socket(hostAddress, port);
            OutputStream stream = socket.getOutputStream();
            for (int i = 0; i < shutdown.length(); i++)
                stream.write(shutdown.charAt(i));
            stream.flush();
            stream.close();
            socket.close();

        } catch (IOException e) {
            System.out.println("core service Server.stop: " + e);
            System.exit(1);
        }

    }

    /**
     * Wait until a proper shutdown command is received, then return. This keeps
     * the main thread alive 
     */
    public void await() {

        // Set up a server socket to wait on
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("localhost"));
        } catch (IOException e) {
            System.out.println("core service Server.await: create[" + port + "]: " + e);
            System.exit(1);
        }
        System.out
                .println("taobobo: core service Server.await:Successfully create [port=" + port + "] to accept shutdown command!");
        // Loop waiting for a connection and a valid command
        while (true) {

            // Wait for the next connection
            Socket socket = null;
            InputStream stream = null;
            try {
                socket = serverSocket.accept();
                socket.setSoTimeout(10 * 1000); // Ten seconds
                stream = socket.getInputStream();
            } catch (AccessControlException ace) {
                System.out.println("core service Server.accept security exception: " + ace.getMessage() + "" + ace);
                continue;
            } catch (IOException e) {
                System.out.println("core service Server.await: accept: " + e);
                System.exit(1);
            }

            // Read a set of characters from the socket
            StringBuffer command = new StringBuffer();
            int expected = 1024; // Cut off to avoid DoS attack
            Random random = null;
            while (expected < shutdown.length()) {
                if (random == null)
                    random = new Random();
                expected += (random.nextInt() % 1024);
            }
            while (expected > 0) {
                int ch = -1;
                try {
                    ch = stream.read();
                } catch (IOException e) {
                    System.out.println("core service Server.await: read: " + e);
                    ch = -1;
                }
                if (ch < 32) // Control character or EOF terminates loop
                    break;
                command.append((char) ch);
                expected--;
            }

            // Close the socket now that we are done with it
            try {
                socket.close();
            } catch (IOException e) {
                ;
            }

            // Match against our command string
            boolean match = command.toString().equals(shutdown);
            if (match) {
                break;
            } else
                System.out.println("core service Server.await: Invalid command '" + command.toString() + "' received");

        }

        // Close the server socket and return
        try {
            System.out.println("core service Server shutdown");
            serverSocket.close();
            System.exit(1);
        } catch (IOException e) {
            ;
        }

    }

}

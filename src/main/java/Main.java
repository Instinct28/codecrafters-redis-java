import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

public class Main {
  public static void main(String[] args){
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    //  Uncomment the code below to pass the first stage
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int port = 6379;
        try {
          serverSocket = new ServerSocket(port);
          // Since the tester restarts your program quite often, setting SO_REUSEADDR
          // ensures that we don't run into 'Address already in use' errors
          serverSocket.setReuseAddress(true);
          // Wait for connection from client.

          while(true){
              clientSocket = serverSocket.accept();
              Socket finalClientSocket = clientSocket;
              CompletableFuture.runAsync(() -> {
                  try {
                      handleClient(finalClientSocket);
                  } catch (Exception e) {
                      throw new RuntimeException(e);
                  }
              });
          }




          //clientSocket.getOutputStream().write("+PONG\r\n".getBytes());


        } catch (IOException e) {
          System.out.println("IOException: " + e.getMessage());
        }
  }

  public static void handleClient(Socket clientSocket) {
      try (clientSocket) {
          InputStream inputStream = clientSocket.getInputStream();
          OutputStream outputStream = clientSocket.getOutputStream();

          Scanner sc = new Scanner(inputStream);
          while (sc.hasNextLine()) {
              String str = sc.nextLine();
              if (str.equalsIgnoreCase("ping")) {
                  outputStream.write("+PONG\r\n".getBytes());
                  outputStream.flush();
              }
          }
      } catch (IOException e) {
          System.out.println("Client error: " + e.getMessage());
      }
  }
}

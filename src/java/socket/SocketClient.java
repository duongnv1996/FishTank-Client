package socket;

import javafx.scene.image.ImageView;
import model.Fish;

import java.io.*;
import java.net.Socket;

/**
 * Created by MyPC on 2/13/2017.
 */
public class SocketClient {
    public void connectSocket(){
        try {
            final Socket socket = new Socket("localhost",8081);
            PrintStream printStream = new PrintStream(socket.getOutputStream());
            printStream.println("Hello I'm client");

            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        try {
//                            final ObjectInputStream objectInputStream  = new ObjectInputStream(socket.getInputStream());
//                            Fish msg =  ((Fish)objectInputStream.readObject());
                            String jsonFish = bufferedReader.readLine();

                            System.out.println("message from sever : " +jsonFish);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

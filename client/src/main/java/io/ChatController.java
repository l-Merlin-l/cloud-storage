package io;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;


public class ChatController implements Initializable {

    private InputStream is;
//    private OutputStream outputStream;
    private FileInputStream fileIS;
    private DataOutputStream dataOS;

    private byte [] buffer;
    @FXML
    private ListView<String> listView;

    @FXML
    private TextField textField;

    public void send(ActionEvent actionEvent) throws IOException {
        String msg = textField.getText();
        File file = new File(msg);
        if(file.exists()){

            System.out.println( "Отправляем файл -> " + file.getName());
            try {
                fileIS =new FileInputStream(file);
                int read;
            dataOS.writeUTF(file.getName());
            dataOS.writeLong(Files.size(Paths.get(msg)));
                while ((read = fileIS.read(buffer)) != -1) {
                    dataOS.write(buffer, 0 ,read);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            dataOS.flush();
            textField.clear();
        }else {
            System.out.println("Файл " +  file.getName() + " не найден");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buffer = new byte[256];
        try {
            Socket socket = new Socket("localhost", 8188);
            is = socket.getInputStream();
            dataOS = new DataOutputStream(socket.getOutputStream());

            Thread readThread = new Thread(() -> {

                try {
                    while (true) {
                        int read = is.read(buffer);
                        Platform.runLater(() -> {
                            listView.getItems().add(new String(buffer, 0, read));
                        });
                    }
                } catch (Exception e) {
                    System.out.println("Exception whole read");
                }
            });
            readThread.setDaemon(true);
            readThread.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

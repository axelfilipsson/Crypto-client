import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

//Klass för klientfönstret
public class ClientGui extends JFrame {

  static JTextField clientMessage;
  static JTextArea serverMessage;
  static JButton sendButton;

  //konstruktor som skapar och sätter layout för de olika grafiska komponenterna
  ClientGui(){
    super("CRYPTO client");
    clientMessage = new JTextField();
    serverMessage = new JTextArea();
    serverMessage.setEditable(false);
    clientMessage.addActionListener(
      new ActionListener(){
        public void actionPerformed(ActionEvent actionEvent){
          Client.writeMessage(actionEvent.getActionCommand());
          clientMessage.setText("");
        }
      }
    );
    add(clientMessage, BorderLayout.SOUTH);
    add(new JScrollPane(serverMessage), BorderLayout.CENTER);
    setSize(450,425);
    setVisible(true);
  }

  //Metod som förlänger serverMessage strängen med det passerade argumentet
  public static void showMessage(String message){
    SwingUtilities.invokeLater(
      new Runnable(){
        public void run(){
         serverMessage.append(message);
        }
      }
    );
  }
}

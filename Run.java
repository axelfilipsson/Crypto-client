import java.io.*;
import javax.swing.JFrame;

//Klass som startar  programmet. Skapar ett clientGui objekt samt kallar Clients
//createClient metod
public class Run {
  public static void main (String[]args) throws IOException{
  ClientGui clientGui = new ClientGui();
  clientGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  Client.createClient(args);
  }
}

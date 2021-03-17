import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;

//En klient som krypterar meddelanden innan avsändning och dekrypterar efter mottagning.
public class Client implements Runnable{

  static String hostName = "127.0.0.1";
  static int portNumber = 5000;
  static Socket socket;
  static SecretKey secretKey;
  static OutputStream out;
  static InputStream in;

  //Metod som sätter host och portnummer om angivet som argumentet
  //Startar en ny klient
  public static void createClient(String[] args) throws IOException {

    if (args.length == 2) {
        hostName = args[0];
        portNumber = Integer.parseInt(args[1]);
    } else if (args.length == 1){
        hostName = args[0];
    }
    socket = new Socket(hostName, portNumber);
    new Client();
  }

  //Konstruktor som skapar ett data in och ett data ut objekt med socketens in/utström
  //Skapar även en ny tråd och kallar på getSecretKey metoden
  public Client() {
    try {
      out = new DataOutputStream(socket.getOutputStream());
      in = new DataInputStream(socket.getInputStream());
    } catch(Exception e) {
    }
    new Thread(this).start();
    getSecretKey();
  }

  //En metod som förvarar input from System.in i en buffer och skickar med detta
  //till metoden encrypter som krypterar meddelandet och ger tillbaka en byte.
  //Efter det så passeras det krypterade meddelandet till sendMessage metoden.
  public static void writeMessage(String message){
    System.out.println("Message sent to server: " + message);
    ClientGui.showMessage("You: " + message + "\n" );
    try {
      byte[] encryptedData = encrypter(message, secretKey);
      sendMessage(encryptedData);
      String encryptedMessage = new String(encryptedData);
      System.out.println("Encrypted message sent to server : " + encryptedMessage);

    } catch(Exception e) {

    }
  }

  //Metod som använder sig av decryptermetoden för att dekryptera meddelandet och
  //skriver sedan ut meddelandet via printlin samt passerar det till showMessage metoden
  public static void readMessage(byte [] encryptedData){
    try{
      String decryptedMessage = decrypter(encryptedData, secretKey);
      System.out.println("Message recieved from server: " + decryptedMessage);
      ClientGui.showMessage("Someone: " + decryptedMessage + "\n" );
    } catch(Exception e) {
    }
  }

  //Skickar det kryppterade meddeleandet via socketens utström.
  public static void sendMessage(byte[] encryptedData){
    try{
      out.write(encryptedData);
    } catch (Exception e) {
        e.printStackTrace();
    }
  }

  //Runmetod som lyssnar på socketens inström, konverterar strömmen till en
  //byteArray och passerar sedan datan till readMessageMetoden.
  public void run() {
    try{
      while(true) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte buffer[] = new byte[1024];
        baos.write(buffer, 0 , in.read(buffer));
        byte [] encryptedData = baos.toByteArray();
        String res = new String(encryptedData);
        System.out.println("Encrypted message recieved from server : " + res);
        readMessage(encryptedData);
      }
    }
    catch (Exception e) {
      System.out.println(e);
      System.exit(1);
    }
  }

  //Metod som läser från filen secretKey som innehåller krypteringsnyckeln
  //och tilldelar secretKey variabeln detta värde för att sen stännga strömmen.
  public static void getSecretKey() {
      try {
          ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("secretKey"));
          secretKey = (SecretKey)objectInputStream.readObject();
          objectInputStream.close();
      }
      catch (Exception e){
      }
  }

    //Krypteringsmetod som tar ut strängmeddelandets bytes i UTF-8 format och krypterar
    //texten med hjälp av ett AES chifferobjekt. Sedan lämnar metoden tillbaka detta
    //kryppterade meddelande.
    public static byte[] encrypter(String message, SecretKey secretKey){
      try {
        Cipher cipher = Cipher.getInstance("AES");
        byte[] text = message.getBytes("UTF-8");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedMessage = cipher.doFinal(text);
        return encryptedMessage;
      } catch(Exception e) {
        e.printStackTrace();
          return null;
      }
    }

    //Dekrypteringsmetod som dekrypterar texten med hjälp av ett AES chifferobjekt
    //och för att sedan konvertera byten till en sträng som skickas tillbaka tillbaka
    // till den som kallat på decryptermetoden.
    public static String decrypter(byte[] message, SecretKey secretKey){
      try {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedMessage = cipher.doFinal(message);
        String result = new String(decryptedMessage);
        return result;
      } catch(Exception e) {
          return null;
      }
    }
}

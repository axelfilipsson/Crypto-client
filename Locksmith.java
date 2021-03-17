import javax.crypto.*;
import java.io.*;
import java.security.*;

//Klass som skapar en ny krypteringsnyckel vid behov.
public class Locksmith{

static SecretKey secretKey;

//Main metod som genererar en nyckel i AES format och tilldelar varaibeln SecretKey
//detta värde. Sedan skapas en fil med namnet secretKey via en utström som stängs
//efter att objektet har skrivits!
  public static void main(String[]args){
    try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
      secretKey = keyGenerator.generateKey();
      final ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("secretKey"));
      objectOutputStream.writeObject(secretKey);
      objectOutputStream.close();
      System.out.println("New secret key generated!");
    } catch(Exception e) {

    }
  }
}

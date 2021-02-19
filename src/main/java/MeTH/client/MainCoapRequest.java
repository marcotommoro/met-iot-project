package MeTH.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class MainCoapRequest {
    private static Logger logger = LoggerFactory.getLogger(MainCoapRequest.class);

    static final String USECASE = "GARAGE";

    public static void main(String[] args){
        CoapGetPostClient[] clients = new CoapGetPostClient[2];

        clients[0] = new CoapGetPostClient("light", USECASE);
        clients[1] = new CoapGetPostClient( "alarm", USECASE);

        Scanner scanner = new Scanner(System.in);
        int choice;

        System.out.println("Youre device are in "+ USECASE);

        while(true){
            System.out.print("Enter:\n1) Get light\n2) Get alarm\n3) Toogle light\n4) Toogle alarm\nChoice: ");

            if(scanner.hasNextInt()){
                choice = scanner.nextInt();
            }else{
                scanner.next();
                choice = -1;
            }

            try{
                switch (choice)
                {
                    case 1:
                    case 2:
                        clients[choice-1].request("GET");
                        break;
                    case 3:
                    case 4:
                        clients[((choice/2)-1)].request("POST");
                        break;
                    default:
                        logger.info("Allora sei un coglione, inserisci un numero valido per favore...");
                        break;
                }

            }catch (Exception e){}


        }


    }
}

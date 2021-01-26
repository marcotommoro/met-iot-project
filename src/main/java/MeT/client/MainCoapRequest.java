package MeT.client;

import org.eclipse.californium.core.CoapClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class MainCoapRequest {
    private static Logger logger =  LoggerFactory.getLogger(MainCoapRequest.class);
    public static void main(String[] args){
        CoapGetPostClient[] clients = new CoapGetPostClient[2];

        clients[0] = new CoapGetPostClient("light");
        clients[1] = new CoapGetPostClient( "alarm");

        Scanner keyboard = new Scanner(System.in);

        int choice;

        while(true){
            System.out.print("Enter:\n1) Get light\n2) Get contact dw\n3) Get light\n4) Get alarm\nChoice: ");
            choice = keyboard.nextInt();
            if (choice < 1 || choice > clients.length * 2){
                logger.info("Allora sei un coglione");
                continue;
            }
            try{
                switch (choice)
                {
                    case 1:
                    case 2:
                        clients[choice-1].request("GET");
                        logger.info("aaaa"+(choice));
                        break;
                    case 3:
                    case 4:
                        clients[((choice/2)-1)].request("POST");
                        logger.info("aaaa"+((choice/2)-1));
                        break;
                    default:
                        break;

                }

            }catch (Exception e){

            }


        }


    }
}

package cz.cvut.ear.flashcards.jms;


import cz.cvut.ear.flashcards.security.request.SignInRequest;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/** Login transaction reciever for JMS
 * @author Roman Filatov
 * @author Yevheniy Tomenchuk
*/
@Component
public class LoginTransactionReceiver {

    @JmsListener(destination = "LoginTransactionQueue", containerFactory = "myFactory")
    @SendTo("outbound.queue")
    public String receiveMessage(SignInRequest transaction) {
        System.out.println("Received <" + transaction + ">");
        return transaction.getUsername();
    }
}
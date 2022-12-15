import javax.jms.*;
import javax.naming.*;
import java.util.Properties;

public class SyncReceiver {
    public static void main(String[] args) {
        try{
            Context ctx = new InitialContext();
            QueueConnectionFactory qcf = (QueueConnectionFactory)ctx.lookup("MyConnectionFactory");
            QueueConnection con = qcf.createQueueConnection();
            QueueSession session = con.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);

            Queue queue = null;
            try{
                queue = (Queue)ctx.lookup("MyQueue");
            }catch(NameNotFoundException nnfe){
                queue = session.createQueue("MyQueue");
                ctx.bind("MyQueue", queue);
            }
            QueueReceiver receiver = session.createReceiver(queue);
            con.start();
            for(;;){
                TextMessage textMessage = (TextMessage)receiver.receive();
                System.out.println(textMessage.getText());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
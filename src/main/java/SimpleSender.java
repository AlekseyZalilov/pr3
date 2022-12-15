import javax.jms.*;
import javax.naming.*;
import java.util.Properties;

public class SimpleSender{
    public static void main(String args[]){
        try {
            Context ctx = new InitialContext();
            QueueConnectionFactory qcf = (QueueConnectionFactory)ctx.lookup("MyConnectionFactory");
            QueueConnection con = qcf.createQueueConnection();
            QueueSession session = con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = null;
            try{
                queue = (Queue)ctx.lookup("MyQueue");
            }catch(NameNotFoundException nnfe){
                queue = session.createQueue("MyQueue");
                ctx.bind("MyQueue",queue);
            }

            TextMessage textMessage = session.createTextMessage();
            QueueSender sender = session.createSender(queue);
            con.start();

            long sendInterval;
            try{
                sendInterval = Long.parseLong(args[0]);
            }catch(Exception rae){
                sendInterval = 1000;
            }

            for(int i=0;i<1000;i++){
                textMessage.setText(i + ". Привет!");
                sender.send(textMessage);
                try{
                    Thread.sleep(sendInterval);
                }catch(Exception se){
                    System.out.println("Sender ошибка "+se.getMessage());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
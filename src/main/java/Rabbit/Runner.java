package Rabbit;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sending message...");
        Runnable runnable =
                () -> { int i=1;
        while(true) {
	        rabbitTemplate.convertAndSend(Application.topicExchangeName, "", Integer.toString(i++));
	        
        }
       };
       Thread t = new Thread(runnable);
       t.start();
//       t.join();
        Runnable runnable2 =
                () -> { while(true) {
                	try {
						receiver.getLatch().await(10, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
                };
      Thread r = new Thread(runnable2);
      r.start();
      r.join();
      
    }

}
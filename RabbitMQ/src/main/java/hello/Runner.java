package hello;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

//Implementa un CommandLineRunner. Esto se ejecutara cuando se cargue el contexto Spring
@Component
public class Runner implements CommandLineRunner {

    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;
    private final ConfigurableApplicationContext context;

	//spring-boot-starter-amqp inyecta en el contexto un RabbitTemplate
    //Hace referencia al receptor que hemos cargado con el Bean
    //Hace referencia al contexto Spring
    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate,
            ConfigurableApplicationContext context) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
        this.context = context;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sending message...");
        //Usa como routing key Application.queueName. Envia el mensaje "Hello from RabbitMQ!"
        rabbitTemplate.convertAndSend(Application.queueName, "Hello from RabbitMQ!");

        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        context.close();
    }

}

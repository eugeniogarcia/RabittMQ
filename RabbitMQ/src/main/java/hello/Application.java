package hello;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    final static String queueName = "spring-boot";

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
    	//Crea un topic exchange
        return new TopicExchange("spring-boot-exchange");
    }

    //Establecemos el binding entre la cola y el exchange y definimos un routing
    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
    	//Vincula la cola con el exchange usando el routing key queueName
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }

    //configuramos la escucha de mensajes que llegan a una cola
    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        
        //Especifica la CF
        container.setConnectionFactory(connectionFactory);
        
        //Especifica el cola por la que escucharemos
        container.setQueueNames(queueName);
        
        //Especifica el listener
        container.setMessageListener(listenerAdapter);
        return container;
    }

	//Configura un listener, indicando un objeto y el metodo que se encargara de procesar los mensajes
    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Application.class, args);
    }

}

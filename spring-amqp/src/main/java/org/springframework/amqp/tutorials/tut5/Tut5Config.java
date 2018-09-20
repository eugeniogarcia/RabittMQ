package org.springframework.amqp.tutorials.tut5;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Profile({"tut5","topics"})
@Configuration
public class Tut5Config {

	//Exchange de tipo topic. Notese como el key es una secuencia de keywords separados por puntos
	@Bean
	public TopicExchange topic() {
		return new TopicExchange("tut.topic");
	}

	@Profile("receiver")
	private static class ReceiverConfig {

		@Bean
		public Tut5Receiver receiver() {
	 	 	return new Tut5Receiver();
		}

		@Bean
		public Queue autoDeleteQueue1() {
			return new AnonymousQueue();
		}

		@Bean
		public Queue autoDeleteQueue2() {
			return new AnonymousQueue();
		}

		//Hace un bind del exchange con la cola, y los keywords, usando * (se sustituye por una palabra)
		//o #, se sustituye por cero o mas palabras
		@Bean
		public Binding binding1a(TopicExchange topic, Queue autoDeleteQueue1) {
			return BindingBuilder.bind(autoDeleteQueue1).to(topic).with("*.orange.*");
		}

		@Bean
		public Binding binding1b(TopicExchange topic, Queue autoDeleteQueue1) {
			return BindingBuilder.bind(autoDeleteQueue1).to(topic).with("*.*.rabbit");
		}

		@Bean
		public Binding binding2a(TopicExchange topic, Queue autoDeleteQueue2) {
			return BindingBuilder.bind(autoDeleteQueue2).to(topic).with("lazy.#");
		}

	}

	@Profile("sender")
	@Bean
	public Tut5Sender sender() {
		return new Tut5Sender();
	}

}

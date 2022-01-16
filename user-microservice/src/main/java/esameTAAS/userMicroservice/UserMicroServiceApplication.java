package esameTAAS.userMicroservice;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

@EnableDiscoveryClient
@SpringBootApplication
public class UserMicroServiceApplication {
    @Qualifier("eurekaClient")
    @Autowired
    @Lazy
    private EurekaClient eurekaClient;

    public static void main(String[] args) {
        SpringApplication.run(UserMicroServiceApplication.class, args);
    }

    public static final String topicExchangeName = "token-exchange";

    public static final String queueName = "token-exchange";

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("token-exchange");
    }
}



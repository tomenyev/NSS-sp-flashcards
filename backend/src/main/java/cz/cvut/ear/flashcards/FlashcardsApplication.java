package cz.cvut.ear.flashcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.jms.ConnectionFactory;

/**
 * app start
 */
@SpringBootApplication
@EnableCaching
//@EnableJms
public class FlashcardsApplication  {

    /**
     * main
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(FlashcardsApplication.class, args);
    }

    /**
     * web mvc static config
     * @return
     */
    @Bean
    WebMvcConfigurer WebMvcConfigurer() {
        return new WebMvcConfigurerAdapter() {
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/static/**")
                        .addResourceLocations("classpath:/static/");
            }
        };
    }

    /**
     * jms config
     * @return
     */
    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("id");
        return converter;
    }


    /**
     * jms config
     * @param connectionFactory
     * @param configurer
     * @return
     */
    @Bean
    public JmsListenerContainerFactory<?> myFactory(
            ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

        // lambda function
        factory.setErrorHandler(t -> System.err.println("An error has occurred in the transaction"));
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}

package com.cachito.web_socket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.ip.config.TcpConnectionFactoryFactoryBean;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.tcp.connection.AbstractServerConnectionFactory;
import org.springframework.integration.ip.tcp.serializer.TcpCodecs;

@SpringBootApplication
public class WebSocketApplication {

  @Bean
  public TcpConnectionFactoryFactoryBean privateFeedServer() {
    TcpConnectionFactoryFactoryBean fb = new TcpConnectionFactoryFactoryBean("server");
    fb.setPort(1234);
    fb.setSerializer(TcpCodecs.lengthHeader1());
    fb.setDeserializer(TcpCodecs.crlf());
    return fb;
  }

  @Bean
  public IntegrationFlow flowIn(AbstractServerConnectionFactory cf) {
    return IntegrationFlow.from(Tcp.inboundAdapter(cf))
        .transform(Transformers.objectToString())
        .handle(System.out::println)
        .get();
  }

  public static void main(String[] args) {
    SpringApplication.run(WebSocketApplication.class, args);
  }
}

package com.telcoware.omp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
//@EnableAutoConfiguration(exclude=WebSocketConfig.class)
// spring-integration-core 를 사용시 "More than one TaskScheduler exists within the context." 발생
// 명시적으로 스케줄 테스크를 처리하는 방법으로 해결을 한다.
//@EnableScheduling // schalue이  websocket module에서도 사용을 함으로 발생한다.

@EnableWebSocketMessageBroker
//@ImportResource("classpath:/tcpClientServerDemo-context.xml")
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// TODO Auto-generated method stub
		//super.configureMessageBroker(registry);
		
		config.enableSimpleBroker("/queue", "/topic");
		config.setApplicationDestinationPrefixes("/app");
	}
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/omp").withSockJS();
	}
	
}

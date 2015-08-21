package com.telcoware.omp.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.telcoware.omp.OccmRecvQueue;

import netty.omp.client.ClientTaskExecutor;

@Configuration
//@Import({ SchedulingConfig.class })
public class ClientConfig {
	static final Logger logger = Logger.getLogger(ClientConfig.class);
	
//	@Bean 
//	public WebSocketConfig webSocketConfig(){
//		return new WebSocketConfig();
//	}
	
	@Bean
	public OccmRecvQueue occmRecvQueue() {
		return new OccmRecvQueue();
	}
	
	@Bean
	// 순환 구조로 controller에서 사용 BEAN을 해당 BEAN에서 사용 함으로 이슈가 발생
	//@DependsOn("SimpMessagingTemplate")
	//@DependsOn("OccmRecvQueue")
	public ClientTaskExecutor echoClient(){
		
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// to customize with your requirements
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(40);
		executor.setQueueCapacity(100);
		executor.initialize();
		
		ClientTaskExecutor client = new ClientTaskExecutor(executor, occmRecvQueue());
		//client.runClient();

		return client;
	}
	
	@Bean
	protected ServletContextListener listener(){
		return new ServletContextListener() {
			@Override
			public void contextInitialized(ServletContextEvent sce) {
                logger.info("ServletContext initialized");
                
                
            }

			@Override
			public void contextDestroyed(ServletContextEvent arg0) {
				logger.info("ServletContext destroyed");
			}
		};
	}
	
	@Bean
	protected ApplicationListener<ApplicationEvent> appListener(){
		return new ApplicationListener<ApplicationEvent>() {

			@Override
			public void onApplicationEvent(ApplicationEvent arg0) {
				// TODO Auto-generated method stub
				
				logger.info("onApplicationEvent(ApplicationEvent arg0)");
				logger.info(arg0);
				logger.info(arg0.getClass().getName());
				if(arg0.getClass().getName().equals("org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent") )
				{
					// 해당 evet의 성공 여부를 확인하고 구동 처리 한다.
					logger.info("================== call ==============================");
					echoClient().runClient();
				}
				
			}

		};
	}

}

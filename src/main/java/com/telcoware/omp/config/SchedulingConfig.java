package com.telcoware.omp.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {
 
// @Bean
// public Demon demon() {
//  return new Demon();
// }
 
 @Override
 public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
  // TODO Auto-generated method stub
  taskRegistrar.setScheduler(taskExecutor());
 }
 
 private Executor taskExecutor() {
  // TODO Auto-generated method stub
  return Executors.newScheduledThreadPool(10);
 }
 
}
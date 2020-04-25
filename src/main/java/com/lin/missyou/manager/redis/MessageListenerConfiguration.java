package com.lin.missyou.manager.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;

@Configuration
public class MessageListenerConfiguration {

    @Value("${spring.redis.listen-pattern}")
    public String pattern;

    @Bean
    public MessageListener getTopicMessageListener(){
        return new TopicMessageListener();
    }

    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory redisConnection){
        //container负责连接redis服务器 并且将之前写的listener绑定到监听里
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnection);	//redisConnection取到redis的连接信息
        Topic topic = new PatternTopic(pattern);    //监听主题初始化
        container.addMessageListener(this.getTopicMessageListener(),topic); //添加监听器和所监听得主题
        return container;
    }
}

package com.suke.wxk.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //  @Configuration 注解，必须加上才能被 Spring 扫描
public class RabbitConfig {
    // 业务队列：投票记录队列（支持高并发）
    public static final String VOTE_RECORD_QUEUE = "vote.record.batch.queue";
    // 死信队列：处理失败的投票记录
    public static final String DEAD_LETTER_QUEUE = "vote.record.dead.queue";
    // 业务交换机
    public static final String VOTE_EXCHANGE = "vote.exchange";
    // 死信交换机（新增）
    public static final String DEAD_LETTER_EXCHANGE = "dead.letter.exchange";
    // 死信路由键（新增，需与业务队列的 x-dead-letter-routing-key 一致）
    public static final String DEAD_LETTER_ROUTING_KEY = "dead.letter.key";

    public static final String VOTE_RECORD_ROUTING_KEY = "vote.record.routing.key";

    // 配置消息转换器：用Jackson2JsonMessageConverter（支持JSON序列化，更安全）
    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        // 配置允许反序列化的类
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        // 添加VoteRecord到白名单（完整类名）
        typeMapper.addTrustedPackages("com.suke.wxk.entity.VoteRecord");
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    // 将自定义转换器应用到监听器容器
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // 使用上面定义的JSON转换器
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    /**
     * 业务队列：配置死信参数（死信交换机、死信路由键）
     */
    @Bean
    public Queue voteRecordQueue() {
        return QueueBuilder.durable(VOTE_RECORD_QUEUE)
                // 当消息成为死信时，转发到死信交换机
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE)
                // 死信转发的路由键
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
                // 可选：消息过期时间（毫秒），超过此时间未消费则成为死信
                // .withArgument("x-message-ttl", 60000)
                // 可选：队列最大长度，超过则最早的消息成为死信
                // .withArgument("x-max-length", 1000)
                .build();
    }

    /**
     * 死信队列：接收业务队列转发的死信消息
     */
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    /**
     * 业务交换机：处理正常业务消息
     */
    @Bean
    public DirectExchange voteExchange() {
        return new DirectExchange(VOTE_EXCHANGE);
    }

    /**
     * 死信交换机：专门处理死信消息的转发（新增）
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    /**
     * 绑定业务队列和业务交换机
     */
    @Bean
    public Binding bindingVoteRecord() {
        return BindingBuilder.bind(voteRecordQueue())
                .to(voteExchange())
                .with(VOTE_RECORD_ROUTING_KEY); // 业务路由键
    }

    /**
     * 绑定死信队列和死信交换机（新增）
     */
    @Bean
    public Binding bindingDeadLetter() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DEAD_LETTER_ROUTING_KEY); // 死信路由键（需与业务队列配置一致）
    }
}
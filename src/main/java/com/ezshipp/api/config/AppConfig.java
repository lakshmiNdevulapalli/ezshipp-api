package com.ezshipp.api.config;

//@Configuration
//@EnableCaching
//@ComponentScan("com.ezshipp.api")
//@PropertySource("classpath:/redis.properties")
public class AppConfig {

//    private @Value("${redis.host}") String redisHost;
//    private @Value("${redis.port}") int redisPort;
//
//    @Bean
//    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
//        return new PropertySourcesPlaceholderConfigurer();
//    }
//
//    @Bean
//    JedisConnectionFactory jedisConnectionFactory() {
//        JedisConnectionFactory factory = new JedisConnectionFactory();
//        factory.setHostName(redisHost);
//        factory.setPort(redisPort);
//        factory.setUsePool(true);
//        return factory;
//    }
//
//    @Bean
//    RedisTemplate<Object, Object> redisTemplate() {
//        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(jedisConnectionFactory());
//        return redisTemplate;
//    }
//
//    @Bean
//    CacheManager cacheManager() {
//        return new RedisCacheManager(redisTemplate());
//    }
}

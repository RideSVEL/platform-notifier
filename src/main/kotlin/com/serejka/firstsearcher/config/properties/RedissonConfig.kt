package com.serejka.firstsearcher.config.properties

import lombok.Data
import org.apache.commons.lang3.StringUtils
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Data
@Configuration
class RedissonConfig(val redissonProperties: RedissonProperties) {
    private val database = 0

    @Bean(name = ["redissonClient"])
    fun redissonSingleClient(): RedissonClient {
        return Redisson.create(prepareSingleRedissonConfig())
    }

    private fun prepareSingleRedissonConfig(): Config {
        val config = Config()
        val singleServerConfig = config.useSingleServer()
        singleServerConfig.setAddress(redissonProperties.url)
        singleServerConfig.setDatabase(database)
        if (StringUtils.isNoneBlank(redissonProperties.password)) {
            singleServerConfig.setPassword(redissonProperties.password)
        }
        return config
    }
}

package ru.vgtu.diplom.orchestrator.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import ru.vgtu.diplom.common.webclient.MdcWebClientFactory

@Configuration
class WebClientConfig {
    @Bean
    fun webClient(): WebClient =
        MdcWebClientFactory.DEFAULT_INSTANCE
}
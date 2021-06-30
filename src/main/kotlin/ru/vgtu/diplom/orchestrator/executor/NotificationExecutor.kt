package ru.vgtu.diplom.orchestrator.executor

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import ru.vgtu.diplom.app.extensions.ApplicationStatus
import ru.vgtu.diplom.app.extensions.ApplicationStatus.*
import ru.vgtu.diplom.common.logging.Loggable

@Component
class NotificationExecutor(
    private val webClient: WebClient,
    @Value("\${service.mock.base-url}") private val mockUrl: String,
) : JavaDelegate {

    companion object : Loggable

    override fun execute(execution: DelegateExecution) {
        val isReject = execution.getVariable("isReject") as Boolean?
        val decision = execution.getVariable("decision") as String?
        when {
            decision != null -> {
                when (ApplicationStatus.valueOf(decision)) {
                    APPROVE -> notifyClient("Вам одобрен автокредит")
                    PRE_APPROVE -> notifyClient("Вам предварительно одобрен автокредит")
                    DECLINE -> notifyClient("Вам было отказано в получении автокредита")
                    else -> return
                }
            }
            isReject != null -> {
                notifyClient("Ваша заявка отклонена, подробнее ничего не скажем")
            }
            else -> {
                logger.error("client notification failed, no one variable exists")
            }
        }

    }

    fun notifyClient(text: String) =
        webClient.sendSms()
            .body(Mono.just(text), String::class.java)
            .retrieveUnitResponseWithSubscribe()

    fun WebClient.sendSms() =
        webClient.post()
            .uri("$mockUrl/sendSms")

    fun WebClient.RequestHeadersSpec<*>.retrieveUnitResponseWithSubscribe() =
        retrieve()
            .bodyToMono(Unit::class.java)
            .subscribe()


}
package ru.vgtu.diplom.orchestrator.executor.initiateApplication

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.slf4j.MDCContext
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.slf4j.MDC
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import ru.vgtu.diplom.orchestrator.dto.ClientData
import ru.vgtu.diplom.orchestrator.dto.ClientIncomes
import ru.vgtu.diplom.orchestrator.mapper.AllMapper
import ru.vgtu.diplom.orchestrator.service.ApplicationService

@Component
class ClientProfileExecutor(
    private val webClient: WebClient,
    @Value("\${service.mock.base-url}") private val mockUrl: String,
    private val allMapper: AllMapper,
    private val applicationService: ApplicationService
) : JavaDelegate {
    override fun execute(execution: DelegateExecution) =
        runBlocking(MDCContext(MDC.getCopyOfContextMap())) {
            val clientId = execution.getVariable("clientId") as String
            val clientIncomes = getClientIncome(clientId)
            val clientData = getClientData(clientId)

            with(allMapper) {
                val profile = clientData.toProfile()
                profile.incomes = clientIncomes.incomes
                profile.employments = clientIncomes.employments

                applicationService.updateProfile(execution.processBusinessKey, profile)
            }
        }


    private suspend fun getClientIncome(clientId: String) =
        webClient.get()
            .uri("$mockUrl/client/$clientId/income")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(ClientIncomes::class.java)
            .awaitFirstOrNull() ?: throw Exception()

    private suspend fun getClientData(clientId: String) =
        webClient.get()
            .uri("$mockUrl/client/$clientId/data")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(ClientData::class.java)
            .awaitFirstOrNull() ?: throw Exception()

}
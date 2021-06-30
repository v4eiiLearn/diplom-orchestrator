package ru.vgtu.diplom.orchestrator.executor.initiateApplication

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component
import ru.vgtu.diplom.orchestrator.dto.ClientInfo
import ru.vgtu.diplom.orchestrator.mapper.AllMapper
import ru.vgtu.diplom.orchestrator.service.ApplicationService

@Component
class ClientInfoExecutor(
    private val applicationService: ApplicationService,
    private val allMapper: AllMapper
) : JavaDelegate {
    override fun execute(execution: DelegateExecution) {
        val clientInfo = execution.getVariable("clientInfo") as ClientInfo
        with(allMapper) {
            val applicationInfo = applicationService.createApplication(clientInfo.toApplication())
            execution.setVariable(
                "applicationId",
                applicationInfo.applicationId
            )
            execution.processBusinessKey = applicationInfo.applicationId
        }
    }
}
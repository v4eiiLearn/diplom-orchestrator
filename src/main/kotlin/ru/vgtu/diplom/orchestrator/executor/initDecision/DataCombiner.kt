package ru.vgtu.diplom.orchestrator.executor.initDecision

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component
import ru.vgtu.diplom.orchestrator.dto.ProcessVariable.*
import ru.vgtu.diplom.orchestrator.executor.fillMdc
import ru.vgtu.diplom.orchestrator.service.ApplicationService

@Component
class DataCombiner(
    private val applicationService: ApplicationService
) : JavaDelegate {
    override fun execute(execution: DelegateExecution) {
        execution.fillMdc()
        val application = applicationService.getApplication(execution.businessKey)
        application.isBki = execution.getVariable(BKI_FLAG.value) as Boolean
        application.isPassportValid = execution.getVariable(PASSPORT_FLAG.value) as Boolean
        application.isIncomeValid = execution.getVariable(INCOME_FLAG.value) as Boolean
        application.isClientValid = execution.getVariable(CLIENT_FLAG.value) as Boolean
        application.isSolvency = execution.getVariable(SOLVENCY_FLAG.value) as Boolean
        application.isUnderwriting = execution.getVariable(UNDERWRITING_FLAG.value) as Boolean

        applicationService.updateApplication(application)
    }
}

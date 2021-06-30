package ru.vgtu.diplom.orchestrator.executor.initDecision

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.JavaDelegate
import org.springframework.stereotype.Component
import ru.vgtu.diplom.app.model.Application
import ru.vgtu.diplom.orchestrator.dto.ProcessVariable.*
import ru.vgtu.diplom.orchestrator.service.ApplicationService

@Component
class DataPlaceholder(
    private val applicationService: ApplicationService
) : JavaDelegate {
    override fun execute(execution: DelegateExecution) {
        val application = applicationService.getApplication(execution.processBusinessKey)
        fillProcessVariables(execution, application)
    }

    private fun fillProcessVariables(execution: DelegateExecution, application: Application) {
        val profile = application.profiles?.first() ?: throw Exception("profile is not exist")
        val passport = profile.documents?.first { it.docType == "PASSPORT" }
        val inn = profile.documents?.first { it.docType == "INN" }
        val snils = profile.documents?.first { it.docType == "SNILS" }
        val isUnderwriting = application.isUnderwriting

        execution.setVariable(CLIENT_ID.value, profile.clientId)
        execution.setVariable(PASSPORT.value, passport?.series.orEmpty() + passport?.number)
        execution.setVariable(INN.value, inn?.series.orEmpty() + inn?.number)
        execution.setVariable(SNILS.value, snils?.series.orEmpty() + snils?.number)
        execution.setVariable(UNDERWRITING_FLAG.value, isUnderwriting)
    }
}
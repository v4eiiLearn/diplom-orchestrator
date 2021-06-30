package ru.vgtu.diplom.orchestrator.listener

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.ExecutionListener
import org.springframework.stereotype.Component
import ru.vgtu.diplom.common.logging.Loggable

@Component
class StartDecisionProcessListener :ExecutionListener {

    companion object: Loggable

    override fun notify(execution: DelegateExecution) {
        logger.info("Start process with business key: ${execution.processBusinessKey}, variables: ${execution.variables}")
    }
}
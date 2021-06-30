package ru.vgtu.diplom.orchestrator.listener

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.ExecutionListener
import org.springframework.stereotype.Component

@Component
class PassportValidationFailListener : ExecutionListener {
    override fun notify(execution: DelegateExecution) {
        execution.setVariable("isBki", false)
        execution.setVariable("isClientValid", false)
    }
}
package ru.vgtu.diplom.orchestrator.executor

import org.camunda.bpm.engine.delegate.DelegateExecution
import org.slf4j.MDC
import ru.vgtu.diplom.common.logging.mdc.MdcVariableName


fun DelegateExecution.fillMdc() {
    MDC.put(MdcVariableName.CLIENT_ID, this.getVariable("clientId") as String)
}
package ru.vgtu.diplom.orchestrator.service

import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.springframework.stereotype.Service
import ru.vgtu.diplom.common.logging.Loggable
import ru.vgtu.diplom.orchestrator.exception.BusinessKeyAlreadyExistsException

/**
 * Сервис работы с процессами camunda
 */
@Service
class ProcessInstanceServiceImpl(
    private val runtimeService: RuntimeService
) : ProcessInstanceService {

    companion object : Loggable {
        const val INITIATE_PROCESS = "initiateProcess"
        const val DECISION_PROCESS = "decisionProcess"
    }

    override fun startInitiateProcess(businessKey: String, variables: Map<String, Any>?): ProcessInstance {
        val processInstance: ProcessInstance? = getProcessInstanceByBusinessKey(INITIATE_PROCESS, businessKey)
        if (processInstance != null) throw BusinessKeyAlreadyExistsException(businessKey)
        return startProcess(INITIATE_PROCESS, businessKey, variables)
    }

    fun startDecisionProcess(businessKey: String, variables: Map<String, Any>?): ProcessInstance {
        val processInstance: ProcessInstance? = getProcessInstanceByBusinessKey(DECISION_PROCESS, businessKey)
        if (processInstance != null) throw BusinessKeyAlreadyExistsException(businessKey)
        return startProcess(DECISION_PROCESS, businessKey, variables)
    }

    override fun getProcessInstanceByBusinessKey(processName: String, businessKey: String): ProcessInstance? {
        return runtimeService.createProcessInstanceQuery()
            .processDefinitionKey(processName)
            .processInstanceBusinessKey(businessKey)
            .singleResult()
    }

    override fun updateVariables(processName: String, businessKey: String, variables: Map<String, Any>?) {
        val processInstance: ProcessInstance? = getProcessInstanceByBusinessKey(processName, businessKey)
        if (processInstance == null) {
            logger.warn("process: $processName with business key: $businessKey not found")
            return
        }
        runtimeService.setVariables(processInstance.processInstanceId, variables)
    }

    override fun getProcessVariablesByBusinessKey(processName: String, businessKey: String): Map<String, Any> {
        val processInstance: ProcessInstance? = getProcessInstanceByBusinessKey(processName, businessKey)
        if (processInstance == null) {
            logger.warn("process: $processName with business key: $businessKey not found")
            return emptyMap()
        }
        return runtimeService.getVariables(processInstance.processInstanceId)
    }

    override fun startProcess(
        processDefinition: String,
        businessKey: String,
        variables: Map<String, Any>?
    ): ProcessInstance {
        logger.info("Start process with business key: $businessKey, variables: $variables")
        return runtimeService.startProcessInstanceByKey(processDefinition, businessKey, variables)
    }
}
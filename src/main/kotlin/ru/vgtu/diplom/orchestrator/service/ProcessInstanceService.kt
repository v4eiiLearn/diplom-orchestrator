package ru.vgtu.diplom.orchestrator.service

import org.camunda.bpm.engine.runtime.ProcessInstance

interface ProcessInstanceService {
    /**
     * Запуск бизнес-процесса с бизнес ключом и переменными
     * @param businessKey бизнес ключ процесса
     * @param variables переменные передаваемые в БП, может быть null
     * @return Инстанс процесса
     */
    fun startInitiateProcess(businessKey: String, variables: Map<String, Any>?): ProcessInstance?

    /**
     * Получение инстанса существующего процесса по имени процесса и бизнес ключу
     * @param processName Имя БП (e.g. MainLoanProcess)
     * @param businessKey бизнес ключ процесса
     * @return Инстанс процесса если он существует, иначе null
     */
    fun getProcessInstanceByBusinessKey(processName: String, businessKey: String): ProcessInstance?

    /**
     * Обновление/добавление переменных в бизнес процесса
     * @param processName Имя БП (e.g. MainLoanProcess)
     * @param businessKey бизнес ключ процесса
     * @param variables переменные передаваемые в БП
     */
    fun updateVariables(processName: String, businessKey: String, variables: Map<String, Any>?)

    /**
     * Получение всех переменных БП
     * @param processName Имя БП (e.g. MainLoanProcess)
     * @param businessKey Бизнес ключ процесса
     * @return Карта переменных процесса или пустая карта
     */
    fun getProcessVariablesByBusinessKey(processName: String, businessKey: String): Map<String, Any>

    /**
     * Запуск именного процесса
     * @param processDefinition Имя БП (e.g. initiateProcess)
     * @param businessKey Бизнес ключ процесса
     * @Param variables переменные передаваемые в БП
     */
    fun startProcess(processDefinition: String, businessKey: String, variables: Map<String, Any>?): ProcessInstance
}
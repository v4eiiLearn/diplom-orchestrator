package ru.vgtu.diplom.orchestrator.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import ru.vgtu.diplom.app.extensions.ApplicationStatus
import ru.vgtu.diplom.common.context.core.holder.ContextHolder
import ru.vgtu.diplom.common.context.core.holder.MsaContextHolder
import ru.vgtu.diplom.common.logging.Loggable
import ru.vgtu.diplom.orchestrator.dto.ClientInfo
import ru.vgtu.diplom.orchestrator.service.ProcessInstanceServiceImpl
import java.time.LocalDateTime


@RestController
class StartController(
    private val processInstanceServiceImpl: ProcessInstanceServiceImpl
) {

    companion object : Loggable

    @PostMapping("/start/initiate")
    fun initiateProcess(@RequestBody clientInfo: ClientInfo, @RequestHeader("H-Client-Id") clientId: String) {
        processInstanceServiceImpl.startInitiateProcess(
             clientId, mapOf(
                Pair("clientId", clientId),
                Pair("clientInfo", clientInfo),
            )
        )
    }

    @PostMapping("/start/decision")
    fun decisionProcess(@RequestBody appId: String) {
        processInstanceServiceImpl.startDecisionProcess(appId, mapOf())
    }
}
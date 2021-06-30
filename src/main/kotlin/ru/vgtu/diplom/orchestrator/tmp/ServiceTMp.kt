package ru.vgtu.diplom.orchestrator.tmp

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.vgtu.diplom.app.extensions.ApplicationStatus
import ru.vgtu.diplom.orchestrator.dto.ClientInfo
import ru.vgtu.diplom.orchestrator.service.ProcessInstanceServiceImpl
import java.time.LocalDate
import java.time.LocalDateTime



class ServiceTMp(
    private val processInstanceServiceImpl: ProcessInstanceServiceImpl
) {




}
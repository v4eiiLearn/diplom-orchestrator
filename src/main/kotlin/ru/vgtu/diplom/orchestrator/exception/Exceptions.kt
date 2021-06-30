package ru.vgtu.diplom.orchestrator.exception

import java.lang.RuntimeException

class BusinessKeyAlreadyExistsException(businessKey: String) :
    RuntimeException("Process instance with the same business key already exists, key = $businessKey")
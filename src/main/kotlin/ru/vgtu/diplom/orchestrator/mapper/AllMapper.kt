package ru.vgtu.diplom.orchestrator.mapper

import org.springframework.stereotype.Component
import ru.vgtu.diplom.app.extensions.ApplicationStatus
import ru.vgtu.diplom.app.model.PostApplication
import ru.vgtu.diplom.app.model.Profile
import ru.vgtu.diplom.orchestrator.dto.ClientData
import ru.vgtu.diplom.orchestrator.dto.ClientInfo

@Component
class AllMapper {
    fun ClientData.toProfile(): Profile =
        Profile(
            clientId = clientId,
            clientType = clientType,
            pfrRequestConsent = false,
            consentTelecomRequest = false,
            personalInfoRequestFlag = false,
            childrenDependentsQty = childrenDependentsQty,
            surname = surname,
            name = name,
            middleName = middleName,
            birthDate = birthDate,
            birthPlace = birthPlace,
            gender = gender,
            marriageStatus = marriageStatus,
            familyMembersNumber = 0,
            citizenshipCountry = null,
            taxCode = null,
            addresses = addresses,
            education = "middle",
            phones = phones,
            contacts = contacts,
            documents = documents
        )

    fun ClientInfo.toApplication(): PostApplication =
        PostApplication(
            userName = userName,
            login = login,
            applicationType = applicationType,
            prescoringFlag = prescoringFlag,
            loanType = loanType,
            productCode = productCode,
            creditProgram = creditProgram,
            programCode = programCode,
            appCreationDate = appCreateDate,
            confirmationDate = confirmationDate,
            applicationStatus = ApplicationStatus.DRAFT.value,
            purchaseRegion = purchaseRegion,
        )

}
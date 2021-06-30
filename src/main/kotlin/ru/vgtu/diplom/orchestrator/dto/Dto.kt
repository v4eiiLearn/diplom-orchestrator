package ru.vgtu.diplom.orchestrator.dto

import ru.vgtu.diplom.app.model.*
import java.io.Serializable
import java.time.LocalDateTime


data class ClientData(
    val clientId: String,
    var clientType: String? = null,
    var clientClass: String? = null,
    var category: String? = null,
    var surname: String? = null,
    var name: String? = null,
    var middleName: String? = null,
    var childrenDependentsQty: kotlin.Int? = null,
    var birthDate: java.time.LocalDate? = null,
    var birthPlace: String? = null,
    var gender: String? = null,
    var marriageStatus: String? = null,
    var addresses: kotlin.collections.List<Address>? = null,
    var phones: kotlin.collections.List<Phone>? = null,
    var contacts: kotlin.collections.List<Contact>? = null,
    var documents: kotlin.collections.List<Document>? = null
) : Serializable

data class ClientIncomes(
    var incomes: kotlin.collections.List<Income>? = null,
    var employments: kotlin.collections.List<Employment>? = null,
) : Serializable

data class ClientInfo(
    var userName: String,
    var login: String,
    var applicationType: String,
    var prescoringFlag: Boolean,
    var loanType: String,
    var productCode: String,
    var creditProgram: String,
    var programCode: String,
    var appCreateDate: LocalDateTime?,
    var confirmationDate: LocalDateTime?,
    var applicationStatus: String?,
    var purchaseRegion: String
): Serializable

enum class ProcessVariable(val value: String) {
    BKI_FLAG("isBki"),
    PASSPORT_FLAG("isPassportValid"),
    INCOME_FLAG("isIncomeValid"),
    CLIENT_FLAG("isClientValid"),
    SOLVENCY_FLAG("isSolvency"),
    UNDERWRITING_FLAG("isUnderwriting"),
    CLIENT_ID("clientId"),
    PASSPORT("passportNumber"),
    INN("inn"),
    SNILS("snils")
}
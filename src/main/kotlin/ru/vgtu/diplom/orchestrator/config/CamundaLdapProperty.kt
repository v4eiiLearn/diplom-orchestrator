package ru.vgtu.diplom.orchestrator.config

import org.camunda.bpm.identity.impl.ldap.LdapConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("camunda.ldap")
data class CamundaLdapProperty(
    val activate: Boolean,
    val acceptUntrustedCertificates: Boolean,
    val adminUserName: String?,
    val adminGroup: String?,
    val userGroup: String,
    val ldapConfiguration: LdapConfiguration
)
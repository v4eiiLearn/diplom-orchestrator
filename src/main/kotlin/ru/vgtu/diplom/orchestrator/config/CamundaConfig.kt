package ru.vgtu.diplom.orchestrator.config

import org.camunda.bpm.engine.AuthorizationService
import org.camunda.bpm.engine.authorization.Authorization
import org.camunda.bpm.engine.authorization.Permissions
import org.camunda.bpm.engine.authorization.Resources
import org.camunda.bpm.engine.impl.plugin.AdministratorAuthorizationPlugin
import org.camunda.bpm.identity.impl.ldap.plugin.LdapIdentityProviderPlugin
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.vgtu.diplom.common.logging.Loggable

fun CharSequence?.isNotNullOrBlank() = this != null && this.isNotBlank()

@Configuration
@EnableConfigurationProperties(CamundaLdapProperty::class)
class CamundaLdapConfig(
    private val ldapProperty: CamundaLdapProperty
) {
    companion object : Loggable

    @Bean
    @ConditionalOnExpression("\${camunda.ldap.activate:true}")
    fun grantRightsUserGroup(authorizationService: AuthorizationService): Boolean {
        if (ldapProperty.userGroup.isNotNullOrBlank()) {
            val authorizationQuery = authorizationService.createAuthorizationQuery().groupIdIn(ldapProperty.userGroup)

            if (authorizationQuery.resourceType(Resources.APPLICATION).resourceId("cockpit").count() == 0L &&
                authorizationQuery.resourceType(Resources.APPLICATION).resourceId("tasklist").count() == 0L
            ) {
                grantApplicationAccessToUser(authorizationService)
            }

            for (resource in Resources.values()) {
                if (authorizationQuery.resourceType(resource).resourceId(Authorization.ANY).count() == 0L &&
                    resource != Resources.APPLICATION
                ) {
                    val auth = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT)
                    auth.groupId = ldapProperty.userGroup
                    auth.setResource(resource)
                    auth.resourceId = Authorization.ANY
                    auth.addPermission(Permissions.ALL)

                    authorizationService.saveAuthorization(auth)
                    logger.info("GRANT group ${ldapProperty.userGroup} ALL permissions on resource ${resource.resourceName()}.")
                }
            }

            return true
        } else {
            logger.warn("user rights are not granted, field null or blank")
            return false
        }
    }

    @Bean
    @ConditionalOnExpression("\${camunda.ldap.activate:true}")
    fun ldapIdentityProviderPlugin(): LdapIdentityProviderPlugin {
        val plugin = LdapIdentityProviderPlugin()
        return with(plugin) {
            serverUrl = ldapProperty.ldapConfiguration.serverUrl

            managerDn = ldapProperty.ldapConfiguration.managerDn
            managerPassword = ldapProperty.ldapConfiguration.managerPassword

            baseDn = ldapProperty.ldapConfiguration.baseDn
            isUseSsl = ldapProperty.ldapConfiguration.isUseSsl
            isAcceptUntrustedCertificates = ldapProperty.acceptUntrustedCertificates

            userSearchBase = ldapProperty.ldapConfiguration.userSearchBase
            userSearchFilter = "(${ldapProperty.ldapConfiguration.userSearchFilter})"
            userIdAttribute = ldapProperty.ldapConfiguration.userIdAttribute
            userFirstnameAttribute = ldapProperty.ldapConfiguration.userFirstnameAttribute
            userLastnameAttribute = ldapProperty.ldapConfiguration.userLastnameAttribute
            userEmailAttribute = ldapProperty.ldapConfiguration.userEmailAttribute
            userPasswordAttribute = ldapProperty.ldapConfiguration.userPasswordAttribute

            groupSearchBase = ldapProperty.ldapConfiguration.groupSearchBase
            groupSearchFilter = "(${ldapProperty.ldapConfiguration.groupSearchFilter})"
            groupIdAttribute = ldapProperty.ldapConfiguration.groupIdAttribute
            groupNameAttribute = ldapProperty.ldapConfiguration.groupNameAttribute
            groupMemberAttribute = ldapProperty.ldapConfiguration.groupMemberAttribute
            securityAuthentication = ldapProperty.ldapConfiguration.securityAuthentication

            this
        }
    }

    @Bean
    @ConditionalOnExpression("\${camunda.ldap.activate:true}")
    fun administratorAuthorizationPlugin(): AdministratorAuthorizationPlugin {
        val plugin = AdministratorAuthorizationPlugin()
        return with(plugin) {
            if (ldapProperty.adminUserName.isNotNullOrBlank() || ldapProperty.adminGroup.isNotNullOrBlank()) {
                if (ldapProperty.adminUserName.isNotNullOrBlank()) administratorUserName = ldapProperty.adminUserName
                if (ldapProperty.adminGroup.isNotNullOrBlank()) administratorGroupName = ldapProperty.adminGroup
            } else {
                logger.warn("admin rights are not granted, fields null or blank")
            }

            this
        }
    }

    fun grantApplicationAccessToUser(authorizationService: AuthorizationService): Boolean {
        val authCockpit = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT)
        authCockpit.groupId = ldapProperty.userGroup
        authCockpit.setResource(Resources.APPLICATION)
        authCockpit.resourceId = "cockpit"
        authCockpit.addPermission(Permissions.ALL)
        val authTasklist = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT)
        authTasklist.groupId = ldapProperty.userGroup
        authTasklist.setResource(Resources.APPLICATION)
        authTasklist.resourceId = "tasklist"
        authTasklist.addPermission(Permissions.ALL)

        authorizationService.saveAuthorization(authCockpit)
        logger.info(
            "GRANT group ${ldapProperty.userGroup} ALL permissions on resource ${Resources.APPLICATION.resourceName()}, " +
                    "resourceId ${authCockpit.resourceId}."
        )
        authorizationService.saveAuthorization(authTasklist)
        logger.info(
            "GRANT group ${ldapProperty.userGroup} ALL permissions on resource ${Resources.APPLICATION.resourceName()}, " +
                    "resourceId ${authTasklist.resourceId}."
        )

        return true
    }
}
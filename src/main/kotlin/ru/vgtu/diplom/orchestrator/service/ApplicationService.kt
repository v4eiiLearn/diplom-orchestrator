package ru.vgtu.diplom.orchestrator.service

import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.stereotype.Service
import ru.vgtu.diplom.app.client.ApplicationClient
import ru.vgtu.diplom.app.model.Application
import ru.vgtu.diplom.app.model.PostApplication
import ru.vgtu.diplom.app.model.Profile
import ru.vgtu.diplom.app.model.Status

@Service
class ApplicationService(
    private val applicationClient: ApplicationClient
) {
    fun createApplication(postApplication: PostApplication) =
        applicationClient.createApplication(postApplication).block() ?: throw Exception("Application info is empty")

    fun updateProfile(appId: String, profile: Profile) {
        applicationClient.updateProfile(appId, profile).subscribe()
    }

    suspend fun getSuspendApplication(appId: String) =
        applicationClient.getApplicationByAppId(appId).awaitFirstOrNull() ?: throw Exception("Application not found!")

    fun getApplication(appId: String) =
        applicationClient.getApplicationByAppId(appId).block() ?: throw Exception("Application not found!")

    fun updateStatus(appId: String, status: String) {
        applicationClient.updateStatus(appId, Status(status)).subscribe()
    }

    fun updateApplication(application: Application) {
        applicationClient.updateApplication(application).subscribe()
    }

}
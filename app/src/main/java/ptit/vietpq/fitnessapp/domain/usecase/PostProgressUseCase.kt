package ptit.vietpq.fitnessapp.domain.usecase

import kotlinx.coroutines.withContext
import ptit.vietpq.fitnessapp.core.AppCoroutineDispatchers
import ptit.vietpq.fitnessapp.core.safeApiCall
import ptit.vietpq.fitnessapp.data.local.session.Session
import ptit.vietpq.fitnessapp.data.local.sharepref.SharePreferenceProvider
import ptit.vietpq.fitnessapp.data.remote.request.ProgressRequest
import ptit.vietpq.fitnessapp.data.remote.service.ProgressService
import ptit.vietpq.fitnessapp.extension.toCurrentDateString
import java.util.Date
import javax.inject.Inject

class PostProgressUseCase @Inject constructor(
    private val progressService: ProgressService,
    private val dispatchers: AppCoroutineDispatchers,
    private val sharePreferenceProvider: SharePreferenceProvider,
) {

    suspend operator fun invoke(
        setsCompleted: Int,
        repsCompleted: Int,
        weightUsed: Int,
        duration: Int,
        status: String,
        notes: String,
    ) = withContext(dispatchers.io) {
        safeApiCall {
            progressService.postProgress(
                progress = ProgressRequest(
                    userId = sharePreferenceProvider.userId,
                    trainingProgramId = Session.trainingProgramId,
                    exerciseId = Session.exerciseId,
                    trainingProgramExerciseId = Session.trainingProgramExerciseId,
                    completionDate = Date().toCurrentDateString(),
                    setsCompleted = setsCompleted,
                    repsCompleted = repsCompleted,
                    weightUsed = weightUsed,
                    duration = duration,
                    status = status,
                    notes = notes,
                )
            )
        }
    }

}
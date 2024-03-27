package data

import androidx.compose.ui.graphics.vector.addPathNodes
import data.model.MapDTO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class MapHelperRepository {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.BODY
        }
    }

    val basicUrl = "http://192.168.0.105:5000"

    suspend fun getMaps() = client.get(basicUrl) { url { appendPathSegments("all_maps") } }.body<List<MapDTO>>()


}
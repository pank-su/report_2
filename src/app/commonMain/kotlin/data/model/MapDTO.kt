package data.model

import kotlinx.serialization.Serializable

@Serializable
data class MapDTO(
    val id: Long, val mapPath: String, val name: String, val yamlPath: String, val isSelected: Boolean
)

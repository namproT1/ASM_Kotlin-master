package fpoly.kot1041.asm.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val id: String = "",
    @SerialName("title")
    val title: String = "",
    @SerialName("content")
    val content: String = "",
    @SerialName("date")
    val date: String = "",
    @SerialName("isRead")
    var isRead: Boolean = false
) 
package br.dev.thiagopereira.luizalabs.db.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "pull_requests",
    indices = [
        Index("repositorioId")
    ]
)
@Parcelize
data class PullRequestEntity(
    @PrimaryKey
    val id: Long,
    val repositorioId: Long,
    val title: String,
    val body: String?,
    @Embedded(prefix = "user_")
    val user: User,
    @Embedded(prefix = "links_")
    @SerializedName("_links")
    val links: Links,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
): Parcelable {

    val link: String
        get() = links.html.href

    @Parcelize
    data class User(
        val login: String,
        @SerializedName("avatar_url")
        val avatarUrl: String
    ): Parcelable

    @Parcelize
    data class Links(
        @Embedded("html_")
        val html: Link
    ): Parcelable {

        @Parcelize
        data class Link(
            val href: String
        ): Parcelable

    }

}

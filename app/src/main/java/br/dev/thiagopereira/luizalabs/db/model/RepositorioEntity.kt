package br.dev.thiagopereira.luizalabs.db.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "repositorios",
    indices = [
        Index("language")
    ]
)
@Parcelize
data class RepositorioEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    val language: String,
    @Embedded(prefix = "owner_")
    val owner: Owner,
    val description: String?,
    @SerializedName("stargazers_count")
    val stargazersCount: Int,
    @SerializedName("forks_count")
    val forksCount: Int,
    @SerializedName("open_issues_count")
    val openIssuesCount: Int
): Parcelable {

    @Parcelize
    data class Owner(
        val login: String,
        @SerializedName("avatar_url")
        val avatarUrl: String,
    ): Parcelable

}

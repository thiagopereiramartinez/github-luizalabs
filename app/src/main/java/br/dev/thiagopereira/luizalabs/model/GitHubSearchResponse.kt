package br.dev.thiagopereira.luizalabs.model

import android.os.Parcelable
import br.dev.thiagopereira.luizalabs.db.model.RepositorioEntity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GitHubSearchResponse(
    @SerializedName("total_count")
    val totalCount: Int,
    val items: List<RepositorioEntity>
): Parcelable

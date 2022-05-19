package com.linc.inphoto.data.repository

import com.linc.inphoto.data.mapper.toTagModel
import com.linc.inphoto.data.network.api.TagApiService
import com.linc.inphoto.data.network.model.post.TagApiModel
import com.linc.inphoto.entity.post.Tag
import com.linc.inphoto.utils.extensions.EMPTY
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TagRepository @Inject constructor(
    private val tagApiService: TagApiService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun loadTags(): List<Tag> = withContext(ioDispatcher) {
        return@withContext loadTags(String.EMPTY)
    }

    suspend fun loadTags(query: String): List<Tag> = withContext(ioDispatcher) {
        return@withContext tagApiService.getTags(query).body
            ?.map(TagApiModel::toTagModel)
            .orEmpty()
    }

}
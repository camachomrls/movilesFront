package com.alilopez.demo.features.jsonplaceholder.domain.usescases

import com.alilopez.demo.features.jsonplaceholder.domain.entities.Posts
import com.alilopez.demo.features.jsonplaceholder.domain.repositories.PostsRepository
import com.alilopez.kt_demohilt.core.di.RickAndMortyRetrofit
import javax.inject.Inject

class GetPostsUseCase @Inject constructor(
        private val repository: PostsRepository
    ) {

        suspend operator fun invoke(): Result<List<Posts>> {
            return try {
                val posts = repository.getPosts()

                val filteredPosts = posts.filter { it.title.isNotBlank() }

                if (filteredPosts.isEmpty()) {
                    Result.failure(Exception("No se encontraron post válidos"))
                } else {
                    Result.success(filteredPosts)
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
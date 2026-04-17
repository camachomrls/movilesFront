package com.alilopez.kt_demohilt.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object PostsListRoute

@Serializable
data class PostDetailsRoute(val postId: Int)
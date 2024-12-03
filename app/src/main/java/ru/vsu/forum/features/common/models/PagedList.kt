package ru.vsu.forum.features.common.models

data class PagedList<T>(val items: List<T>, val count: Int)
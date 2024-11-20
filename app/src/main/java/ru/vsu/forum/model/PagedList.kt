package ru.vsu.forum.model

data class PagedList<T>(val items: List<T>, val count: Int)
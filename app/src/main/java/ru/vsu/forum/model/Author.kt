package ru.vsu.forum.model

import java.util.UUID

data class Author(val id: UUID, val name : String, val avatarId: UUID?)

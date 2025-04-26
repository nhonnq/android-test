package dev.nhonnq.domain.entities

interface IUser {
    val login: String
    val avatarUrl: String?
    val htmlUrl: String?
}
package com.udindev.githubuser.util

open class Event<out T>(private val content: T) {

    @Suppress("MemberVisibilityCanBePrivate")

    var userNotFound = false
        private set

    fun getUserNotFound(): T? {
        return if (userNotFound) {
            null
        } else {
            userNotFound = true
            content
        }
    }
}
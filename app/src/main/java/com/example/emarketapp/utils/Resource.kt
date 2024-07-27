package com.example.emarketapp.utils

sealed class Resource<out T> {
    data class Success<out T>(val result: T) : Resource<T>()
    data class Failure(val exceptionMessage: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
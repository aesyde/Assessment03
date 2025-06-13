package com.fahrulredho0018.assessment03.ui.theme.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahrulredho0018.assessment03.model.Book
import com.fahrulredho0018.assessment03.network.ApiStatus
import com.fahrulredho0018.assessment03.network.BookApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Book>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun retrieveData(userId: String) {
        viewModelScope.launch (Dispatchers.IO) {
            status.value = ApiStatus.LOADING
            try {
                data.value = BookApi.service.getBooks(userId)
                status.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "FailureL ${e.message}")
                status.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(userId: String, title: String, author: String, publisher: String, year: String, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = BookApi.service.postBooks(
                    userId,
                    title.toRequestBody("text/plain".toMediaTypeOrNull()),
                    author.toRequestBody("text/plain".toMediaTypeOrNull()),
                    publisher.toRequestBody("text/plain".toMediaTypeOrNull()),
                    year.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody()
                )

                if (result.status == "success") {
                    retrieveData(userId)
                } else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun updateData(
        userId: String,
        title: String,
        author: String,
        publisher: String,
        year: String,
        bitmap: Bitmap,
        id: String
    )
    {
        Log.d("MainViewModel", "$userId , $title , $author , $id")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = BookApi.service.updateBooks(
                    userId,
                    title.toRequestBody("text/plain".toMediaTypeOrNull()),
                    author.toRequestBody("text/plain".toMediaTypeOrNull()),
                    publisher.toRequestBody("text/plain".toMediaTypeOrNull()),
                    year.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bitmap.toMultipartBody(),
                    id
                )

                if (result.status == "success") {
                    retrieveData(userId)
                } else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody)
    }

    fun deleteBook(userId: String, id: String) {
        viewModelScope.launch {
            try {
                val result = BookApi.service.deleteBooks(id)

                if (result.status == "success") {
                    retrieveData(userId)
                } else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun clearMessage() { errorMessage.value = null }
}
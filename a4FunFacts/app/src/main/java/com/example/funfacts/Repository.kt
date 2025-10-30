package com.example.funfacts

import android.util.Log
import com.example.funfacts.room.Fact
import com.example.funfacts.room.FactDao
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class Repository (val scope: CoroutineScope, private val dao: FactDao) {
    private val client = HttpClient(Android)
    {
        install(ContentNegotiation){
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    val allFacts: Flow<List<Fact?>> = dao.getAllFacts()

    fun fetchNewFact() {
        scope.launch {
            try {
                 val fact: Fact = client.get("https://uselessfacts.jsph.pl//api/v2/facts/random").body()

                dao.insertFact(fact)
            }
            catch (e: Exception)
            {
                Log.e("Fun Fact Activity", "Error Fetching", e)
            }
        }
    }

    fun deleteFact(id: Int) {
        scope.launch {
            dao.deleteFact(id)
        }
    }
}
package com.example.cursofirebaselife.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cursofirebaselife.domain.CanAccessToApp
import com.example.cursofirebaselife.presentation.model.Artist
import com.example.cursofirebaselife.presentation.model.Player
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class HomeViewmodel : ViewModel() {

    private var canAccessToApp: CanAccessToApp = CanAccessToApp()
    private val database = Firebase.database
    private var db: FirebaseFirestore = Firebase.firestore

    private val _artist = MutableStateFlow<List<Artist>>(emptyList())
    val artist: StateFlow<List<Artist>> = _artist

    private val _player = MutableStateFlow<Player?>(null)
    val player: StateFlow<Player?> = _player

    private val _blockVersion = MutableStateFlow<Boolean>(false)
    val blockVersion: StateFlow<Boolean> = _blockVersion

    init {
        checkUserVersion()
        getArtists()
        getPlayer()
    }

    private fun checkUserVersion() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                canAccessToApp()
            }
            _blockVersion.value = !result
        }
    }

    private fun getPlayer() {
        viewModelScope.launch {
            collectPlayer().collect {
                val player = it.getValue(Player::class.java)
                _player.value = player
            }
        }
    }

    private fun collectPlayer(): Flow<DataSnapshot> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                trySend(snapshot).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("aris log", "Error Firebase: ${error.message}")

                // EVITAR CRASH DE LA APP
                if (!isClosedForSend) {
                    close()  // <-- no lanzamos excepción
                }
            }
        }

        val ref = database.reference.child("player")
        ref.addValueEventListener(listener)

        awaitClose {
            ref.removeEventListener(listener)
        }
    }


    private fun getArtists() {
        viewModelScope.launch {
            val result: List<Artist> = withContext(Dispatchers.IO) {
                getAllArtists()
            }
            _artist.value = result
        }
    }

    private suspend fun getAllArtists(): List<Artist> {
        return try {
            db.collection("artists").get().await().documents.mapNotNull { snapshot ->
                snapshot.toObject(Artist::class.java)
            }
        } catch (e: Exception) {
            Log.i("aris", e.toString())
            emptyList()
        }
    }

    fun onPlaySelected() {
        if (player.value != null) {
            val currentPlayer = _player.value?.copy(play = !player.value?.play!!)
            val ref = database.reference.child("player")
            ref.setValue(currentPlayer)
        }
    }

    fun onCancelSelected() {
        val ref = database.reference.child("player")
        ref.setValue(null)
    }

    fun addPlayer(artist: Artist) {
        val ref = database.reference.child("player")
        val player = Player(artist = artist, play = true)
        ref.setValue(player)
    }
}

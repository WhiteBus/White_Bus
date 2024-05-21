package com.google.mediapipe.examples.facelandmarker.remote.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.mediapipe.examples.facelandmarker.model.Passenger

class PassengerRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // 파이어 베이스 부분 -> 이후에 완성 하기
//    suspend fun getPassengers(): List<Passenger> {
//        val uid = auth.currentUser?.uid ?: return emptyList()
//        val userDocRef = db.collection("users").document(uid)
//
//        return try {
//            val documentSnapshot = userDocRef.get().await()
//            if (documentSnapshot.exists()) {
//                val data = documentSnapshot.data ?: return emptyList()
//                val nickname = data["nickname"] as? String ?: ""
//                val profileImageUrl = data["profileImageUrl"] as? String ?: ""
//                listOf(Passenger(nickname, profileImageUrl))
//            } catch (e: Exception) {
//                println("Error getting user data: $e")
//                emptyList()
//            }
//        }
//    }

    fun listenToPassengers(onDataChanged: (List<Passenger>) -> Unit): ListenerRegistration {
        val uid = auth.currentUser?.uid ?: return db.collection("dummy").addSnapshotListener { _, _ -> }
        val userDocRef = db.collection("users").document(uid)

        return userDocRef.addSnapshotListener { snapshot, _ ->
            if (snapshot != null && snapshot.exists()) {
                val data = snapshot.data ?: return@addSnapshotListener
                val nickname = data["nickname"] as? String ?: ""
                val profileImageUrl = data["profileImageUrl"] as? String ?: ""
                onDataChanged(listOf(Passenger(nickname, profileImageUrl)))
            } else {
                onDataChanged(emptyList())
            }
        }
    }
}
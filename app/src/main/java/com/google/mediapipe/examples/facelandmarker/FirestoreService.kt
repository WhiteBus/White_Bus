package com.google.mediapipe.examples.facelandmarker
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.mediapipe.examples.facelandmarker.remote.dto.Station
private const val TAG = "fire"

data class Users(
    val nickname: String = "",
    val profileImageUrl: String = "",
    val blindType: Int
)

data class DriverUser(
    val nickname: String = "",
)

data class BlindUser(
    val nickname: String = "",
    val profileImageUrl: String = ""
)

data class StationStay(
    val nickname: String = "",
    val profileImageUrl: String = ""
)

data class Matching(
    val driverId: String = "",
    val blindIds: List<String> = listOf()
)

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
/*
    //user 컬렉션의 사용자들을 blind와 dirver로 나누는 함수
    fun transferUserData() {
        val uid = auth.currentUser?.uid ?: return

        val userDocRef = db.collection("users").document(uid)
        userDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject<Users>()
                user?.let {
                    if(it.blindType==1){
                        addDriver(uid, it.nickname)
                    }else if(it.blindType == 2){
                        addBlind(uid,it.nickname, it.profileImageUrl)
                    }
                }
            }
            .addOnFailureListener { e ->
                println("Error getting user data: $e")
            }
    }

    //driver 컬렉션에 유저 이동
    fun addDriver(uid: String, nickname: String) {
        val driver: MutableMap<String, Any> = HashMap()
        driver["nickname"] = nickname
        db.collection("DriverUser").document(uid)
            .set(driver)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    // Driver 컬렉션에 버스번호 새로운 필드 추가
    fun addBusNumberFieldToDriver(driverUid: String, busNumberValue: String) {
        val driverDocRef = db.collection("DriverUser").document(driverUid)

        // update() 메서드를 사용하여 새로운 필드 추가
        driverDocRef.update("busNumb", busNumberValue)
            .addOnSuccessListener {
                println("busNumb field added successfully to Driver")
            }
            .addOnFailureListener { e ->
                println("Error adding busNumb field to Driver: $e")
            }
    }

    //blind 컬렉션에 유저 이동
    fun addBlind(uid: String, nickname: String, profileImageUrl: String) {
        val blind = BlindUser(nickname, profileImageUrl)
        db.collection("BlindUser").document(uid).set(blind)
            .addOnSuccessListener {
                println("Blind added successfully")
            }
            .addOnFailureListener { e ->
                println("Error adding blind: $e")
            }
    }



    // 특정 blind 유저를 Station 컬렉션에 추가
    fun addBlindToStation(driverid: String, blindUid: String) {
        val firebaseUser = auth.currentUser
        firebaseUser?.let {
            val uid = it.uid
            val blind: MutableMap<String, Any> = HashMap()
            blind["uid"] = uid

            db.collection("OnBus").document(driverid)
                .set(blind)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
        val blindDocRef = db.collection("BlindUser").document(blindUid)
        blindDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                val blind = documentSnapshot.toObject<Users>()
                blind?.let {
                    val station = StationStay(it.nickname, it.profileImageUrl)
                    db.collection("StationStay").document(blindUid).set(station)
                        .addOnSuccessListener {
                            println("Blind user added to Station successfully")
                        }
                        .addOnFailureListener { e ->
                            println("Error adding blind user to Station: $e")
                        }
                }
            }
            .addOnFailureListener { e ->
                println("Error getting blind user data: $e")
            }
    }
*/
    // 드라이버와 매칭될 blind 유저들을 Matching 컬렉션에 추가
    fun createMatching(driverUid: String, blindUids: List<String>) {
        val matching = Matching(driverId = driverUid, blindIds = blindUids)
        db.collection("Matching").document(driverUid).set(matching)
            .addOnSuccessListener {
                println("Matching created successfully")
            }
            .addOnFailureListener { e ->
                println("Error creating matching: $e")
            }
    }

    // 특정 blind 유저를 Station 컬렉션에서 가져와 Matching 컬렉션에 추가
    fun processBlindUserAtStation(driverUid: String, blindUid: String) {
        val stationDocRef = db.collection("StationStay").document(blindUid)
        stationDocRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Matching 컬렉션에 추가
                    db.collection("Matching").document(driverUid).get()
                        .addOnSuccessListener { matchingSnapshot ->
                            if (matchingSnapshot.exists()) {
                                // 기존 Matching 문서에 blindUid 추가
                                val currentBlindUids = matchingSnapshot.get("blindIds") as? List<String> ?: listOf()
                                val updatedBlindUids = currentBlindUids.toMutableList().apply { add(blindUid) }
                                db.collection("Matching").document(driverUid).update("blindIds", updatedBlindUids)
                                    .addOnSuccessListener {
                                        println("Blind user added to existing matching successfully")
                                    }
                                    .addOnFailureListener { e ->
                                        println("Error updating matching: $e")
                                    }
                            } else {
                                // 새로운 Matching 문서 생성
                                val newMatching = Matching(driverUid, listOf(blindUid))
                                db.collection("Matching").document(driverUid).set(newMatching)
                                    .addOnSuccessListener {
                                        println("New matching created successfully")
                                    }
                                    .addOnFailureListener { e ->
                                        println("Error creating new matching: $e")
                                    }
                            }
                        }
                } else {
                    println("Blind user not found in Stations collection")
                }
            }
            .addOnFailureListener { e ->
                println("Error getting blind user from station: $e")
            }
    }
    // Matching 컬렉션에 새로운 필드 추가
    fun addBusOutFieldToOnBus(driverUid: String, busOutValue: Boolean) {
        val matchingDocRef = db.collection("Matching").document(driverUid)

        // update() 메서드를 사용하여 새로운 필드 추가
        matchingDocRef.update("busOut", busOutValue)
            .addOnSuccessListener {
                println("busOut field added successfully to Matching")
            }
            .addOnFailureListener { e ->
                println("Error adding busOut field to Matching: $e")
            }
    }

}

/*
1. 문서 이름 알고 있는 경우
val docRef = db.collection("your_collection").document("your_document_id")
docRef.get()
    .addOnSuccessListener { document ->
        if (document != null) {
            // 문서가 존재할 때 필드 값 가져오기
            val fieldData = document.data?.get("your_field_name")
            println("Field Value: $fieldData")
        } else {
            println("No such document")
        }
    }
    .addOnFailureListener { exception ->
        println("get failed with ", exception)
    }


2. 특정 필드의 값을 기준으로 컬렉션 내의 문서를 검색하는 경우
db.collection("your_collection")
    .whereEqualTo("your_field_name", "desired_field_value")
    .get()
    .addOnSuccessListener { documents ->
        for (document in documents) {
            // 문서에서 필드 값 가져오기
            val fieldData = document.data["your_field_name"]
            println("Field Value: $fieldData")
        }
    }
    .addOnFailureListener { exception ->
        println("get failed with ", exception)
    }

 */


/*
    private lateinit var firestoreService: FirestoreService

    onCreate내부
    firestoreService = FirestoreService()

    사용 시
    firestoreService.함수명(매개변수)

 */
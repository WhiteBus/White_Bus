package com.google.mediapipe.examples.facelandmarker

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.oAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.initialize
import com.google.mediapipe.examples.facelandmarker.databinding.ActivityAppUserDeterminantBinding
import com.google.mediapipe.examples.facelandmarker.databinding.ActivityLoginBinding
import com.google.mediapipe.examples.facelandmarker.repository.activity_home
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

private const val TAG = "LoginActivity"


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Firebase.initialize(this)
        auth = FirebaseAuth.getInstance()
        val kakaoLoginBtn = findViewById<ImageView>(R.id.login)

        kakaoLoginBtn.setOnClickListener {
            UserApiClient.instance.run {
                if (isKakaoTalkLoginAvailable(this@LoginActivity)) {
                    loginWithKakaoTalk(this@LoginActivity, callback = kakaoLoginCallback)
                } else {
                    loginWithKakaoAccount(this@LoginActivity, callback = kakaoLoginCallback)
                }
            }
        }
    }
    private val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.w(TAG, "Kakao sign in failed", error)
        } else if (token != null) {
            Log.d(TAG, "Kakao sign in succeeded")

            val providerBuilder = OAuthProvider.newBuilder("oidc.white_bus")
            val providerId = "oidc.white_bus"

            val credential = oAuthCredential(providerId) {
                setIdToken(token.idToken)
            }

            Firebase.auth
                .signInWithCredential(credential)
                .addOnSuccessListener { authResult ->
                    val user = auth.currentUser
                    Log.d(TAG, "Firebase sign in succeeded: $user")
                    user?.let {
                        val userRef = db.collection("users").document(it.uid)
                        Log.d(TAG, "Firebase sign in succeeded: ${it.uid}")

                        userRef.get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    val blindtype = documentSnapshot.getLong("blindtype")
                                    Log.d(TAG, "Firebase sign in succeeded: $blindtype")

                                    if (blindtype?.toInt() == 1 ) {
                                        val intent = Intent(this@LoginActivity, BusNumberInputActivity::class.java)
                                        startActivity(intent)
                                    }else if(blindtype?.toInt() == 2){
                                        Log.d(TAG, "Firebase sign in succeeded: blindtype 2")

                                        //val intent = Intent(this@LoginActivity, OnStationUser::class.java)
                                        //장애인(2) -> 즐겨찾기 view
                                        val intent = Intent(this@LoginActivity, FindCurrentPosition::class.java)
                                        startActivity(intent)
                                    }
                                    else {
                                        val intent = Intent(this@LoginActivity, AppUserDeterminantActivity::class.java)
                                        startActivity(intent)
                                    }
                                    finish()
                                } else {
                                    // Firestore에 사용자 정보가 없는 경우
                                    val userData = hashMapOf(
                                        "blindtype" to 0 // 기본 blindtype
                                    )
                                    saveKakaoUserInfoToFirebase()

                                    db.collection("users").document(it.uid)
                                        .set(userData)
                                        .addOnSuccessListener {
                                            // 사용자 정보 등록 후 SelectUserActivity로 이동
                                            val intent = Intent(
                                                this@LoginActivity,
                                                AppUserDeterminantActivity::class.java
                                            )
                                            startActivity(intent)
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
                                            // 사용자 정보 등록 실패 시 로그 기록
                                            Log.e(TAG, "Error adding document", e)
                                        }
                                }
                            }
                            .addOnFailureListener { e ->
                                // Firestore 문서 가져오기 실패 시 로그 기록
                                Log.e(TAG, "Error getting document", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    // Firebase 로그인 실패 시 로그 기록
                    Log.w(TAG, "Firebase sign in failed", e)
                }
        }
    }
    private fun saveKakaoUserInfoToFirebase() {
        // 카카오 SDK를 사용하여 사용자 정보 가져오기
        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(
                    TAG, "사용자 정보 요청 성공" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )
                val nickname = user.kakaoAccount?.profile?.nickname ?: ""
                val profileImageUrl = user.kakaoAccount?.profile?.thumbnailImageUrl ?: ""
                saveUserInfoToFirebase(profileImageUrl, nickname)
            }
        }
    }

    private fun saveUserInfoToFirebase(profileImageUrl: String, nickname: String) {
        val firebaseUser = auth.currentUser
        firebaseUser?.let {
            val uid = it.uid
            val userData: MutableMap<String, Any> = HashMap()
            userData["profileImageUrl"] = profileImageUrl
            userData["nickname"] = nickname
            db.collection("users").document(uid)
                .set(userData)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }

    fun checkBlind() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userRef = db.collection("users").document(currentUser.uid)
            userRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    val blindtype = documentSnapshot.getLong("blindtype")
                    if (blindtype?.toInt() == 1 || blindtype?.toInt() == 2) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this@LoginActivity, AppUserDeterminantActivity::class.java)
                        startActivity(intent)
                    }
                    finish()
                }
                .addOnFailureListener { e ->
                    // Firestore 문서 가져오기 실패 시 로그 기록
                    Log.e(TAG, "Error getting document", e)
                }
        }
    }


    // Switch the Activity function
    private fun switchActivity(activity: Activity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.screen_none, R.anim.screen_exit)
    }
}
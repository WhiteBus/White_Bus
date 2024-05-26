package com.google.mediapipe.examples.facelandmarker.remote.service;



//class FindNearestStationService {
//    private lateinit var findNearestStationView: FindNearestStationView
//
//    fun setNearestStationGetView(findNearestStationView: FindNearestStationView) {
//        this.findNearestStationView = findNearestStationView
//    }
//
//    // 이후 카카오톡 자동 로그인 시 accessToken 필요
//    fun getNearestStation(lang: String, longitude: Double, latitude: Double, radius: Int, stationClass: Int, apiKey: String) {
//        val findNearestStationService = getRetrofit().create(FindNearestStationInterface::class.java)
//        findNearestStationService.getNearestStation(lang, longitude, latitude, radius, stationClass, apiKey)
//            .enqueue(object : Callback<FindNearestStationGetRes> {
//                override fun onResponse(
//                    call: Call<FindNearestStationGetRes>,
//                    response: Response<FindNearestStationGetRes>
//                ) {
//                    if (response.isSuccessful) {
//                        val result: FindNearestStationGetRes? = response.body()
//                        if (result != null) {
//                            // 서버 응답 코드에 따라 성공 또는 실패 처리
//                            val count = result.result.count
//                            if (count > 0) {
//                                findNearestStationView.onFindNearestStationSuccess(result)
//                            } else {
//                                findNearestStationView.onFindNearestStationFailure("주변에 역이 없습니다.", result)
//                            }
//                        } else {
//                            Log.e("FIND-NEAREST-STATION-SUCCESS", "Response body is null")
//                            findNearestStationView.onFindNearestStationFailure("응답이 없습니다.", result)
//                        }
//                    } else {
//                        Log.e("FIND-NEAREST-STATION-SUCCESS", "Response not successful: $response")
//                    }
//                }
//
//                override fun onFailure(call: Call<FindNearestStationGetRes>, t: Throwable) {
//                    Log.d("FIND-NEAREST-STATION-FAILURE", t.toString())
//                }
//            })
//    }
//}

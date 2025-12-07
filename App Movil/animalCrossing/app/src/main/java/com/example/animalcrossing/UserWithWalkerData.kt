package com.example.animalcrossing

data class UserWithWalkerData (
    val user: com.example.animalcrossing.data.entity.userEntity?,
    val walker: com.example.animalcrossing.data.entity.walkerEntity?
) : java.io.Serializable
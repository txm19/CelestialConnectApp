package com.example.celestialconnect
class HelperClass {
    var name: String? = null
    var email: String? = null
    var username: String? = null
    var password: String? = null
    var followers: Int = 0
    var bio: String? = null
    var imageUrl: String? = null
    var caption: String? = null
//    var profilePic: String? = null

    constructor(
        name: String?,
        email: String?,
        username: String?,
        password: String?,
        followers: Int,
        bio: String?,
        imageUrl: String?,
        caption: String?//,
//        profilePic: String?
    ) {
        this.name = name
        this.email = email
        this.username = username
        this.password = password
        this.followers = followers
        this.bio = bio
        this.imageUrl = imageUrl
        this.caption = caption
//        this.profilePic = profilePic
    }

    constructor()
}

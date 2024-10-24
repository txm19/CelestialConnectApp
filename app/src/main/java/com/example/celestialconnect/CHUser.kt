package com.example.celestialconnect

class CHUser
{
    var name: String? = null
    var email: String? = null
    var uid: String? = null
    //var imageResourceID: Int = 0

    constructor()

    constructor(name: String?, email: String?, uid: String?)//,imageResourceID: Int)
    {
        this.name = name
        this.email = email
        this.uid = uid
        //this.imageResourceID = imageResourceID

    }
}
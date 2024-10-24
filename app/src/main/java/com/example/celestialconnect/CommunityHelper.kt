package com.example.celestialconnect

class CommunityHelper {

    var agenda: String? = null
    var guideline: String? = null
    var community_name: String? = null


    constructor(

        community_name: String?,
        agenda: String?,
        guideline: String?


    ) {

        this.community_name = community_name
        this.agenda = agenda
        this.guideline = guideline

    }

    constructor()
}

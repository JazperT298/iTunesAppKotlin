package com.example.itunesappkotlin.models

internal class User {

    var id: String? = null
    var username: String? = null
    var imageURL: String? = null
    var status: String? = null
    var search: String? = null

    constructor(id: String, username: String, imageURL: String, status: String, search: String) {
        this.id = id
        this.username = username
        this.imageURL = imageURL
        this.status = status
        this.search = search
    }

    constructor() {

    }
}

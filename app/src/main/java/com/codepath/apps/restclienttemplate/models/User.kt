package com.codepath.apps.restclienttemplate.models

import org.json.JSONObject

class User {

    var userId: Int = 0
    var name: String = ""
    var screenName: String = ""
    var location: String = ""
    var description: String = ""
    var publicImageURL: String = ""

    companion object {
        fun parseJSON (jsonObject: JSONObject) : User {
            val user = User()
            user.name = jsonObject.getString("name")
            user.screenName = jsonObject.getString("screen_name")
            user.location = jsonObject.getString("location")
            user.description = jsonObject.getString("description")
            user.publicImageURL = jsonObject.getString("profile_image_url_https")
            return user
        }
    }
}

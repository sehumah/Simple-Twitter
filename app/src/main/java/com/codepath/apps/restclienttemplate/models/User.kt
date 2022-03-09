package com.codepath.apps.restclienttemplate.models

import org.json.JSONObject

class User {
    /**
    "user": {
        "id": 6253282,
        "id_str": "6253282",
        "name": "Twitter API",
        "screen_name": "TwitterAPI",
        "location": "San Francisco, CA",
        "description": "The Real Twitter API. Tweets about API changes, service issues and our Developer Platform. Don't get an answer? It's on my website.",
        "url": "https://t.co/8IkCzCDr19",
        "entities": {
        "url": {
        "urls": [
        {
        "url": "https://t.co/8IkCzCDr19",
        "expanded_url": "https://developer.twitter.com",
        "display_url": "developer.twitter.com",
        "indices": [
        0,
        23
        ]
        }
        ]
        },
        "description": {
        "urls": []
        }
    },
    */
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

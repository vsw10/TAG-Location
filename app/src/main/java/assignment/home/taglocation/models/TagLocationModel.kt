package assignment.home.taglocation.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TagLocationModel : RealmObject() {
    @PrimaryKey
    var id: Int? = null

    var propertyName: String? = null
    var propertyCoordinates: String? = null
}



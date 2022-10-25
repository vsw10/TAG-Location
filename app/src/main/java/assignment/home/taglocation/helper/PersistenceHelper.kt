package assignment.home.taglocation.helper

import assignment.home.taglocation.models.TagLocationModel
import io.realm.Realm
import io.realm.RealmObject
import io.realm.kotlin.createObject

object PersistenceHelper {

    private val pendingObjects: MutableList<RealmObject> = mutableListOf()

    fun saveResult(
        obj: RealmObject, tagLocationModel: TagLocationModel?,
        onSuccess: Realm.Transaction.OnSuccess? = null, onError: Realm.Transaction.OnError? = null
    ) {
        synchronized(pendingObjects) {
            pendingObjects.add(obj)
        }
        process(tagLocationModel, onSuccess, onError)
    }

    private fun process(
        tagLocationModel: TagLocationModel?,
        onSuccess: Realm.Transaction.OnSuccess? = null, onError: Realm.Transaction.OnError? = null
    ) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransactionAsync(
                object : Realm.Transaction {
                    override fun execute(realm: Realm) {
                        var nextId: Int? =
                            (realm.where(TagLocationModel::class.java).max("id")?.toInt())
                        if (nextId == null) {
                            nextId = 1
                        } else {
                            nextId =
                                (realm.where(TagLocationModel::class.java).max("id")?.toInt()
                                    ?.plus(1))
                        }

                        val tagLocationModel = realm.createObject<TagLocationModel>(nextId)

                        //tagLocationModel.propertyName = tagLocationModel?.propertyName
                        var propertyName: String? = tagLocationModel.propertyName
                        var propertyCoordinates: String? = tagLocationModel.propertyCoordinates


                        tagLocationModel.propertyName = propertyName
                        tagLocationModel.propertyCoordinates = propertyCoordinates

                        realm.copyToRealmOrUpdate(tagLocationModel)
                    }
                },
                onSuccess, onError
            )
        }

    }
}
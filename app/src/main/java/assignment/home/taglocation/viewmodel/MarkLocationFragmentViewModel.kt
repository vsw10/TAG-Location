package assignment.home.taglocation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import assignment.home.taglocation.TagLocationApplication
import assignment.home.taglocation.helper.PersistenceHelper
import assignment.home.taglocation.models.TagLocationModel
import io.realm.RealmObject

class MarkLocationFragmentViewModel(application: Application) : AndroidViewModel(application) {


    private val app: TagLocationApplication by lazy { getApplication<TagLocationApplication>() }

    var tagLocationModelLiveData: MutableLiveData<TagLocationModel> =
        MutableLiveData<TagLocationModel>()

    fun saveRealmObjects(realmObject: RealmObject) {
        PersistenceHelper.saveResult(realmObject, tagLocationModel)
    }

    companion object {
        var tagLocationModel: TagLocationModel? = null
    }

    fun setTagLocationModel(tagLocationModel: TagLocationModel) {
        tagLocationModelLiveData.value = tagLocationModel
    }

    fun getTagLocationModel(): MutableLiveData<TagLocationModel> {
        return tagLocationModelLiveData

    }
}
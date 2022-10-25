package assignment.home.taglocation

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class TagLocationApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        initialiseRealm()

    }

    /**
     * Function to initialise the RealmDatabase
     */
    fun initialiseRealm() {

        Realm.init(this)

        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder().schemaVersion(1).deleteRealmIfMigrationNeeded().build()
        )

    }
}
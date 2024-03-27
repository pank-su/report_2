package data.di

import data.MapHelperRepository
import data.MapLoaderRepository
import data.RosClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.dsl.module

val dataModule = module {
    single {

        val ros = RosClient.ros
        CoroutineScope(Dispatchers.Default).launch{
            ros.connect()
        }
        ros
    }
    single {
        MapHelperRepository()
    }
    single {
        MapLoaderRepository(get())
    }
}
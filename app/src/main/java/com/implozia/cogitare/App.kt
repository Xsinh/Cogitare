package com.implozia.cogitare

import android.app.Application
import androidx.room.Room
import com.implozia.cogitare.data.NoteDao
import com.implozia.cogitare.data.NoteDatabase
import com.implozia.cogitare.data.repository.NoteRepository
import com.implozia.cogitare.data.repository.NoteRepositoryImpl
import com.implozia.cogitare.ui.MainViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class App : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@App))

        bind() from singleton { NoteDatabase(instance()) }
        bind() from singleton { instance<NoteDatabase>().noteDao() }

        bind<NoteRepository>() with singleton { NoteRepositoryImpl(instance()) }
        bind() from provider { MainViewModelFactory(instance()) }
    }
}
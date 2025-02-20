package com.example.eshopping.presentation.di

import com.example.eshopping.data.repoimpl.RepoImpl
import com.example.eshopping.domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UIModule {

    @Provides
    fun provideRepo(firestore: FirebaseFirestore,auth: FirebaseAuth,firebaseMessaging: FirebaseMessaging): Repo {
        return RepoImpl(firestore,auth,firebaseMessaging)
    }
}
package com.example.eshopping.data.repoimpl

import android.util.Log
import com.example.eshopping.common.CATEGORY
import com.example.eshopping.common.PRODUCT
import com.example.eshopping.common.ResultState
import com.example.eshopping.common.USER
import com.example.eshopping.data.model.Category
import com.example.eshopping.data.model.Product
import com.example.eshopping.data.model.UserData
import com.example.eshopping.domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepoImpl @Inject constructor(private val firestore: FirebaseFirestore,private val auth: FirebaseAuth): Repo {
    override suspend fun getAllCategory(): Flow<ResultState<List<Category>>> = callbackFlow {
        trySend(ResultState.Loading)
        firestore.collection(CATEGORY).get().addOnSuccessListener {
            val category = it.documents.mapNotNull {
                it.toObject(Category::class.java)
            }
            trySend(ResultState.Success(category))
            Log.d("TAGCategoryProduct", category.toString())
        }.addOnFailureListener {
            trySend(ResultState.Error(it.message.toString()))
        }
        awaitClose {
            close()
        }
    }

    override suspend fun getAllProduct(): Flow<ResultState<List<Product>>> = callbackFlow {
        trySend(ResultState.Loading)
        firestore.collection(PRODUCT).get().addOnSuccessListener {
            val product = it.documents.mapNotNull {
                it.toObject(Product::class.java)?.apply {
                    pId = it.id
                }
            }
//            product.forEachIndexed { index, product ->
//                product.pId = it.documents[index].id
//                product
//            }

            trySend(ResultState.Success(product))
            Log.d("TAGCategoryProduct", product.toString())
        }.addOnFailureListener {
            trySend(ResultState.Error(it.message.toString()))
        }
        awaitClose {
            close()
        }
    }

    override suspend fun registerUserWithEmailPass(userData: UserData): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        auth.createUserWithEmailAndPassword(userData.email,userData.password).addOnSuccessListener {
            firestore.collection(USER).document(it.user?.uid.toString()).set(userData).addOnSuccessListener {
                trySend(ResultState.Success("User registered successfully"))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.message.toString()))
            }
        }.addOnFailureListener {
            trySend(ResultState.Error(it.message.toString()))
        }
        awaitClose {
            close()
        }
    }

    override suspend fun signInUserWithEmailPass(
        email: String,
        password: String
    ): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
            trySend(ResultState.Success("User logged in successfully"))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.message.toString()))
        }
        awaitClose {
            close()
        }
    }

    override suspend fun getSpecificProduct(id: String): Flow<ResultState<Product>> = callbackFlow{
        trySend(ResultState.Loading)
        firestore.collection(PRODUCT).document(id).get().addOnSuccessListener {
            val product = it.toObject(Product::class.java)
            trySend(ResultState.Success(product!!))
            Log.d("TAGRepo", "getSpecificProduct: $product")
        }.addOnFailureListener {
            trySend(ResultState.Error(message = it.message.toString()))
        }
        awaitClose {
            close()
        }
    }

    override suspend fun getUserById(uid: String): Flow<ResultState<UserData>> = callbackFlow{
        trySend(ResultState.Loading)
        firestore.collection(USER).document(uid).get().addOnSuccessListener {
            val user = it.toObject(UserData::class.java)!!.apply {
                this.uid = it.id
            }
            trySend(ResultState.Success(user))

        }.addOnFailureListener {
            trySend(ResultState.Error(message = it.message.toString()))
        }
        awaitClose {
            close()
        }
    }

    override suspend fun updateUserData(updatedFields: Map<String, Any?>): Flow<ResultState<String>> = callbackFlow{
        trySend(ResultState.Loading)
        firestore.collection(USER).document(auth.currentUser?.uid.toString())
            .update(updatedFields).addOnSuccessListener {
                trySend(ResultState.Success("User updated successfully"))
            }.addOnFailureListener {
                trySend(ResultState.Error(message = it.message.toString()))
            }
        awaitClose {
            close()
        }
    }
}

package com.example.personalexpensetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.personalexpensetracker.model.Loan
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoanViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _loans = MutableStateFlow<List<Loan>>(emptyList())
    val loans: StateFlow<List<Loan>> = _loans

    fun loadLoans() {
        val userId = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val snapshot = db.collection("loan")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()

                val list = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(Loan::class.java)?.copy(id = doc.id)
                }
                _loans.value = list
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addLoan(loan: Loan) {
        val userId = auth.currentUser?.uid ?: return

        val newLoan = loan.copy(
            userId = userId
        )

        viewModelScope.launch {
            try {
                val docRef = db.collection("loan").document()
                val loanWithId = newLoan.copy(id = docRef.id)
                docRef.set(loanWithId).await()
                _loans.value = _loans.value + loanWithId
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateLoan(loan: Loan) {
        viewModelScope.launch {
            try {
                db.collection("loan").document(loan.id).set(loan).await()
                _loans.value = _loans.value.map { if (it.id == loan.id) loan else it }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteLoan(loan: Loan) {
        viewModelScope.launch {
            try {
                db.collection("loan").document(loan.id).delete().await()
                _loans.value = _loans.value.filter { it.id != loan.id }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


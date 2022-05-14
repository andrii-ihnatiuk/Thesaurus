package com.opsu.thesaurus.models

import java.io.Serializable

class DataModels {

    data class SetModel(val title: String, val numOfTerms: Int, val createdBy: String, val terms: ArrayList<TermModel>) : Serializable

    data class TermModel(var term: String, var definition: String) : Serializable
}
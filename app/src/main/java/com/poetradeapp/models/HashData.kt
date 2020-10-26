package com.poetradeapp.models

import com.poetradeapp.models.enums.HashType

data class HashData(
    val type: HashType,
    var hash: Int,
    var isUpdated: Boolean
)
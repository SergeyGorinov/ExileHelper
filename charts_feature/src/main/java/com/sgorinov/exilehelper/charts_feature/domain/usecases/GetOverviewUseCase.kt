package com.sgorinov.exilehelper.charts_feature.domain.usecases

import com.sgorinov.exilehelper.charts_feature.domain.IFeatureRepository

internal class GetOverviewUseCase(private val repository: IFeatureRepository) {

    fun execute() = repository.overviewData
}
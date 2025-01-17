package com.example.officemanagerapp.ui.services.market.workers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.officemanagerapp.models.WorkerPreview
import com.example.officemanagerapp.network.Resource
import com.example.officemanagerapp.repository.ServicesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketWorkersViewModel @Inject constructor(
    private val repository: ServicesRepository
) : ViewModel() {

    private val _marketPreviewWorkers: MutableLiveData<Resource<List<WorkerPreview>>> = MutableLiveData()
    val marketPreviewWorkers: LiveData<Resource<List<WorkerPreview>>>
        get() = _marketPreviewWorkers

    fun getPreviewWorkers() = viewModelScope.launch {
        _marketPreviewWorkers.value = Resource.Loading
        _marketPreviewWorkers.value = repository.getMarketPreviewWorkers()
    }

    val previewWorkers: List<WorkerPreview> = listOf(
        WorkerPreview(1, "Василий", "Петров", "", "2000", 4.0, "2 года"),
        WorkerPreview(2, "Александр", "Васильев", "", "1400", 3.0, "1.5 года"),
        WorkerPreview(3, "Виталий", "Сачков", "", "1800", 4.0, "3 года"),
        WorkerPreview(4, "Алексей", "Мухов", "", "3000", 5.0, "2.5 года"),
        WorkerPreview(5, "Светлана", "Баламова", "", "1500", 3.0, "1 год"),
        WorkerPreview(6, "Михаил", "Абрамова", "", "800", 2.0, "2 месяца"),
    )

}
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NutritionViewModel : ViewModel() {
    private val _nutritionData = MutableLiveData<List<String>>()
    val nutritionData: LiveData<List<String>> get() = _nutritionData

    fun setNutritionData(nutrition: List<String>) {
        _nutritionData.value = nutrition
    }
}
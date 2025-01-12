package com.example.masterproject.screens.exchangerate.edititem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.masterproject.model.PairCoinsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditItemViewModel @Inject constructor(
    private val pairsCoinsRepository: PairCoinsRepository
): ViewModel() {

    private val _stateFlow = MutableStateFlow(ScreenState())
    val stateFlow: StateFlow<ScreenState> = _stateFlow

    private val _exitChannel = Channel<Unit>()
    val exitChannel: ReceiveChannel<Unit> = _exitChannel

    fun addPair(pair: String){
        viewModelScope.launch {
            _stateFlow.update { it.copy(isEditInProgress = true) }
            pairsCoinsRepository.addPairInList(pair)
            _exitChannel.send(Unit)
        }
    }

    fun removePair(pair: String){
        viewModelScope.launch {
            var id: Long = -1

            for (i in pairsCoinsRepository.getPairList().value){
                if (i.pair == pair){
                    id = i.id
                }
            }
            if (id == (-1).toLong()) return@launch

            _stateFlow.update { it.copy(isEditInProgress = true) }
            pairsCoinsRepository.removePairFromList(id)
            _exitChannel.send(Unit)
        }
    }

    data class ScreenState(
        private val isEditInProgress: Boolean = false
    ){
        val isTextInputEnabled: Boolean get() = !isEditInProgress
        val isProgressVisible: Boolean get() = isEditInProgress
        fun isButtonEnabled(input: String): Boolean {
            return input.isNotBlank() && !isEditInProgress
        }
    }
}
package com.ottego.saathidaar.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ottego.saathidaar.Model.NewMatchesModel;

import java.util.List;

public class NewMatchViewModel extends ViewModel {
   public MutableLiveData<List<NewMatchesModel>> _list = new MutableLiveData<>();
   public LiveData<List<NewMatchesModel>> list = _list;

}

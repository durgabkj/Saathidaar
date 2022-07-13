package com.ottego.saathidaar.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ottego.saathidaar.Model.InboxModel;
import com.ottego.saathidaar.Model.NewMatchesModel;

import java.util.List;

public class InboxViewModel extends ViewModel {
    public MutableLiveData<List<InboxModel>> _list = new MutableLiveData<>();
    public LiveData<List<InboxModel>> list1 = _list;
}

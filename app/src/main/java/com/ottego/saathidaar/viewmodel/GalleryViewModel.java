package com.ottego.saathidaar.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ottego.saathidaar.Model.ImageModel;
import com.ottego.saathidaar.Model.NewMatchesModel;

import java.util.List;

public class GalleryViewModel extends ViewModel {
    public MutableLiveData<List<ImageModel>> _list = new MutableLiveData<>();
    public LiveData<List<ImageModel>> list = _list;
}

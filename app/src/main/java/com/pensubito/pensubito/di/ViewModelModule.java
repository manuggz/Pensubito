package com.pensubito.pensubito.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.pensubito.pensubito.viewmodel.PensubitoModelFactory;
import com.pensubito.pensubito.vm.TrimestreListViewModel;
import com.pensubito.pensubito.vm.TrimestreViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TrimestreListViewModel.class)
    abstract ViewModel bindTrimestreListViewModel(TrimestreListViewModel repoViewModel);
    @Binds
    @IntoMap
    @ViewModelKey(TrimestreViewModel.class)
    abstract ViewModel bindTrimestreViewModel(TrimestreViewModel trimestreViewModel);
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(PensubitoModelFactory factory);
}

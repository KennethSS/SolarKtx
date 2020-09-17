package com.solar.solarktx

import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

operator fun CompositeDisposable.plusAssign(d: Disposable) {
    this.add(d)
}

fun <T> Single<T>.base() =
        subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.base() =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Flowable<T>.base() =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun Completable.base() =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.observable(state: MutableLiveData<NetworkState<T>>) =
    subscribeOn(Schedulers.io())
        .doOnSubscribe { state.value = NetworkState.Loading() }
        .doOnTerminate { state.value = NetworkState.Init() }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { state.value = NetworkState.Success(it) },
            { state.value = NetworkState.Error(it) }
        )

fun <T> Single<T>.singleNetwork(state: MutableLiveData<NetworkState<T>>) =
        subscribeOn(Schedulers.io())
                .doAfterTerminate { state.value = NetworkState.Init() }
                .doOnSubscribe { state.value = NetworkState.Loading() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { state.value = NetworkState.Success(it) },
                        { state.value = NetworkState.Error(it) }
                )

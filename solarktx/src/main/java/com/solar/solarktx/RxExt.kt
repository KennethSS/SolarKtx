package com.solar.solarktx

import androidx.lifecycle.MutableLiveData
import io.reactivex.*
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
        .doOnSubscribe { state.value = NetworkState.Loading }
        .doOnTerminate { state.value = NetworkState.Init }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { state.value = NetworkState.Success(it) },
            { state.value = NetworkState.Error(it) }
        )

fun <T> Single<T>.singleNetwork(state: MutableLiveData<NetworkState<T>>) =
    subscribeOn(Schedulers.io())
        .doAfterTerminate { state.value = NetworkState.Init }
        .doOnSubscribe { state.value = NetworkState.Loading }
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { state.value = NetworkState.Success(it) },
            { state.value = NetworkState.Error(it) }
        )

fun <T,R> Single<T>.singleNetworkWithMapper(state: MutableLiveData<NetworkState<R>>, map: ((t: T) -> R)) =
    subscribeOn(Schedulers.io())
        .doAfterTerminate { state.postValue(NetworkState.Init) }
        .doOnSubscribe { state.postValue(NetworkState.Loading) }
        .subscribeOn(Schedulers.computation())
        .map(map)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            { state.postValue(NetworkState.Success(it)) },
            { state.postValue(NetworkState.Error(it)) }
        )
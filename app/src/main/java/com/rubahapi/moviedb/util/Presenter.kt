package com.rubahapi.footballapps.util

import android.support.v4.app.Fragment

interface Presenter<T:Fragment>{
    fun onAttach(view: T)
    fun onDetach()
}
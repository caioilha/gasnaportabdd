package com.gasnaporta.gasnaporta.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
class Supplier(val loc: @RawValue Loc, val priceP2: Double, val priceP13: Double, val priceP45: Double, val _id: String, val address: String, val name: String, val scoresAvg: Double, val phone: String, var distance: Double, var isFavorite: Boolean) : Parcelable
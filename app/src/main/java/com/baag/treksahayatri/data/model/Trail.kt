package com.baag.treksahayatri.data.model

import android.os.Parcel
import android.os.Parcelable

data class Trail(
	val id: Int,
	val name: String,
	val description: String,
	val region: String,
	val distance_km: String,
	val duration_days: String,
	val elevation: String,
	val difficulty: String,
	val start_point: LatLng,
	val end_point: LatLng,
	val images: List<Image>
) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readInt(),
		parcel.readString() ?: "",
		parcel.readString() ?: "",
		parcel.readString() ?: "",
		parcel.readString() ?: "",
		parcel.readString() ?: "",
		parcel.readString() ?: "",
		parcel.readString() ?: "",
		parcel.readParcelable(LatLng::class.java.classLoader) ?: LatLng(0.0, 0.0),
		parcel.readParcelable(LatLng::class.java.classLoader) ?: LatLng(0.0, 0.0),
		parcel.createTypedArrayList(Image.CREATOR) ?: emptyList()
	)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(id)
		parcel.writeString(name)
		parcel.writeString(description)
		parcel.writeString(region)
		parcel.writeString(distance_km)
		parcel.writeString(duration_days)
		parcel.writeString(elevation)
		parcel.writeString(difficulty)
		parcel.writeParcelable(start_point, flags)
		parcel.writeParcelable(end_point, flags)
		parcel.writeTypedList(images)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Trail> {
		override fun createFromParcel(parcel: Parcel): Trail {
			return Trail(parcel)
		}

		override fun newArray(size: Int): Array<Trail?> {
			return arrayOfNulls(size)
		}
	}
}

data class LatLng(val lat: Double, val lng: Double) : Parcelable {
	constructor(parcel: Parcel) : this(parcel.readDouble(), parcel.readDouble())

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeDouble(lat)
		parcel.writeDouble(lng)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<LatLng> {
		override fun createFromParcel(parcel: Parcel): LatLng {
			return LatLng(parcel)
		}

		override fun newArray(size: Int): Array<LatLng?> {
			return arrayOfNulls(size)
		}
	}
}

data class Image(val id: Int, val trail: Int, val image: String) : Parcelable {
	constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readInt(), parcel.readString() ?: "")

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(id)
		parcel.writeInt(trail)
		parcel.writeString(image)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Image> {
		override fun createFromParcel(parcel: Parcel): Image {
			return Image(parcel)
		}

		override fun newArray(size: Int): Array<Image?> {
			return arrayOfNulls(size)
		}
	}
}

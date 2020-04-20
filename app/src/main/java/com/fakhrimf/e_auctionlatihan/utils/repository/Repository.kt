package com.fakhrimf.e_auctionlatihan.utils.repository

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.fakhrimf.e_auctionlatihan.R
import com.fakhrimf.e_auctionlatihan.model.BidModel
import com.fakhrimf.e_auctionlatihan.model.DatabaseMessageModel
import com.fakhrimf.e_auctionlatihan.model.ItemModel
import com.fakhrimf.e_auctionlatihan.model.ProfileModel
import com.fakhrimf.e_auctionlatihan.utils.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import org.joda.time.DateTime
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Repository {
    companion object {
        fun newInstance() = Repository()
        private val database = FirebaseDatabase.getInstance()
        private val storage = FirebaseStorage.getInstance()
        val reference = database.reference
        val storageReference = storage.reference
        fun getChild(child: String) = reference.child(child)
        fun getSharedPreferences(context: Context): SharedPreferences? = context.getSharedPreferences(APP_SHARED_PREFERENCE, MODE_PRIVATE)
        fun getCurrentUser(context: Context): ProfileModel? {
            val gson = Gson()
            val profileJson = getSharedPreferences(context)?.getString(PROFILE_SHARED_KEY, null)
            return if (profileJson != null) gson.fromJson(profileJson, ProfileModel::class.java)
            else null
        }

        fun getUserAsString(profileModel: ProfileModel): String {
            val gson = Gson()
            return gson.toJson(profileModel) as String
        }

        fun getUserAsModel(profileJson: String): ProfileModel {
            return Gson().fromJson(profileJson, ProfileModel::class.java)
        }
    }

    fun registerProfile(context: Context, profileModel: ProfileModel): MutableLiveData<DatabaseMessageModel> {
        val liveData = MutableLiveData<DatabaseMessageModel>()
        profileModel.id = "${reference.push().key}"
        getChild(DB_CHILD_USER).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                liveData.value = DatabaseMessageModel(false, p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (i in p0.children) {
                    val model = i.getValue(ProfileModel::class.java)
                    if (model?.username == profileModel.username) {
                        liveData.value = DatabaseMessageModel(false, context.getString(R.string.username_taken))
                        break
                    }
                    if (model?.email == profileModel.email) {
                        liveData.value = DatabaseMessageModel(false, context.getString(R.string.email_registered))
                        break
                    }
                    getChild(DB_CHILD_USER).child(profileModel.id).setValue(profileModel) { error, _ ->
                        if (error != null) {
                            liveData.value = DatabaseMessageModel(false, error.message)
                        } else {
                            liveData.value = DatabaseMessageModel(true, DB_SET_VALUE_SUCCESS)
                            storeUser(context, profileModel)
                        }
                    }
                    break
                }
            }
        })
        return liveData
    }

    fun editProfile(context: Context, profileModel: ProfileModel, path: Uri?): MutableLiveData<DatabaseMessageModel> {
        val liveData = MutableLiveData<DatabaseMessageModel>()
        val ref = storageReference.child(STORAGE_IMAGES).child(profileModel.id).child(profileModel.username)
        if (path != null) {
            ref.putFile(path).apply {
                addOnSuccessListener {
                    ref.downloadUrl.apply {
                        addOnSuccessListener {
                            profileModel.image = it.toString()
                            getChild(DB_CHILD_USER).child(profileModel.id).setValue(profileModel).addOnSuccessListener {
                                liveData.value = DatabaseMessageModel(true, DB_SET_VALUE_SUCCESS)
                                storeUser(context, profileModel)
                            }
                        }
                        addOnFailureListener {
                            liveData.value = DatabaseMessageModel(false, "${it.message}")
                        }
                    }
                }
                addOnFailureListener {
                    liveData.value = DatabaseMessageModel(false, "${it.message}")
                }
            }
        } else {
            getChild(DB_CHILD_USER).child(profileModel.id).setValue(profileModel).addOnSuccessListener {
                liveData.value = DatabaseMessageModel(true, DB_SET_VALUE_SUCCESS)
                storeUser(context, profileModel)
            }
        }

        return liveData
    }

    private fun getProfileData(): MutableLiveData<ArrayList<ProfileModel>> {
        val liveData = MutableLiveData<ArrayList<ProfileModel>>()
        val list = ArrayList<ProfileModel>()

        getChild(DB_CHILD_USER).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                throw Throwable(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                for (data in p0.children) {
                    val model = data.getValue(ProfileModel::class.java)
                    model?.let {
                        list.add(model)
                    }
                }
                liveData.value = list
            }
        })
        return liveData
    }

    fun getProfileData(id: String): MutableLiveData<ProfileModel> {
        val liveData = MutableLiveData<ProfileModel>()

        getChild(DB_CHILD_USER).orderByChild(DB_CHILD_ID).equalTo(id).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                throw Throwable(p0.message)
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    val model = data.getValue(ProfileModel::class.java)
                    model?.let {
                        liveData.value = it
                    }
                }
            }
        })
        return liveData
    }

    private fun storeUser(context: Context, profileModel: ProfileModel) {
        val sharedPref = context.getSharedPreferences(APP_SHARED_PREFERENCE, MODE_PRIVATE)
        val editor = sharedPref.edit()
        val gson = Gson()
        val json = gson.toJson(profileModel) as String
        editor.putString(PROFILE_SHARED_KEY, json)
        editor.apply()
    }

    fun login(context: Context, lifecycleOwner: LifecycleOwner, profileModel: ProfileModel): MutableLiveData<Boolean> {
        val liveData = MutableLiveData<Boolean>()
        var check = false
        getProfileData().observe(lifecycleOwner, Observer<ArrayList<ProfileModel>> {
            for (i in it) {
                if (profileModel.username == i.username && profileModel.password == i.password) {
                    check = true
                    storeUser(context, i)
                }
            }
            liveData.value = check
        })

        return liveData
    }

    fun getAdminCode(): MutableLiveData<String> {
        val liveData = MutableLiveData<String>()
        getChild(DB_CHILD_CREDENTIAL).child(DB_CHILD_ADMIN_CODE).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                liveData.value = ERROR_GET_CODE
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (i in p0.children) {
                    val code = i.getValue(String::class.java)
                    liveData.value = code
                }
            }
        })
        return liveData
    }

    fun addRequest(itemModel: ItemModel, path: Uri): MutableLiveData<DatabaseMessageModel> {
        val liveData = MutableLiveData<DatabaseMessageModel>()
        itemModel.id = "${reference.push().key}"
        itemModel.status = WAITING_STATUS
        itemModel.dateRequested = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
        val ref = storageReference.child(STORAGE_IMAGES).child(itemModel.id).child(itemModel.title)
        ref.putFile(path).apply {
            addOnSuccessListener {
                ref.downloadUrl.apply {
                    addOnSuccessListener {
                        itemModel.image = it.toString()
                        getChild(DB_CHILD_REQUEST).child(itemModel.id).setValue(itemModel) { error, _ ->
                            if (error != null) {
                                liveData.value = DatabaseMessageModel(false, error.message)
                            } else {
                                liveData.value = DatabaseMessageModel(true, DB_SET_VALUE_SUCCESS)
                            }
                        }
                    }
                    addOnFailureListener {
                        liveData.value = DatabaseMessageModel(false, "${it.message}")
                    }
                }
            }
            addOnFailureListener {
                liveData.value = DatabaseMessageModel(false, "${it.message}")
            }
        }
        return liveData
    }

    fun getPendingRequest(context: Context): MutableLiveData<ArrayList<ItemModel>> {
        val liveData = MutableLiveData<ArrayList<ItemModel>>()
        val list = ArrayList<ItemModel>()
        getChild(DB_CHILD_REQUEST).orderByChild(DB_CHILD_STATUS).equalTo(WAITING_STATUS).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toasty.error(context, p0.message, Toast.LENGTH_LONG, true).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                for (i in p0.children) {
                    val model = i.getValue(ItemModel::class.java)
                    model?.let { list.add(it) }
                }
                liveData.value = list
            }
        })
        return liveData
    }

    fun getDeclinedRequest(context: Context): MutableLiveData<ArrayList<ItemModel>> {
        val liveData = MutableLiveData<ArrayList<ItemModel>>()
        val list = ArrayList<ItemModel>()
        getChild(DB_CHILD_REQUEST).orderByChild(DB_CHILD_STATUS).equalTo(DECLINED_STATUS).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toasty.error(context, p0.message, Toast.LENGTH_LONG, true).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                for (i in p0.children) {
                    val model = i.getValue(ItemModel::class.java)
                    model?.let { list.add(it) }
                }
                liveData.value = list
            }
        })
        return liveData
    }

    fun getRequestHome(context: Context): MutableLiveData<ArrayList<ItemModel>> {
        val liveData = MutableLiveData<ArrayList<ItemModel>>()
        val list = ArrayList<ItemModel>()
        val today = DateTime()
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        getChild(DB_CHILD_REQUEST).orderByChild(DB_CHILD_STATUS).equalTo(APPROVED_STATUS).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toasty.error(context, p0.message, Toast.LENGTH_LONG, true).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                for (i in p0.children) {
                    val model = i.getValue(ItemModel::class.java)
                    if (today.isBefore(DateTime(sdf.parse(model!!.due))) || today.isEqual(DateTime(sdf.parse(model.due)))) {
                        model.let { list.add(it) }
                    }
                }
                list.reverse()
                liveData.value = list
            }
        })
        return liveData
    }

    fun getRequestReport(context: Context): MutableLiveData<ArrayList<ItemModel>> {
        val liveData = MutableLiveData<ArrayList<ItemModel>>()
        val list = ArrayList<ItemModel>()
        val today = DateTime()
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        getChild(DB_CHILD_REQUEST).orderByChild(DB_CHILD_STATUS).equalTo(APPROVED_STATUS).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toasty.error(context, p0.message, Toast.LENGTH_LONG, true).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                for (i in p0.children) {
                    val model = i.getValue(ItemModel::class.java)
                    if (today.isAfter(DateTime(sdf.parse(model!!.due)))) {
                        model.let { list.add(it) }
                    }
                }
                list.reverse()
                liveData.value = list
            }
        })
        return liveData
    }

    fun getApprovedRequest(context: Context): MutableLiveData<ArrayList<ItemModel>> {
        val liveData = MutableLiveData<ArrayList<ItemModel>>()
        val list = ArrayList<ItemModel>()
        getChild(DB_CHILD_REQUEST).orderByChild(DB_CHILD_STATUS).equalTo(APPROVED_STATUS).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toasty.error(context, p0.message, Toast.LENGTH_LONG, true).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                for (i in p0.children) {
                    val model = i.getValue(ItemModel::class.java)
                    model?.let { list.add(it) }
                }
                liveData.value = list
            }
        })
        return liveData
    }

    private fun getAllRequest(context: Context): MutableLiveData<ArrayList<ItemModel>> {
        val liveData = MutableLiveData<ArrayList<ItemModel>>()
        val list = ArrayList<ItemModel>()
        getChild(DB_CHILD_REQUEST).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toasty.error(context, p0.message, Toast.LENGTH_LONG, true).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                for (i in p0.children) {
                    val model = i.getValue(ItemModel::class.java)
                    model?.let { list.add(it) }
                }
                liveData.value = list
            }
        })
        return liveData
    }

    fun acceptRequest(items: ArrayList<ItemModel>, due: String): MutableLiveData<DatabaseMessageModel> {
        val liveData = MutableLiveData<DatabaseMessageModel>()
        for (i in items) {
            i.due = due
            i.status = APPROVED_STATUS
            getChild(DB_CHILD_REQUEST).child(i.id).setValue(i) { p0, _ ->
                if (p0 != null) {
                    liveData.value = DatabaseMessageModel(false, p0.message)
                } else {
                    liveData.value = DatabaseMessageModel(true, DB_SET_VALUE_SUCCESS)
                }
            }
        }
        return liveData
    }

    fun declineRequest(items: ArrayList<ItemModel>): MutableLiveData<DatabaseMessageModel> {
        val liveData = MutableLiveData<DatabaseMessageModel>()
        for (i in items) {
            i.status = DECLINED_STATUS
            getChild(DB_CHILD_REQUEST).child(i.id).setValue(i) { p0, _ ->
                if (p0 != null) {
                    liveData.value = DatabaseMessageModel(false, p0.message)
                } else {
                    liveData.value = DatabaseMessageModel(true, DB_SET_VALUE_SUCCESS)
                }
            }
        }
        return liveData
    }

    fun getHighestBid(itemModel: ItemModel): MutableLiveData<String> {
        val liveData = MutableLiveData<String>()
        getChild(DB_CHILD_REQUEST).orderByChild(DB_CHILD_ID).equalTo(itemModel.id).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for (i in p0.children) {
                    val model = i.getValue(ItemModel::class.java)
                    if (model?.latestBid != null && model.latestBid != "") {
                        liveData.value = model.latestBid
                    } else {
                        liveData.value = NO_BID
                    }
                }
            }
        })
        return liveData
    }

    fun getBids(itemModel: ItemModel): MutableLiveData<ArrayList<BidModel>> {
        val liveData = MutableLiveData<ArrayList<BidModel>>()
        val list = ArrayList<BidModel>()
        getChild(DB_CHILD_REQUEST).child(itemModel.id).child(DB_CHILD_BIDS).orderByChild("bid").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for (i in p0.children) {
                    val model = i.getValue(BidModel::class.java)
                    model?.let { list.add(it) }
                }
                list.reverse()
                liveData.value = list
            }
        })
        return liveData
    }

    fun bid(context: Context, itemModel: ItemModel, bidModel: BidModel): MutableLiveData<DatabaseMessageModel> {
        val liveData = MutableLiveData<DatabaseMessageModel>()
        val key = reference.push().key
        bidModel.id = "$key"
        getChild(DB_CHILD_REQUEST).child(itemModel.id).child(DB_HIGHEST_BIDDER).setValue(getCurrentUser(context)?.username) { error, _ ->
            if (error != null) {
                liveData.value = DatabaseMessageModel(false, error.message)
            }
        }
        getChild(DB_CHILD_REQUEST).child(itemModel.id).child(DB_LATEST_BID).setValue(bidModel.bid) { error, _ ->
            if (error != null) {
                liveData.value = DatabaseMessageModel(false, error.message)
            }
        }
        getChild(DB_CHILD_REQUEST).child(itemModel.id).child(DB_CHILD_BIDS).child(bidModel.id).setValue(bidModel) { p0, _ ->
            if (p0 != null) {
                liveData.value = DatabaseMessageModel(false, p0.message)
            } else {
                liveData.value = DatabaseMessageModel(true, DB_SET_VALUE_SUCCESS)
            }
        }
        return liveData
    }

    fun getMyBids(lifecycleOwner: LifecycleOwner, context: Context): MutableLiveData<ArrayList<ItemModel>> {
        val userId = getCurrentUser(context)?.id
        val liveData = MutableLiveData<ArrayList<ItemModel>>()
        val list = ArrayList<ItemModel>()

        getApprovedRequest(context).observe(lifecycleOwner, Observer {
            list.clear()
            for (i in it) {
                getBids(i).observe(lifecycleOwner, Observer { bids ->
                    for (o in bids) {
                        if (o.bidderId == userId) {
                            i.myBid = o.bid
                            list.add(i)
                            break
                        }
                    }
                    liveData.value = list
                })
            }
        })
        return liveData
    }

    fun getMyRequestedItems(lifecycleOwner: LifecycleOwner, context: Context): MutableLiveData<ArrayList<ItemModel>> {
        val userId = getCurrentUser(context)?.id
        val liveData = MutableLiveData<ArrayList<ItemModel>>()
        val list = ArrayList<ItemModel>()
        getAllRequest(context).observe(lifecycleOwner, Observer {
            for (i in it) {
                val itemUserId = Gson().fromJson(i.user, ProfileModel::class.java).id
                if (itemUserId == userId) {
                    list.add(i)
                }
            }
            liveData.value = list
        })
        return liveData
    }

    fun searchHome(query: String) : MutableLiveData<ArrayList<ItemModel>> {
        val liveData = MutableLiveData<ArrayList<ItemModel>>()
        val list = ArrayList<ItemModel>()
        val today = DateTime()
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        getChild(DB_CHILD_REQUEST).orderByChild(DB_CHILD_STATUS).equalTo(APPROVED_STATUS).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()
                for (i in p0.children) {
                    val model = i.getValue(ItemModel::class.java)
                    if (today.isBefore(DateTime(sdf.parse(model!!.due))) || today.isEqual(DateTime(sdf.parse(model.due)))) {
                        if (model.title.contains(query, true)) model.let { list.add(it) }
                    }
                }
                list.reverse()
                liveData.value = list
            }
        })
        return liveData
    }
}
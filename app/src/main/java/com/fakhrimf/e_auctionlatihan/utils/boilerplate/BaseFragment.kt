package com.fakhrimf.e_auctionlatihan.utils.boilerplate

import android.view.Menu
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.fakhrimf.e_auctionlatihan.R
import es.dmoral.toasty.Toasty
import kotlin.concurrent.timer

open class BaseFragment : Fragment() {
    fun setMenuItemVisibility(menu: Menu, visible: Boolean) {
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            if (menuItem != menu.findItem(R.id.search)) menuItem.isVisible = visible
            requireActivity().invalidateOptionsMenu()
        }
    }

    fun showInfo(text: String) {
        Toasty.info(requireContext(), text, Toast.LENGTH_LONG, true).show()
    }

    /*fun showInfoAlert(text: String) {
        SweetAlertDialog(context).apply {
            titleText = getString(R.string.success)
            contentText = text
            show()
        }
    }*/

    fun showSuccess(text: String) {
        Toasty.success(requireContext(), text, Toast.LENGTH_LONG, true).show()
    }

    /*fun showSuccessAlert(text: String) {
        SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE).apply {
            titleText = getString(R.string.success)
            contentText = text
            show()
        }
    }*/

    fun showWarning(text: String) {
        Toasty.warning(requireContext(), text, Toast.LENGTH_LONG, true).show()
    }

    fun showError(text: String) {
        Toasty.error(requireContext(), text, Toast.LENGTH_LONG, true).show()
    }
}
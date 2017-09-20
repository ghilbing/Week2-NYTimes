package fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.codepath.nytimesapp.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

import models.Filters;


public class DatePickerFragment extends DialogFragment {


    DatePickerDialog.OnDateSetListener listener;

    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = this.getArguments();
        GregorianCalendar beginDate = (GregorianCalendar) args.getSerializable("calendar");
        if (beginDate == null) {
            beginDate = new GregorianCalendar();
        }

        int year = beginDate.get(Calendar.YEAR);
        int month = beginDate.get(Calendar.MONTH);
        int day = beginDate.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }

    public interface OnDateSetListener {
    }
}

package fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.codepath.nytimesapp.R;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import models.Filters;

import static com.example.codepath.nytimesapp.R.id.btnSave;

public class FiltersFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Bind(R.id.etBeginDate)
    EditText beginDate;
    @Bind(R.id.spSortOrder)
    Spinner spinnerOrder;
    @Bind(R.id.cbArts)
    CheckBox arts;
    @Bind(R.id.cbFashion_and_Stlye)
    CheckBox fashion;
    @Bind(R.id.cbSports)
    CheckBox sports;
    @Bind(btnSave)
    Button save;
    @Bind(R.id.btnCancel)
    Button cancel;

    Filters filters;
    DatePickerFragment mDatePickerFragment;

    private EditFiltersDialogListener listener;

    public FiltersFragment() {
        this.listener =  null;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        filters.beginDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        beginDate.setText((monthOfYear + 1) + " / " + dayOfMonth + " / " + year);
        mDatePickerFragment.dismiss();
    }

    public interface EditFiltersDialogListener {
        void onFinishedEditFilters(Filters filters);
    }

    public void setEditorFiltersDialogListener(EditFiltersDialogListener listener) {
        this.listener = listener;
    }

    public static FiltersFragment newInstance(Filters filters) {
        FiltersFragment fragment = new FiltersFragment();
        Bundle args = new Bundle();
        args.putParcelable("filters", Parcels.wrap(filters));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filters, container);



    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        Bundle arguments = getArguments();


        filters = Parcels.unwrap(arguments.getParcelable("filters"));


        getDialog().setTitle("Edit Filters");
        setupDatePickerClickListener();
        setupSaveButton();
        setupCancelButton();
        setupFilters();
    }

    private void setupFilters() {
        if (this.filters.beginDate != null) {
            int monthOfYear = this.filters.beginDate.get(Calendar.MONTH);
            int dayOfMonth = this.filters.beginDate.get(Calendar.DAY_OF_MONTH);
            int year = this.filters.beginDate.get(Calendar.YEAR);
            beginDate.setText((monthOfYear + 1) + " / " + dayOfMonth + " / " + year);
        }
        setSpinner(spinnerOrder, filters.sortOrder);
        if (filters.newsDesks != null) {
            arts.setChecked(filters.newsDesks.contains("Arts"));
            fashion.setChecked(filters.newsDesks.contains("Fashion"));
            sports.setChecked(filters.newsDesks.contains("Sports"));
        }
    }

    private void setSpinner(Spinner spinner, String value) {
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                index = i;
            }
        }
        spinner.setSelection(index);
    }

    private void setupDatePickerClickListener() {
        final DatePickerDialog.OnDateSetListener listener = this;
        beginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                mDatePickerFragment = new DatePickerFragment();
                Bundle args = new Bundle();
                args.putSerializable("calendar", filters.beginDate);
                mDatePickerFragment.setArguments(args);
                mDatePickerFragment.setListener(listener);
                mDatePickerFragment.show(fm, "fragment_select_begin_date");
            }
        });
    }

    private Set<String> buildNewsDesksSet() {
        Set<String> newsDesks = new HashSet<>();
        if (arts.isChecked()) { newsDesks.add("Arts"); }
        if (fashion.isChecked()) { newsDesks.add("Fashion"); }
        if (sports.isChecked()) { newsDesks.add("Sports"); }
        return newsDesks;
    }

    private void setupSaveButton() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    String sortOrderString = spinnerOrder.getSelectedItem().toString();
                    Set<String> newsDesks = buildNewsDesksSet();
                    Filters f = new Filters(sortOrderString, newsDesks, filters.beginDate, filters.query);
                    listener.onFinishedEditFilters(f);
                    Log.d("FILTER", f.toString());
                }
            }
        });
    }

    private void setupCancelButton(){
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}

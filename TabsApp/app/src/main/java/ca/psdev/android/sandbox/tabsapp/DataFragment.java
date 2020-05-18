package ca.psdev.android.sandbox.tabsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.fragment.app.ListFragment;

public class DataFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(),
                android.R.layout.simple_list_item_1, new String[]{"Data 1", "Data 2", "Data 3"});
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

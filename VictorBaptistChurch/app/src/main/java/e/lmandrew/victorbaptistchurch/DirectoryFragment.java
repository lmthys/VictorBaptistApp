package e.lmandrew.victorbaptistchurch;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class DirectoryFragment extends Fragment {

    Activity context;

    public DirectoryFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.directory_fragment, container, false);

        context = getActivity();

        return rootView;
    }
}


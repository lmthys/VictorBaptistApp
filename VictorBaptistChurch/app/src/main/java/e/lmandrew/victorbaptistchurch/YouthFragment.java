package e.lmandrew.victorbaptistchurch;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class YouthFragment extends Fragment {

    Activity context;

    public YouthFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.youth_fragement, container, false);

        context = getActivity();

        return rootView;
    }
}



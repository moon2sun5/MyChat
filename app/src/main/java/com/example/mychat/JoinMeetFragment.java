package com.example.mychat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JoinMeetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoinMeetFragment extends Fragment {

    private String sampleToken ="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcGlrZXkiOiI0MjYxZjlhMi0yNTRjLTRmNDYtYjJlMC0zYWJlZGJiZWRjNjAiLCJwZXJtaXNzaW9ucyI6WyJhbGxvd19qb2luIl0sImlhdCI6MTcxNTU0NjAwNiwiZXhwIjoxNzE4MTM4MDA2fQ.RbLbJiLR8iTu6MZGPFeO87Ir3quXnqc9INs2NBa8Kxo";

    private static final int PERMISSION_REQ_ID = 22;

    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public JoinMeetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JoinMeetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JoinMeetFragment newInstance(String param1, String param2) {
        JoinMeetFragment fragment = new JoinMeetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_meet, container, false);
//        setContentView(R.layout.fragment_join_meet);

        final Button btnCreate = view.findViewById(R.id.btnCreateMeeting);
        final Button btnJoin = view.findViewById(R.id.btnJoinMeeting);
        final EditText etMeetingId = view.findViewById(R.id.etMeetingId);

        btnCreate.setOnClickListener(v -> {
            createMeeting(sampleToken);
        });

        btnJoin.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MeetingActivity.class);
            intent.putExtra("token", sampleToken);
            intent.putExtra("meetingId", etMeetingId.getText().toString());
            startActivity(intent);
        });
        checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID);
        checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID);
        return view;
    }

    private void createMeeting(String token) {
        // we will make an API call to VideoSDK Server to get a roomId
        AndroidNetworking.post("https://api.videosdk.live/v2/rooms")
                .addHeaders("Authorization", token) //we will pass the token in the Headers
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // response will contain `roomId`
                            final String meetingId = response.getString("roomId");

                            // starting the MeetingActivity with received roomId and our sampleToken
                            Intent intent = new Intent(getActivity(), MeetingActivity.class);
                            intent.putExtra("token", sampleToken);
                            intent.putExtra("meetingId", meetingId);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.printStackTrace();
                        Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
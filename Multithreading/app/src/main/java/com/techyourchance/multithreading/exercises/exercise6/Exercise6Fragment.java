package com.techyourchance.multithreading.exercises.exercise6;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.techyourchance.multithreading.DefaultConfiguration;
import com.techyourchance.multithreading.R;
import com.techyourchance.multithreading.common.BaseFragment;

import java.math.BigInteger;

public class Exercise6Fragment extends BaseFragment implements FactorialUseCase.Listener {

    private static int MAX_TIMEOUT_MS = DefaultConfiguration.DEFAULT_FACTORIAL_TIMEOUT_MS;

    public static Fragment newInstance() {
        return new Exercise6Fragment();
    }

    private FactorialUseCase mFactorialUseCase;

    private EditText mEdtArgument;
    private EditText mEdtTimeout;
    private Button mBtnStartWork;
    private TextView mTxtResult;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_6, container, false);

        mEdtArgument = view.findViewById(R.id.edt_argument);
        mEdtTimeout = view.findViewById(R.id.edt_timeout);
        mBtnStartWork = view.findViewById(R.id.btn_compute);
        mTxtResult = view.findViewById(R.id.txt_result);

        mFactorialUseCase = new FactorialUseCase();
        mFactorialUseCase.registerListener(this);

        mBtnStartWork.setOnClickListener(v -> {
            if (mEdtArgument.getText().toString().isEmpty()) {
                return;
            }
            mTxtResult.setText("");
            mBtnStartWork.setEnabled(false);

            InputMethodManager imm =
                    (InputMethodManager) requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mBtnStartWork.getWindowToken(), 0);

            int argument = Integer.valueOf(mEdtArgument.getText().toString());
            mFactorialUseCase.computeFactorial(argument, getTimeout());
        });

        return view;
    }

    @Override
    public void onStop() {
        mFactorialUseCase.stopComputation();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        mFactorialUseCase.registerListener(this);
    }

    private int getTimeout() {
        int timeout;
        if (mEdtTimeout.getText().toString().isEmpty()) {
            timeout = MAX_TIMEOUT_MS;
        } else {
            timeout = Integer.valueOf(mEdtTimeout.getText().toString());
            if (timeout > MAX_TIMEOUT_MS) {
                timeout = MAX_TIMEOUT_MS;
            }
        }
        return timeout;
    }

    @Override
    protected String getScreenTitle() {
        return "Exercise 6";
    }

    @Override
    public void onComputationCompleted(String result) {
        Log.d("Ex6", "Result: " + result);
        mTxtResult.setText(result);
        mBtnStartWork.setEnabled(true);
    }
}

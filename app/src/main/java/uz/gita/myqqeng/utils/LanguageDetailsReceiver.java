package uz.gita.myqqeng.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import java.util.ArrayList;
import java.util.List;

import uz.gita.myqqeng.MainActivity;

public class LanguageDetailsReceiver extends BroadcastReceiver {
    List<String> mLanguages;
    MainActivity mSSL;

    public LanguageDetailsReceiver(MainActivity ssl) {
        mSSL = ssl;
        mLanguages= new ArrayList<String>();
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle extras = getResultExtras(true);
        mLanguages = extras.getStringArrayList
                (RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES);
        if (mLanguages == null) {
            mSSL.updateResults("sóz tabılmadı");
        }
    }
}

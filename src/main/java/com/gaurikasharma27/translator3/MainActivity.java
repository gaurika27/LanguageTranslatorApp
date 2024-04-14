package com.gaurikasharma27.translator3;

import static com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class MainActivity extends AppCompatActivity {

    private Spinner fromSpinner, toSpinner;
    private EditText sourceEdt;
    private Button btn;
    private TextView translatedTV;

    //source array of strings- spinners' data
    String[] fromLanguages={
            "from","from","English","Afrikaans","Arabic","Belarusian","Bengali","Catalan","Czech", "Welsh", "Hindi", "Urdu"
    };
    String[] toLanguages={
            "to","from","English","Afrikaans","Arabic","Belarusian","Bengali","Catalan","Czech", "Welsh", "Hindi", "Urdu"
    };

    //Permissions
    private static final int REQUEST_PERMISSION_CODE=1;

    int languageCode, fromLanguageCode, toLanguageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromSpinner=findViewById(R.id.idFromSpinner);
        toSpinner=findViewById(R.id.idToSpinner);
        sourceEdt=findViewById(R.id.idEditSource);
        btn=findViewById(R.id.button);
        translatedTV=findViewById(R.id.idTranslated);


        //Spinner 1
        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromLanguageCode=GetLanguageCode(fromLanguages[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter fromAdapter=new ArrayAdapter(this,R.layout.spinner_item, fromLanguages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        //spinner 2
        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toLanguageCode=GetLanguageCode(toLanguages[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter toAdapter=new ArrayAdapter(this,R.layout.spinner_item,toLanguages);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                translatedTV.setText("");
                if(sourceEdt.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Please enter your text to translate", Toast.LENGTH_SHORT).show();
                }else if(fromLanguageCode == 0){
                    Toast.makeText(MainActivity.this, "Please select source language", Toast.LENGTH_SHORT).show();
                }else if(toLanguageCode == 0){
                    Toast.makeText(MainActivity.this, "Please language to translate", Toast.LENGTH_SHORT).show();
                }else{
                    translateText(fromLanguageCode, toLanguageCode, sourceEdt.getText().toString());
                }

            }
        });
    }

    private void translateText(int fromLanguageCode, int toLanguageCode, String src){
        translatedTV.setText("Downloading Modal...");

        try {
            TranslatorOptions options = new TranslatorOptions.Builder().setSourceLanguage(String.valueOf(fromLanguageCode)).setTargetLanguage(String.valueOf(toLanguageCode)).build();

            Translator translator= Translation.getClient(options);
            DownloadConditions conditions=new DownloadConditions.Builder().build();
            translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    translatedTV.setText("Translating...");
                    translator.translate(src).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            translatedTV.setText(s);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Fail to translate: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, "Fail to download the language: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    private int GetLanguageCode(String language){
        int languageCode=0;

        switch (language) {
            case "English":
                languageCode = FirebaseTranslateLanguage.EN;
                break;
            case "Afrikaans":
                languageCode = FirebaseTranslateLanguage.AF;
                break;
            case "Arabic":
                languageCode = FirebaseTranslateLanguage.AR;
                break;
            case "Belarusian":
                languageCode = FirebaseTranslateLanguage.BE;
                break;
            case "Bengali":
                languageCode = FirebaseTranslateLanguage.BN;
                break;
            case "Catalan":
                languageCode = FirebaseTranslateLanguage.CA;
                break;
            case "Czech":
                languageCode = FirebaseTranslateLanguage.CS;
                break;
            case "Welsh":
                languageCode = FirebaseTranslateLanguage.CY;
                break;
            case "Hindi":
                languageCode = FirebaseTranslateLanguage.HI;
                break;
            case "Urdu":
                languageCode = FirebaseTranslateLanguage.UR;
                break;
            default:
                languageCode = 0;
        }
        return languageCode;
    }
}
package com.dummies.android.silentmodetoggle;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    /**A variavel private AudioManager no nivel da classe. com isso podemos ter acesso a ela em outras partes da activity*/
    private AudioManager mAudioManager;
    private boolean mPhoneIsSilent;
    private static final String LOG_TAG= "SilentModeApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**onCreate eh chamado quando a activity eh criada a primira vez.*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**Inlicializa variavel mAudioManager como um AudioManager (fornece acesso ao controle do volume e do modo de campainha) para isso usaremos o metodo getSystemService() que retorna uma classe object*/
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        checkIfPhoneIsSilent();

        /**Metodo para configurar o "event listener" do botao*/
        setButtonClickListener();

        Log.d(LOG_TAG,"This is a test");

    }

    private void setButtonClickListener() {
        /**Abaixo vamos criar um botao chamado toggleButton, para fazer isso vamos usar o metodo findViewById(), que permite encontrar qualquer view dentro do layout da activity (retorna uma classe view), */
        Button toggleButton = (Button) findViewById(R.id.toggleButton);
        /**Para responder ao evento de toque precisamos registrar um "event listener" que responde a um evento no sistema android*/
        toggleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(mPhoneIsSilent){
                    try {
                        //mude para normal
                        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        mPhoneIsSilent = false;
                    }catch (SecurityException e){
                        Intent PermitionIntent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                        startActivity(PermitionIntent);
                        Log.d(LOG_TAG,"");
                    }
                }else{
                    try {
                        //mude para silencioso
                        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        mPhoneIsSilent = true;
                    }catch (SecurityException e){
                        Intent PermitionIntent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                        startActivity(PermitionIntent);
                    }
                }
                //muda a UI
                toggleUI();
            }
        });

    }

    private void checkIfPhoneIsSilent() {
        /**inicializa a variavel de nivel de classe mPhoneIsSilent. Isso permite ao aplicativo saber em qual estado esta a campainha de AudioManager*/
        int ringerMode;
        ringerMode = mAudioManager.getRingerMode();
        if(ringerMode == AudioManager.RINGER_MODE_SILENT){
            mPhoneIsSilent = true;
        } else{
            mPhoneIsSilent = false;
        }
    }

    /**Alterna a imagem da UI de silencioso para normal ou vice-versa.*/
    private void toggleUI(){

        ImageView imageView = (ImageView) findViewById(R.id.phone_icon);
        Drawable newPhoneImage;
        if(mPhoneIsSilent){
            newPhoneImage = getResources().getDrawable(R.drawable.phone_silent);
        }else {
            newPhoneImage = getResources().getDrawable(R.drawable.phone_on);
        }
        imageView.setImageDrawable(newPhoneImage);
    }

    @Override
    protected void onResume(){
        super.onResume();
        checkIfPhoneIsSilent();
        toggleUI();
    }
}

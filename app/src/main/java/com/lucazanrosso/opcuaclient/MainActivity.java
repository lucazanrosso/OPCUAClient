package com.lucazanrosso.opcuaclient;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.CertificateValidationListener;
import com.prosysopc.ua.PkiFileBasedCertificateValidator;
import com.prosysopc.ua.UserIdentity;
import com.prosysopc.ua.client.UaClient;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.SecurityMode;

import java.io.File;
import java.util.EnumSet;
import java.util.Locale;

public class MainActivity extends Activity {
    Button connectButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectButton = findViewById(R.id.connectButton);
        textView = findViewById(R.id.textView);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectAndReadTask task = new ConnectAndReadTask();
                task.execute();
            }
        });
    }

    private class ConnectAndReadTask extends AsyncTask<Void, Void, String> {

        public ConnectAndReadTask() {
        }

        @Override
        protected String doInBackground(Void... params) {
            UaClient client;
            String result;
            try {
                client = new UaClient("");

                ApplicationDescription appDescription = new ApplicationDescription();
                appDescription.setApplicationName(new LocalizedText("SimpleAndroidClient",
                        Locale.ENGLISH));
                String android_id = Settings.Secure.getString(
                        getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                appDescription.setApplicationUri("urn:" + android_id +":UA:SimpleAndroidClient");
                appDescription.setProductUri("urn:prosysopc.com:UA:SimpleAndroidClient");
                appDescription.setApplicationType(ApplicationType.Client);

                PkiFileBasedCertificateValidator validator = new PkiFileBasedCertificateValidator(
                        getFilesDir().getPath() + "/PKI/CA");
                validator.setValidationListener(new CertificateValidationListener() {
                    @Override
                    public PkiFileBasedCertificateValidator.ValidationResult onValidate(
                            Cert cert, ApplicationDescription applicationDescription,
                            EnumSet<PkiFileBasedCertificateValidator.CertificateCheck> enumSet) {
                        return PkiFileBasedCertificateValidator.ValidationResult.AcceptPermanently;
                    }
                });
                client.setCertificateValidator(validator);

                ApplicationIdentity identity = ApplicationIdentity.loadOrCreateCertificate(
                        appDescription, "Sample Organisation", "opcua",
                        new File(validator.getBaseDir(), "private"), true);

                identity.setApplicationDescription(appDescription);
                client.setApplicationIdentity(identity);

                client.setTimeout(60000);
                client.setSecurityMode(SecurityMode.NONE);
                client.setUserIdentity(new UserIdentity());
                client.connect();

                DataValue dv = client.readValue(Identifiers.Server_ServerStatus_CurrentTime);
                result = (dv.getValue().getValue()).toString();

                client.disconnect();
            } catch (Exception e) {
                result = e.toString();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            textView.setText(s);
        }
    }
}

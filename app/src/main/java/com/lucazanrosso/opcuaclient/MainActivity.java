package com.lucazanrosso.opcuaclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.security.Security;

import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.transport.ServiceChannel;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.utils.CertificateUtils;
import org.opcfoundation.ua.utils.EndpointUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);

    }

    public void connect(View view) {
        new  Connect().execute(null, null, null);
    }

    private static class Connect extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                ////////////// CLIENT //////////////
                Application myClientApplication = new Application();
                // Load Client's Application Instance Certificate from file
                KeyPair myClientApplicationInstanceCertificate = null;

                // Create Client Application Instance Certificate
                {
                    String certificateCommonName = "UA Sample Client";
                    System.out.println("Generating new Certificate for Client using CN: " + certificateCommonName);
                    String applicationUri = myClientApplication.getApplicationUri();
                    String organisation = "Sample Organisation";
                    int validityTime = 365;
                    myClientApplicationInstanceCertificate = CertificateUtils
                            .createApplicationInstanceCertificate(certificateCommonName, organisation, applicationUri, validityTime);
                }

                // Create Client
                Client myClient = new Client(myClientApplication);
                myClientApplication.addApplicationInstanceCertificate(myClientApplicationInstanceCertificate);
                //////////////////////////////////////

                /////////// DISCOVER ENDPOINT ////////
                // Discover server's endpoints, and choose one
                EndpointDescription[] endpoints = myClient.discoverEndpoints(""); // 51210=Sample
                // Server
                // Filter out all but opc.tcp protocol endpoints
                endpoints = EndpointUtil.selectByProtocol(endpoints, "opc.tcp");
                // Filter out all but Signed & Encrypted endpoints
//                endpoints = EndpointUtil.selectByMessageSecurityMode(endpoints, MessageSecurityMode.SignAndEncrypt);
//                // Filter out all but Basic128 cryption endpoints
//                endpoints = EndpointUtil.selectBySecurityPolicy(endpoints, SecurityPolicy.BASIC128RSA15);
                // Sort endpoints by security level. The lowest level at the beginning, the highest at the end
                // of the array
                endpoints = EndpointUtil.sortBySecurityLevel(endpoints);
                // Choose one endpoint
                EndpointDescription endpoint = endpoints[endpoints.length - 1];
                //////////////////////////////////////

                //////////// Open and close connection ////////////
                // Create Channel
                ServiceChannel myChannel = myClient.createServiceChannel(endpoint);
                System.out.println("Connected to server at '" + endpoint.getEndpointUrl() + "' with Security Mode '"
                        + endpoint.getSecurityMode() + "' and Security Policy '" + endpoint.getSecurityPolicyUri() + "'");
//                Toast.makeText(context, "Connected to server at '" + endpoint.getEndpointUrl() + "' with Security Mode '"
//                        + endpoint.getSecurityMode() + "' and Security Policy '" + endpoint.getSecurityPolicyUri() + "'", Toast.LENGTH_LONG).show();
                ///////////// SHUTDOWN /////////////
                // Close channel
                myChannel.closeAsync();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "Connecting...";
        }


        @Override
        protected void onPostExecute(String result) {

        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
}

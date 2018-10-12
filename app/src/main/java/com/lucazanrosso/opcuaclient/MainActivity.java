package com.lucazanrosso.opcuaclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.security.Security;
import java.util.Locale;

import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.application.SessionChannel;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.WriteValue;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
import org.opcfoundation.ua.utils.EndpointUtil;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    // Bouncy Castle encryption
    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text_view);
    }

    public void connect(View view) {
        new  ConnectionAsyncTask().execute(null, null, null);
    }

    private class ConnectionAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {

                ////////////// CLIENT //////////////
//                Application myClientApplication = new Application();
//
//                // Create Client Application Instance Certificate
//                KeyPair myClientApplicationInstanceCertificate;
//                String certificateCommonName = "UA Sample Client";
//                System.out.println("Generating new Certificate for Client using CN: " + certificateCommonName);
//                String applicationUri = myClientApplication.getApplicationUri();
//                String organisation = "Sample Organisation";
//                int validityTime = 3650;
//                myClientApplicationInstanceCertificate = CertificateUtils
//                        .createApplicationInstanceCertificate(certificateCommonName, organisation, applicationUri, validityTime);
//
//                // Create Client
//                Client myClient = new Client(myClientApplication);
//                myClientApplication.addApplicationInstanceCertificate(myClientApplicationInstanceCertificate);
                //////////////////////////////////////


                /////////////// CLIENT ///////////////
                // Create ApplicationDescription
                ApplicationDescription applicationDescription = new ApplicationDescription();
                applicationDescription.setApplicationName(new LocalizedText("AndroidClient", Locale.ENGLISH));
                applicationDescription.setApplicationUri("urn:localhost:AndroidClient");
                applicationDescription.setProductUri("urn:lucazanrosso:AndroidClient");
                applicationDescription.setApplicationType(ApplicationType.Client);

                // Create Client Application Instance Certificate
                KeyPair myClientApplicationInstanceCertificate = ExampleKeys.getCert(getApplicationContext(), applicationDescription);

                // Create Client
                Client myClient = Client.createClientApplication(myClientApplicationInstanceCertificate);
                //////////////////////////////////////


                /////////// DISCOVER ENDPOINT ////////
                // Discover endpoints
                EndpointDescription[] endpoints = myClient.discoverEndpoints("opc.tcp://xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

                // Filter out all but opc.tcp protocol endpoints
                endpoints = EndpointUtil.selectByProtocol(endpoints, "opc.tcp");

                // Filter out all but Signed & Encrypted endpoints
                endpoints = EndpointUtil.selectByMessageSecurityMode(endpoints, MessageSecurityMode.SignAndEncrypt);

                // Filter out all but Basic256Sha256 cryption endpoints
                endpoints = EndpointUtil.selectBySecurityPolicy(endpoints, SecurityPolicy.BASIC256SHA256);

                // Sort endpoints by security level. The lowest level at the beginning, the highest at the end of the array
                endpoints = EndpointUtil.sortBySecurityLevel(endpoints);

                // Choose one endpoint.
                EndpointDescription endpoint = endpoints[endpoints.length - 1];

                System.out.println("Security Level " + endpoint.getSecurityPolicyUri());
                System.out.println("Security Mode " + endpoint.getSecurityMode());
                //////////////////////////////////////


                /////////////// SESSION //////////////
                // Create the session from the chosen endpoint
                SessionChannel mySession = myClient.createSessionChannel(endpoint);

                // Activate the session. Use mySession.activate() if you do not want to use user authentication
                mySession.activate("xxxxxxxxxx", "xxxxxxxxxx");

                // Read a variable
                NodeId nodeId = new NodeId(5, "Counter1");
                ReadValueId readValueId = new ReadValueId(nodeId, Attributes.Value, null, null);
                ReadResponse res = mySession.Read(null, 500.0, TimestampsToReturn.Source, readValueId);
                DataValue[] dataValue = res.getResults();
                String result = dataValue[0].getValue().toString();

                // Write a variable. In this case the same variable read is set to 0
                WriteValue writeValue = new WriteValue(nodeId, Attributes.Value, null, new DataValue(new Variant(0)));
                mySession.Write(null, writeValue);

                // Close the session
                mySession.close();
                mySession.closeAsync();
                //////////////////////////////////////

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return "Connection failed";
            }
        }


        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
        }


        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }
}

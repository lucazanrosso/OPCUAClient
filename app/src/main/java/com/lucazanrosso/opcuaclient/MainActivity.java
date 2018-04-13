package com.lucazanrosso.opcuaclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.security.Security;
import java.util.Locale;

import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.application.SessionChannel;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResponse;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MessageSecurityMode;
import org.opcfoundation.ua.core.ReadRequest;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.core.WriteValue;
import org.opcfoundation.ua.transport.ServiceChannel;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.SecurityPolicy;
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

    private class Connect extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                ////////////// CLIENT //////////////
                Application myClientApplication = new Application();
//                // Load Client's Application Instance Certificate from file
//                KeyPair myClientApplicationInstanceCertificate = null;
//
//                // Create Client Application Instance Certificate
//                {
//                    String certificateCommonName = "UA Sample Client";
//                    System.out.println("Generating new Certificate for Client using CN: " + certificateCommonName);
//                    String applicationUri = myClientApplication.getApplicationUri();
//                    System.out.println("Application URI 2: " + applicationUri);
//                    String organisation = "Sample Organisation";
//                    int validityTime = 365;
//                    myClientApplicationInstanceCertificate = CertificateUtils
//                            .createApplicationInstanceCertificate(certificateCommonName, organisation, applicationUri, validityTime);
//                }
//
//                // Create Client
//                Client myClient = new Client(myClientApplication);
//                myClientApplication.addApplicationInstanceCertificate(myClientApplicationInstanceCertificate);
                //////////////////////////////////////



                ApplicationDescription applicationDescription = new ApplicationDescription();
                applicationDescription.setApplicationName(new LocalizedText("AndroidClient", Locale.ENGLISH));
                applicationDescription.setApplicationUri("urn:localhost:AndroidClient");
                applicationDescription.setProductUri("urn:lucazanrosso:AndroidClient");
                applicationDescription.setApplicationType(ApplicationType.Client);

                KeyPair myClientApplicationInstanceCertificate = ExampleKeys.getCert(getApplicationContext(),applicationDescription);
                Client myClient = Client.createClientApplication(myClientApplicationInstanceCertificate);
                System.out.println("Application URI " + myClient.getApplication().getApplicationUri());

                /////////// DISCOVER ENDPOINT ////////
//                 Discover server's endpoints, and choose one
                EndpointDescription[] endpoints = myClient.discoverEndpoints("opc.tcp://DESKTOP-EGB7B8G.homenet.telecomitalia.it:53530/OPCUA/SimulationServer"); // 51210=Sample
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
                System.out.println("Security Level " + endpoint.getSecurityPolicyUri());
                System.out.println("Security Mode " + endpoint.getSecurityMode());
                //////////////////////////////////////

                SessionChannel mySession = myClient.createSessionChannel(endpoint);

                mySession.activate("Luca", "lukesky");
//                mySession.activate();

////                // Read a variable
                ReadResponse res = mySession.Read(null, 500.0, TimestampsToReturn.Source,
                        new ReadValueId(new NodeId(5, "Counter1"), Attributes.Value, null, null));
                System.out.println(res);

                mySession.Write(null, new WriteValue(new NodeId(5, "Counter1"), Attributes.Value, null, new DataValue(new Variant(0))));

                mySession.close();
                mySession.closeAsync();

//                //////////// Open and close connection ////////////
//                // Create Channel
//                ServiceChannel myChannel = myClient.createServiceChannel(endpoint);
//                System.out.println("Connected to server at '" + endpoint.getEndpointUrl() + "' with Security Mode '"
//                        + endpoint.getSecurityMode() + "' and Security Policy '" + endpoint.getSecurityPolicyUri() + "'");
//                System.out.println(myChannel.Read(null, 500.0, TimestampsToReturn.Source,
//                        new ReadValueId(new NodeId(5, "Counter1"), Attributes.Value, null, null)));
//
//                ///////////// SHUTDOWN /////////////
//                // Close channel
//                myChannel.closeAsync();
//                // Unbind endpoint. This also closes the socket 6001 as it has no more endpoints.
//
//                //////////////////////////////////////

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

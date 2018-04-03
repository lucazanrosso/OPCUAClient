package com.lucazanrosso.opcuaclient;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.security.Security;
import java.util.Arrays;

import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.application.SessionChannel;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.BrowseDescription;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResponse;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReadRequest;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.TimestampsToReturn;
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
                EndpointDescription endpoint = endpoints[0];
                //////////////////////////////////////



                //////////// Open and close connection ////////////
//                Create Channel
//                ServiceChannel myChannel = myClient.createServiceChannel(endpoint);

                //////////// TEST-STACK ////////////
                // Create Channel
//                ServiceChannel myChannel = myClient.createServiceChannel(endpoint);
//                // Create Test Request
//                ReadValueId[] nodesToRead = {new ReadValueId(Identifiers.RootFolder, Attributes.BrowseName, null, null)};
//                ReadRequest req = new ReadRequest(null, 0.0, TimestampsToReturn.Both, nodesToRead);
//                System.out.println("REQUEST: " + req);
//
//                // Invoke service
//                ReadResponse res = myChannel.Read(req);
//                // Print result
//                System.out.println("RESPONSE: " + res);
//                //////////////////////////////////////
//
//
//                System.out.println("Connected to server at '" + endpoint.getEndpointUrl() + "' with Security Mode '"
//                        + endpoint.getSecurityMode() + "' and Security Policy '" + endpoint.getSecurityPolicyUri() + "'");
//
//                ///////////// SHUTDOWN /////////////
////                 Close channel
//                myChannel.closeAsync();



                SessionChannel mySession = myClient.createSessionChannel(endpoint);
                // mySession.activate("username", "123");
                mySession.activate();

                // Read a variable
                ReadResponse res4 = mySession.Read(null, 500.0, TimestampsToReturn.Source,
                        new ReadValueId(new NodeId(5, "Counter1"), Attributes.Value, null, null));
                System.out.println(res4);


                // Other examples to test
                // 3333333
                // Browse Root
//                BrowseDescription browse = new BrowseDescription();
//                browse.setNodeId(Identifiers.RootFolder);
//                browse.setBrowseDirection(BrowseDirection.Forward);
//                browse.setIncludeSubtypes(true);
//                BrowseResponse res3 = mySession.Browse(null, null, null, browse);
//                System.out.println(res3);

                // 55555555
                // Read Namespace Array
//                ReadResponse res5 = mySession.Read(null, null, TimestampsToReturn.Neither,
//                        new ReadValueId(Identifiers.Server_NamespaceArray, Attributes.Value, null, null));
//                String[] namespaceArray = (String[]) res5.getResults()[0].getValue().getValue();
//                System.out.println(Arrays.toString(namespaceArray));

                //////////// TEST-STACK ////////////
                // Create Channel
//                ServiceChannel myChannel = myClient.createServiceChannel(endpoint);
//                // Create Test Request
//                ReadValueId[] nodesToRead = {new ReadValueId(Identifiers.RootFolder, Attributes.BrowseName, null, null)};
//                ReadRequest req = new ReadRequest(null, 0.0, TimestampsToReturn.Both, nodesToRead);
//                System.out.println("REQUEST: " + req);
//
//                // Invoke service
//                ReadResponse res = myChannel.Read(req);
//                // Print result
//                System.out.println("RESPONSE: " + res);
                //////////////////////////////////////


                mySession.close();
                mySession.closeAsync();


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

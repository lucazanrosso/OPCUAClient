# OPCUAClient
A basic android app that implements an OPC UA client.

In this same folder there is a file "OPCUAClient guide.pdf" that explains step by step how to create a simple OPC UA client for Android. 
Below are the contents of the guide:

Introduction . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  1
1 What is OPC UA? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 3
1.1 Features . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  4
1.1.1 Functional Equivalence . . . . . . . . . . . . . . . . . . . . . . . . . .  4
1.1.2 Platform Independence . . . . . . . . . . . . . . . . . . . . . . . . . . . 5
1.1.3 Security . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  5
1.1.4 Extensible . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  6
1.1.5 Information Modeling . . . . . . . . . . . . . . . . . . . . . . . . . . .  6
1.2 Architecture . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  7
1.2.1 Data Model . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  7
1.2.2 Transport . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 8
1.2.3 Base servicies . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 10
1.2.4 Information Models . . . . . . . . . . . . . . . . . . . . . . . . . . . . 10
1.2.5 Companion Models . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 11
1.2.6 Vendor Specific Extension . . . . . . . . . . . . . . . . . . . . . . . .  12
1.3 Profiles . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 12
1.4 Communication Models . . . . . . . . . . . . . . . . . . . . . . . . . . . . 13
2 Why use Android? . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 17
2.1 Open Source . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  17
2.2 Technology for more people in more places . . . . . . . . . . . . . . . . .  17
2.3 Most used OS . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 17
2.4 Android Studio and Google Play . . . . . . . . . . . . . . . . . . . . . . . 18
2.5 Documentation . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  18
3 Set up the development environment . . . . . . . . . . . . . . . . . . . . . . 19
3.1 JDK 8 . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  19
3.2 Maven . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  20
3.3 UPC UA Java Stack . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  22
3.4 SLF4J . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  23
3.5 Spongy Castle . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  23
3.6 Simulation Server . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  23
3.7 Android Studio . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 24
3.7.1 Emulator . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 25
4 Create a simple client app . . . . . . . . . . . . . . . . . . . . . . . . . . 27
4.1 Add libraries . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  27
4.2 Permissions . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  27
4.3 AsyncTask . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  29
4.4 Create a Client . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  30
4.5 Discover endpoints . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 31
4.6 Activate a Session . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 32
4.6.1 Read a value . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 32
4.6.2 Write a value . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  33
4.7 User Interface . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 33
5 Data Security . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .  37
5.1 Encryption . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 37
5.2 Certificates . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 37
5.3 Security modes and policies . . . . . . . . . . . . . . . . . . . . . . . .  40
5.4 User authentication . . . . . . . . . . . . . . . . . . . . . . . . . . . .  41
5.5 Change server settings . . . . . . . . . . . . . . . . . . . . . . . . . . . 41
Conclusion . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 45
References . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . 47

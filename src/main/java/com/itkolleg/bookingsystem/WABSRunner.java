package com.itkolleg.bookingsystem;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

@SpringBootApplication
public class WABSRunner {
    //
    public static void main(String[] args) throws IOException {
        // Erstelle einen ClassLoader für die WABSRunner-Klasse
        ClassLoader classLoader = WABSRunner.class.getClassLoader();

        // Erstelle eine Datei anhand der Ressource "serviceAccountKey.json" im Classpath
        // und lese den Inhalt der Datei in einen FileInputStream
        File file = new File(Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());
        FileInputStream serviceAccount = new FileInputStream(file.getAbsolutePath());

        // Erstelle Firebase-Optionen mit den angegebenen Anmeldeinformationen und der angegebenen Datenbank-URL
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://fir-tutorial-db-8b5e6-default-rtdb.europe-west1.firebasedatabase.app")
                .build();

        // Initialisiere die Firebase-App mit den angegebenen Optionen
        FirebaseApp.initializeApp(options);

        // Starte die Spring-Boot-Anwendung mit der WABSRunner-Klasse und den übergebenen Argumenten
        SpringApplication.run(WABSRunner.class, args);
    }
}


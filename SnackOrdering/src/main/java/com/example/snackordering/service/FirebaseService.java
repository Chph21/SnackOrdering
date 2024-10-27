package com.example.snackordering.service;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public class FirebaseService {

    private static final String BUCKET_NAME = "snackorderingapp-86e91.appspot.com";

    public static String uploadImageToFirebase(MultipartFile image) {
        try {
            // Explicitly specify the bucket name
            Bucket bucket = StorageClient.getInstance().bucket(BUCKET_NAME);
            String blobName = UUID.randomUUID().toString() + "-" + image.getOriginalFilename();
            Blob blob = bucket.create(blobName, image.getInputStream(), image.getContentType());

            // Make the blob publicly accessible
            blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

            // Generate the public URL
            String publicUrl = String.format("https://storage.googleapis.com/%s/%s", BUCKET_NAME, blobName);
            return publicUrl;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }
}
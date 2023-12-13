package com.example.yourtrainer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

public class SignUpPage extends AppCompatActivity {

    EditText emailET,passwordET,nameET,phoneET,addressET,usernameET;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button signUpBtn,uploadBtn;
    TextView signInLink;
    ImageView imageView;
    Client client;
    long maxId=0;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private StorageTask uploadTask;
    public Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        firebaseAuth=FirebaseAuth.getInstance();
        nameET=findViewById(R.id.nameET);
        phoneET=findViewById(R.id.phoneET);
        addressET=findViewById(R.id.addressET);
        usernameET=findViewById(R.id.usernameET);
        radioGroup=findViewById(R.id.radioGroup);
        emailET=findViewById(R.id.emailET);
        passwordET=findViewById(R.id.passwordET);
        signUpBtn=findViewById(R.id.signUpBtn);
        uploadBtn=findViewById(R.id.uploadBtn);
        imageView=findViewById(R.id.imageView);
        storageReference = FirebaseStorage.getInstance().getReference("Client");
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Client");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxId= snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(SignUpPage.this,"Upload in progress",Toast.LENGTH_LONG).show();
                }else {
                    FileUploader();
                }

            }
        });

        signInLink=findViewById(R.id.signInLink);

        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpPage.this, LoginPage.class);
                startActivity(i);
            }
        });


    }

    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(radioId);
    }

    private void FileChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    private void FileUploader(){
        final String imageId;
        imageId=System.currentTimeMillis()+"."+getExtension(imageUri);


        String name = nameET.getText().toString().trim();
        String phone = phoneET.getText().toString().trim();
        String address = addressET.getText().toString().trim();
        String username = usernameET.getText().toString().trim();
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton=findViewById(radioId);
        String gender = radioButton.getText().toString();
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString();



        if(name.isEmpty()){
            nameET.setError("Please enter your name!");
            nameET.requestFocus();

        }else if(phone.isEmpty()){
            phoneET.setError("Please enter your phone number!");
            phoneET.requestFocus();

        }else if(username.isEmpty()){
            usernameET.setError("Please enter your username!");
            usernameET.requestFocus();

        }else if(address.isEmpty()){
            addressET.setError("Please enter your address!");
            addressET.requestFocus();

        }else if(email.isEmpty()){
            emailET.setError("Please enter your email!");
            emailET.requestFocus();

        }
        else if(password.isEmpty()){
            passwordET.setError("Please enter your password!");
            passwordET.requestFocus();
        }
        else if(name.isEmpty() && phone.isEmpty() && username.isEmpty()&&  address.isEmpty() && email.isEmpty() && password.isEmpty()){
            Toast.makeText(SignUpPage.this,"Fields are empty!",Toast.LENGTH_SHORT);
        }
        else if(!name.isEmpty() && !phone.isEmpty() && !username.isEmpty() && !address.isEmpty() && !email.isEmpty() && !password.isEmpty()){
            //create user using authentication
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpPage.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(SignUpPage.this,"SignUp Unsuccessful, please try again",Toast.LENGTH_SHORT).show();

                    }
                    else{
                        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                        String userId=firebaseUser.getUid();
                        client = new Client();
                        client.setClientFullName(nameET.getText().toString().trim());
                        client.setClientPhone(phoneET.getText().toString().trim());
                        client.setClientUsername(usernameET.getText().toString().trim());
                        client.setClientAddress(addressET.getText().toString().trim());
                        client.setClientGender(radioButton.getText().toString());
                        client.setClientEmail(emailET.getText().toString().trim());
                        client.setImageId(imageId);
                        databaseReference.child(userId).setValue(client);

                        //Upload image in Firebase Storage
                        StorageReference ref = storageReference.child(imageId);
                        uploadTask=ref.putFile(imageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // Get a URL to the uploaded content
                                        // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                        Toast.makeText(SignUpPage.this,"Image Uploaded Successfully",Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle unsuccessful uploads
                                        // ...
                                        Toast.makeText(SignUpPage.this,"Image Uploaded Unsuccessfully",Toast.LENGTH_LONG).show();

                                    }
                                });
                        Toast.makeText(SignUpPage.this,"SignUp Successful!",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(SignUpPage.this, NavigationPage.class);
                        startActivity(i);
                    }
                }
            });
        }
        else{
            Toast.makeText(SignUpPage.this,"Error Occurred",Toast.LENGTH_SHORT).show();

        }
        //int p=Integer.parseInt(price.getText().toString().trim());
        //sw.setPrice(p);


    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData() !=null){
            imageUri=data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}

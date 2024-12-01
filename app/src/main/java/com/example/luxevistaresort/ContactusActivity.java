package com.example.luxevistaresort;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ContactusActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText messageEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        // Initialize views
        nameEditText = findViewById(R.id.contactUsName);
        emailEditText = findViewById(R.id.contactUsEmail);
        messageEditText = findViewById(R.id.contactUsMessage);
        submitButton = findViewById(R.id.contactUsSubmitButton);

        // Set onClick listener for the Submit button
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });
    }

    private void handleSubmit() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String message = messageEditText.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Please enter your name");
            nameEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email");
            emailEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(message)) {
            messageEditText.setError("Please enter your message");
            messageEditText.requestFocus();
            return;
        }

        // Simulate a successful form submission (you can replace this with API logic)
        Toast.makeText(this, "Thank you for contacting us!", Toast.LENGTH_LONG).show();

        // Clear the fields after submission
        nameEditText.setText("");
        emailEditText.setText("");
        messageEditText.setText("");
    }
}

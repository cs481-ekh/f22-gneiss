import * as React from "react";
import { ChangeEvent, useState } from "react";
import {
  Alert,
  Link,
  FormGroup,
  Snackbar,
  TextField,
} from "@mui/material";
import { Button } from "@mui/material";
import history from "./history";

export interface newAcccountFormProps {}

export function NewAccountForm(props: newAcccountFormProps) {
  const styles = {
    signIn: {
      display: "flex",
      flexDirection: "column",
      backgroundColor: "rgba(255,255,255, 0.9)",
      borderRadius: "8px",
      padding: "25px 75px",
    } as const,
    input: {
      margin: "10px",
    },
    button: {
      backgroundColor: "#D64309",
    },
  };

  const [firstNameField, setFirstNameField] = useState("");
  const [lastNameField, setLastNameField] = useState("");
  const [emailField, setEmailField] = useState("");
  const [passwordField, setPasswordField] = useState("");
  const [passwordField2, setPasswordField2] = useState("");
  const [alertReason, setAlertReason] = useState("");

  const handleFirstNameChange = (e: ChangeEvent<HTMLInputElement>) => {
    setFirstNameField(e.target.value);
  };

  const handleLastNameChange = (e: ChangeEvent<HTMLInputElement>) => {
    setLastNameField(e.target.value);
  };

  const handleEmailChange = (e: ChangeEvent<HTMLInputElement>) => {
    setEmailField(e.target.value);
  };

  const handlePasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
    setPasswordField(e.target.value);
  };

  const handlePassword2Change = (e: ChangeEvent<HTMLInputElement>) => {
    setPasswordField2(e.target.value);
  };

  //Alpha Characters only (case insensitive)
  const checkSignage = /^[a-z]+$/i;

  /* min eight characters,
  max 20 characters, 
  at least one letter, one number 
  and one special character */
  const checkPassSignage = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&^])[A-Za-z\d@$!%*#?&^]{8,20}$/;

  const handleSubmit = () => {
    console.log(`First Name: ${firstNameField}`);
    console.log(`Last Name: ${lastNameField}`);
    console.log(`Email: ${emailField}`);
    console.log(`Password: ${passwordField}`);
    console.log(`Check Password: ${passwordField2}`);

    //All fields must be filled
    if (firstNameField  === "" || lastNameField  === "" || emailField === "" || passwordField === "" || passwordField2 === "") {
      setAlertReason("Enter your name, email address, and password.");
      return;
    }

    //Passwords must match
    if (passwordField !== passwordField2) {
      setAlertReason("Your passwords must match.");
      return;
    }

    //Check username/password requirements and security for text fields.
    //Firstname, Lastname, and Email forms must use appropriate signage (alpha characters only)
    if (!(firstNameField.match(checkSignage)) || !(lastNameField.match(checkSignage)) ) {
      setAlertReason("Name fields must only be alphanumeric.");
      return;
    }

    //Passwords must be correct format (8-20 characters, 1 letter, 1 number, 1 special character, no spaces)
    if (!(passwordField.match(checkPassSignage)) || !(passwordField2.match(checkPassSignage)) ) {
      setAlertReason("Passwords must be 6-20 characters, and contain 1 letter, 1 number, 1 special character (@$!%*#?&^), and no spaces.");
      return;
    }

    //console.log info should be passed to the backend here.

    history.push("home");
  };

  const handleAlertClose = (
    event?: React.SyntheticEvent | Event,
    reason?: string
  ) => {
    if (reason === "clickaway") {
      return;
    }

    setAlertReason("");
  };

  return (
    <FormGroup style={styles.signIn}>
      <TextField
        onChange={handleFirstNameChange}
        style={styles.input}
        id="firstName"
        label="First Name"
        variant="outlined"
      />
      <TextField
        onChange={handleLastNameChange}
        style={styles.input}
        id="lastName"
        label="Last Name"
        variant="outlined"
      />
      <TextField
        onChange={handleEmailChange}
        style={styles.input}
        id="email"
        label="Email"
        variant="outlined"
      />
      <TextField
        onChange={handlePasswordChange}
        style={styles.input}
        id="password"
        label="Password"
        variant="outlined"
        type="password"
      />
      <TextField
        onChange={handlePassword2Change}
        style={styles.input}
        id="checkpassword"
        label="Confirm Password"
        variant="outlined"
        type="password"
      />
      <div>
        <Button
          onClick={handleSubmit}
          style={styles.button}
          variant="contained"
        >
          Create Account
        </Button>
      </div>
      <Link href="/" underline="always">
          Back
      </Link>
      <Snackbar open={alertReason !== ""}>
        <Alert
          className="alert"
          onClose={handleAlertClose}
          severity="error"
          sx={{ width: "100%" }}
        >
          {alertReason}
        </Alert>
      </Snackbar>
    </FormGroup>
  );
}

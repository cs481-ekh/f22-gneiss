import * as React from "react";
import { ChangeEvent, useState } from "react";
import {
  Alert,
  Checkbox,
  FormControlLabel,
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

  const [emailField, setEmailField] = useState("");
  const [passwordField, setPasswordField] = useState("");
  const [alertReason, setAlertReason] = useState("");
  const [checked, setChecked] = useState(false);

  const handleEmailChange = (e: ChangeEvent<HTMLInputElement>) => {
    setEmailField(e.target.value);
  };

  const handlePasswordChange = (e: ChangeEvent<HTMLInputElement>) => {
    setPasswordField(e.target.value);
  };

  const handleCheckedChange = (
    event: ChangeEvent<HTMLInputElement>,
    checked: boolean
  ) => {
    setChecked(checked);
  };

  const handleSubmit = () => {
    console.log(`Email: ${emailField}`);
    console.log(`Password: ${passwordField}`);

    if (emailField === "" || passwordField === "") {
      setAlertReason("Enter your email and password.");
      return;
    }

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
      <div>
        <FormControlLabel
          control={
            <Checkbox checked={checked} onChange={handleCheckedChange} />
          }
          label="keep me logged in"
        />
        <Button
          onClick={handleSubmit}
          style={styles.button}
          variant="contained"
        >
          Sign in
        </Button>
      </div>
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

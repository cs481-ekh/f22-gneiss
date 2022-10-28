import * as React from "react";
import { ChangeEvent, useState } from "react";
import {
  Alert,
  Link,
  Checkbox,
  FormControlLabel,
  FormGroup,
  Snackbar,
  TextField,
} from "@mui/material";
import { Button } from "@mui/material";
import history from "./history";
import { getHttpService } from "../data/httpService";

export interface SignInFormProps {}

export function SignInForm(props: SignInFormProps) {
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
    navLink: {
      margin: "8px",
    },
  };

  const [emailField, setEmailField] = useState("");
  const [passwordField, setPasswordField] = useState("");
  const [alertReason, setAlertReason] = useState("");
  const [checked, setChecked] = useState(false);
  const httpService = getHttpService();

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
    if (emailField === "" || passwordField === "") {
      setAlertReason("Enter your email and password.");
      return;
    }

    httpService.axios
      .post<any>("/api/user/login", {
        username: emailField,
        password: passwordField,
      })
      .then((res) => {
        httpService.setAuth(res.data.jwt);
        history.push("home");
      })
      .catch(() => {
        setAlertReason("Credentials are invalid. Try again.");
        setPasswordField("");
      });
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
        value={passwordField}
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
        <Button onClick={handleSubmit} variant="contained">
          Sign in
        </Button>
      </div>
      <Link style={styles.navLink} href="/newuser" underline="always">
        Need account?
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

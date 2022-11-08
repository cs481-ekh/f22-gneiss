import { Alert, Button, Snackbar } from "@mui/material";
import axios from "axios";
import { useState } from "react";
import { useParams } from "react-router-dom";
import { FileSelectButton } from "./fileSelectButton";
import { IStepProps } from "./IStepProps";

export interface InvoiceStepProps extends IStepProps {}

export function InvoiceStep(props: InvoiceStepProps) {
  const styles = {
    outerBox: {
      display: "flex",
      flexDirection: "row",
    } as const,
    buttons: {
      display: "flex",
      flexDirection: "column",
      justifyContent: "space-between",
      marginLeft: "10vw",
    } as const,
  };

  const [file, setFile] = useState<File>();
  const [alertActive, setAlertActive] = useState(false);
  const [alertReason, setAlertReason] = useState("");
  const { id } = useParams();

  const startAlert = (reason: string) => {
    setAlertActive(true);
    setAlertReason(reason);
  };

  const handleAlertClose = (
    event?: React.SyntheticEvent | Event,
    reason?: string
  ) => {
    if (reason === "clickaway") {
      return;
    }

    setAlertActive(false);
  };

  const validate = (f: File | undefined) => {
    if (f === undefined) {
      startAlert("You must select an Invoice (PDF) file");
      return false;
    }

    if (f.type !== "application/pdf") {
      startAlert("File selected is not a PDF");
      return false;
    }

    return true;
  };

  const save = async () => {
    if (!validate(file)) {
      return;
    }
    let formData = new FormData();
    formData.append("file", file!);

    axios
      .post(`/api/packet/invoicepdf/${id}`, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then(() => {
        props.completeStep();
      })
      .catch((e: any) => {
        startAlert("Failed to save PDF");
      });
  };

  return (
    <div style={styles.outerBox}>
      <h1>Invoice File Upload</h1>
      <div style={styles.buttons}>
        <FileSelectButton
          buttonLabel="Upload File"
          validateSelection={validate}
          file={file}
          setFile={setFile}
        />
        <Button variant="contained" onClick={save}>
          Save & Continue
        </Button>
      </div>
      <Snackbar open={alertActive}>
        <Alert
          className="alert"
          onClose={handleAlertClose}
          severity="error"
          sx={{ width: "100%" }}
        >
          {alertReason}
        </Alert>
      </Snackbar>
    </div>
  );
}

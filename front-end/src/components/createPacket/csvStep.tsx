import { Alert, Button, Snackbar } from "@mui/material";
import { useState } from "react";
import { getHttpService } from "../../data/httpService";
import { FileSelectButton } from "./fileSelectButton";
import { IStepProps } from "./IStepProps";

export interface CSVStepProps extends IStepProps {}

export function CsvStep(props: CSVStepProps) {
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
  const httpService = getHttpService()

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
      startAlert("You must select a valid CSV file");
      return false;
    }

    if (f.type !== "text/csv") {
      startAlert("File selected is not a CSV file");
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

    httpService.axios
      .post("/api/csv", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      })
      .then(() => {
        props.completeStep();
      })
      .catch((e: any) => {
        startAlert("Failed to save CSV");
      });
  };

  return (
    <div style={styles.outerBox}>
      <h1>CSV File Upload</h1>
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

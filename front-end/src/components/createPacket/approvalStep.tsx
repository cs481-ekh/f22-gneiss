import { Alert, Button, Snackbar } from "@mui/material";
import axios from "axios";
import { useState } from "react";
import { CommaSeparatedList } from "./commaSeparatedList";
import { FileSelectButton } from "./fileSelectButton";
import { IStepProps } from "./IStepProps";

export function ApprovalStep(props: IStepProps) {
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
      width: "150px",
    } as const,
  };

  const [wordSet, setWordSet] = useState(new Set<string>());
  const [file, setFile] = useState<File>();
  const [alertActive, setAlertActive] = useState(false);
  const [alertReason, setAlertReason] = useState("");

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
      startAlert("You must select a PDF file");
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
    formData.append("highlightWords", Array.from(wordSet).join(","));

    axios
      .post("/api/highlightpdf", formData, {
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
      <CommaSeparatedList wordSet={wordSet} setWordSet={setWordSet} />
      <div style={styles.buttons}>
        <FileSelectButton
          buttonLabel="Upload File"
          validateSelection={validate}
          file={file}
          setFile={setFile}
        />
        <Button variant="contained" onClick={save}>
          Save
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

import { Button } from "@mui/material";
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

  return (
    <div style={styles.outerBox}>
      <CommaSeparatedList wordSet={wordSet} setWordSet={setWordSet} />
      <div style={styles.buttons}>
        <FileSelectButton buttonLabel="Upload File" />
        <Button variant="contained" onClick={props.completeStep}>
          Save
        </Button>
      </div>
    </div>
  );
}

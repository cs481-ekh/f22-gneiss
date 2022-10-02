import { Button } from "@mui/material";
import { useState } from "react";
import { CommaSeparatedList } from "./commaSeparatedList";
import { IStepProps } from "./IStepProps";

export interface InvoiceStepProps extends IStepProps{}

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

  const [wordSet, setWordSet] = useState(new Set<string>());

  return (
    <div style={styles.outerBox}>
      <CommaSeparatedList wordSet={wordSet} setWordSet={setWordSet} />
      <div style={styles.buttons}>
        <Button variant="contained">Select Invoice</Button>
        <Button onClick={props.completeStep} variant="contained">Save & Continue</Button>
      </div>
    </div>
  );
}

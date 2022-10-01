import { Button } from "@mui/material";
import { useState } from "react";
import { CommaSeparatedList } from "./commaSeparatedList";

export interface InvoiceStepProps {}

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
        <Button variant="contained">Save</Button>
      </div>
    </div>
  );
}

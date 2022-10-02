import { Button } from "@mui/material";
import { SetStateAction, useState } from "react";
import { CommaSeparatedList } from "./commaSeparatedList";
import { IStepProps } from "./IStepProps";
//Needs to import Axios?

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

  const [file, setFile] = useState()
  function handleChange(event:any) { //event is inferred, needs specification
    setFile(event.target.files[0])
  }

  return (
    <div style={styles.outerBox}>
      <form>
        <h1>Invoice File Upload</h1>
        <input type="file" onChange={handleChange}/>
        <button type="submit">Upload</button>
      </form>
      <div style={styles.buttons}>
        <Button onClick={props.completeStep} variant="contained">Save & Continue</Button>
      </div>
    </div>
  );
}

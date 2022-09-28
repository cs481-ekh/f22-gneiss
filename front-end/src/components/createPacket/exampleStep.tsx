import { Button } from "@mui/material";
import { IStepProps } from "./IStepProps";

export function ExampleStep(props: IStepProps) {
  return (
    <div>
      <Button onClick={props.completeStep} variant="contained">
        Complete
      </Button>
    </div>
  );
}

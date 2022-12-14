import * as React from "react";
import Stepper from "@mui/material/Stepper";
import Step from "@mui/material/Step";
import StepButton from "@mui/material/StepButton";
import { IStepProps } from "./IStepProps";
import { Link, Paper } from "@mui/material";

export type StepFC = React.FC<IStepProps>;

export type StepInfo = {
  label: string;
  element: StepFC;
};

export interface CreatePacketStepperProps {
  steps: StepInfo[];
  stepsFinishedComponent: React.FC;
}

export default function CreatePacketStepper(props: CreatePacketStepperProps) {
  const styles = {
    outerBox: {
      display: "flex",
      flexDirection: "column",
      borderRadius: "8px",
      padding: "25px 75px",
      gap: "32px",
    } as const,
    stepper: {
      display: "flex",
      height: "15vh",
    },
    stepContent: {
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
      justifyContent: "center",
    } as const,
    header: {
      display: "flex",
      gap: "16px",
      padding: "16px",
      alignItems: "center",
    } as const,
  };

  const [activeStep, setActiveStep] = React.useState(0);
  const [completed, setCompleted] = React.useState<{
    [k: number]: boolean;
  }>([]);

  const totalSteps = () => {
    return props.steps.length;
  };

  const completedSteps = () => {
    return Object.keys(completed).length;
  };

  const isLastStep = () => {
    return activeStep === totalSteps() - 1;
  };

  const allStepsCompleted = () => {
    return completedSteps() === totalSteps();
  };

  const handleNext = () => {
    let newActiveStep = isLastStep()
      ? allStepsCompleted()
        ? 0
        : props.steps.findIndex((step, i) => !(i in completed))
      : activeStep + 1;
    setActiveStep(newActiveStep);
  };

  const handleStep = (step: number) => () => {
    setActiveStep(step);
  };

  const handleComplete = (step: number) => {
    const newCompleted = completed;
    newCompleted[step] = true;
    setCompleted(newCompleted);
    handleNext();
  };

  const stepComponents = props.steps.map((v, i) => {
    const stepProps: IStepProps = {
      completeStep: () => {
        handleComplete(i);
      },
    };
    return props.steps[i].element(stepProps);
  });

  return (
    <Paper style={styles.outerBox}>
      <header style={styles.header}>
        <Link href={"/f22-gneiss/home"}>Back</Link>
        <h1>Edit Packet</h1>
      </header>
      <Stepper nonLinear activeStep={activeStep}>
        {props.steps.map((step, index) => (
          <Step
            sx={{
              "& .MuiStepLabel-root .Mui-completed": {
                color: "green", // circle color (COMPLETED)
              },
            }}
            key={step.label}
            completed={completed[index]}
          >
            <StepButton color="inherit" onClick={handleStep(index)}>
              {step.label}
            </StepButton>
          </Step>
        ))}
      </Stepper>
      <div style={styles.stepContent}>{stepComponents[activeStep]}</div>
      {allStepsCompleted() && <props.stepsFinishedComponent />}
    </Paper>
  );
}

import * as React from "react";
import Box from "@mui/material/Box";
import Stepper from "@mui/material/Stepper";
import Step from "@mui/material/Step";
import StepButton from "@mui/material/StepButton";
import { IStepProps } from "./IStepProps";

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
      backgroundColor: "rgba(255,255,255, 0.9)",
      borderRadius: "8px",
      padding: "25px 75px",
      height: "50vh",
      width: "50vw",
    } as const,
    stepper: {
      display: "flex",
      height: "15vh",
    },
    stepContent: {
      display: "flex",
      flexDirection: "column",
      height: "100vh",
      alignItems: "center",
      justifyContent: "center",
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
    <Box style={styles.outerBox}>
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
    </Box>
  );
}

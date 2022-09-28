import { StepInfo } from "../components/createPacket/createPacketStepper";
import { ExampleStep } from "../components/createPacket/exampleStep";

export const createPacketSteps: StepInfo[] = [
  {
    label: "Invoice",
    element: ExampleStep,
  },
  {
    label: "Approval",
    element: ExampleStep,
  },
  {
    label: "CSV",
    element: ExampleStep,
  },
];

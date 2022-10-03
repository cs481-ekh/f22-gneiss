import { ApprovalStep } from "../components/createPacket/approvalStep";
import { StepInfo } from "../components/createPacket/createPacketStepper";
import { ExampleStep } from "../components/createPacket/exampleStep";

export const createPacketSteps: StepInfo[] = [
  {
    label: "Invoice",
    element: ExampleStep,
  },
  {
    label: "Approval",
    element: ApprovalStep,
  },
  {
    label: "CSV",
    element: ExampleStep,
  },
];

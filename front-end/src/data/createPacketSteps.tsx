import { ApprovalStep } from "../components/createPacket/approvalStep";
import { StepInfo } from "../components/createPacket/createPacketStepper";
import { ExampleStep } from "../components/createPacket/exampleStep";
import { InvoiceStep } from "../components/createPacket/invoiceStep";

export const createPacketSteps: StepInfo[] = [
  {
    label: "Invoice",
    element: InvoiceStep,
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

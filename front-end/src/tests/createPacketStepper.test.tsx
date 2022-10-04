import { Button } from "@mui/material";
import { render, fireEvent, screen, waitFor } from "@testing-library/react";
import CreatePacketStepper, {
  StepInfo,
} from "../components/createPacket/createPacketStepper";
import { IStepProps } from "../components/createPacket/IStepProps";

function testStep(props: IStepProps) {
  return (
    <div>
      <h1>Test Step</h1>
      <Button onClick={props.completeStep}>Complete</Button>
    </div>
  );
}

const stepItems: StepInfo[] = [
  {
    label: "Step 1",
    element: testStep,
  },
  {
    label: "Step 2",
    element: testStep,
  },
  {
    label: "Step 3",
    element: testStep,
  },
];

test.each(stepItems)("Each step has button", (item) => {
  render(
    <CreatePacketStepper
      steps={stepItems}
      stepsFinishedComponent={() => <p>All Done</p>}
    />
  );
  expect(screen.getByText(item.label)).toBeTruthy();
});

test("Test element appears when steps completed", () => {
  render(
    <CreatePacketStepper
      steps={stepItems}
      stepsFinishedComponent={() => <p>All Done</p>}
    />
  );
  stepItems.forEach(() => {
    fireEvent.click(screen.getByText("Complete"));
  });
  expect(screen.getByText("All Done")).toBeTruthy();
});

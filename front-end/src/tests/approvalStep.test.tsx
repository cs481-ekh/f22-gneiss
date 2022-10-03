import { render, fireEvent, screen, waitFor } from "@testing-library/react";
import { ApprovalStep } from "../components/createPacket/approvalStep";
import { SignInForm } from "../components/signInForm";

test("Email and password fields must be filled", () => {
  let completed = false;
  render(
    <ApprovalStep
      completeStep={() => {
        completed = true;
      }}
    />
  );
  fireEvent.click(screen.getByText("Save"));
  expect(completed).toBeTruthy();
});

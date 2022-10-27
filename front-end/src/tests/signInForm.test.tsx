import { render, fireEvent, screen, waitFor } from "@testing-library/react";
import { SignInForm } from "../components/signInForm";

test("Email and password fields must be filled", () => {
  render(<SignInForm />);
  fireEvent.click(screen.getByText("Sign in"));
  expect(screen.getByText("Enter your email and password.")).toBeTruthy();
});

test("If input validation succeeds, go to landing page", () => {
  const result = render(<SignInForm />);
  fireEvent.change(screen.getByLabelText("Email"), {
    target: { value: "email" },
  });
  fireEvent.change(screen.getByLabelText("Password"), {
    target: { value: "password" },
  });
  fireEvent.click(screen.getByText("Sign in"));
  expect(() => screen.getByRole("alert")).toThrow();
});

//Expand test for sign in with valid/invalid JWT

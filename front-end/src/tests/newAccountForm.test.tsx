import { render, fireEvent, screen, waitFor } from "@testing-library/react";
import { NewAccountForm } from "../components/newAccountForm";

test("First Name, Last Name, Email, Password, and Confirm Password fields must be filled", () => {
  render(<NewAccountForm />);
  fireEvent.change(screen.getByLabelText("Email"), {
    target: { value: "email" },
  });
  fireEvent.click(screen.getByText("Create Account"));
  expect(screen.getByText("Enter your name, email address, and password.")).toBeTruthy();
});

//Firstname, Lastname, and Email forms must use appropriate signage
test("Firstname, Lastname, and Email forms must use appropriate signage", () => {
  render(<NewAccountForm />);
  fireEvent.change(screen.getByLabelText("First Name"), {
    target: { value: "Luc4$" },
  });
  fireEvent.change(screen.getByLabelText("Last Name"), {
    target: { value: "R1n%l3r" },
  });
  fireEvent.change(screen.getByLabelText("Email"), {
    target: { value: "valid@boisestate.edu" },
  });
  fireEvent.change(screen.getByLabelText("Password"), {
    target: { value: "1234ValidPa$$" },
  });
  fireEvent.change(screen.getByLabelText("Confirm Password"), {
    target: { value: "1234ValidPa$$" },
  });
  fireEvent.click(screen.getByText("Create Account"));
  expect(screen.getByText("Name fields must only be alphanumeric.")).toBeTruthy();
});

//Passwords must be correct format (6-18 characters, 1 letter, 1 number, 1 special character, no spaces)
// test("Passwords must be correct format", () => {
//   render(<NewAccountForm />);
//   fireEvent.click(screen.getByText("Create Account"));
//   expect(screen.getByText("Enter your name, email address, and password.")).toBeTruthy();
// });

//Passwords must match

// test("If input validation succeeds, go to landing page", () => {
//   const result = render(<NewAccountForm />);
//   fireEvent.change(screen.getByLabelText("Email"), {
//     target: { value: "email" },
//   });
//   fireEvent.change(screen.getByLabelText("Password"), {
//     target: { value: "password" },
//   });
//   fireEvent.click(screen.getByText("Create Account"));
//   expect(() => screen.getByRole("alert")).toThrow();
// });

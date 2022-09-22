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

//Firstname and Lastname forms must use appropriate signage
test("Firstname and Lastname forms must use appropriate signage", () => {
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
  expect(screen.getByText("Name fields must only be alphabetic characters, - or '.")).toBeTruthy();
});

test("Firstname and Lastname forms must allow for - or '", () => {
  render(<NewAccountForm />);
  fireEvent.change(screen.getByLabelText("First Name"), {
    target: { value: "Jo'seph" },
  });
  fireEvent.change(screen.getByLabelText("Last Name"), {
    target: { value: "Gordon-Levitt" },
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
  expect(() => screen.getByRole("alert")).toThrow();
});

test("Firstname and Lastname signage, spaces", () => {
  render(<NewAccountForm />);
  fireEvent.change(screen.getByLabelText("First Name"), {
    target: { value: "Luc as" },
  });
  fireEvent.change(screen.getByLabelText("Last Name"), {
    target: { value: "Rins ler" },
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
  expect(screen.getByText("Name fields must only be alphabetic characters, - or '.")).toBeTruthy();
});

//Email should follow expected format and only allow basic signage.
test("Email should follow expected format and only allow basic signage", () => {
  render(<NewAccountForm />);
  fireEvent.change(screen.getByLabelText("First Name"), {
    target: { value: "Lucas" },
  });
  fireEvent.change(screen.getByLabelText("Last Name"), {
    target: { value: "Rinsler" },
  });
  fireEvent.change(screen.getByLabelText("Email"), {
    target: { value: "1#valid @boisestateedu" },
  });
  fireEvent.change(screen.getByLabelText("Password"), {
    target: { value: "1A$a123456789012345!a" },
  });
  fireEvent.change(screen.getByLabelText("Confirm Password"), {
    target: { value: "1A$a123456789012345!a" },
  });
  fireEvent.click(screen.getByText("Create Account"));
  expect(screen.getByText("Please enter a valid email address.")).toBeTruthy();
});

//Passwords must be correct format (6-18 characters, 1 letter, 1 number, 1 special character, no spaces)
test("Passwords must be correct format (all lowercase)", () => {
  render(<NewAccountForm />);
  fireEvent.change(screen.getByLabelText("First Name"), {
    target: { value: "Lucas" },
  });
  fireEvent.change(screen.getByLabelText("Last Name"), {
    target: { value: "Rinsler" },
  });
  fireEvent.change(screen.getByLabelText("Email"), {
    target: { value: "valid@boisestate.edu" },
  });
  fireEvent.change(screen.getByLabelText("Password"), {
    target: { value: "invalidpass" },
  });
  fireEvent.change(screen.getByLabelText("Confirm Password"), {
    target: { value: "invalidpass" },
  });
  fireEvent.click(screen.getByText("Create Account"));
  expect(screen.getByText("Passwords must be 6-20 characters, and contain 1 letter, 1 number, 1 special character (@$!%*#?&^_), and no spaces.")).toBeTruthy();
});

test("Passwords must be correct format (ALL UPPERCASE)", () => {
  render(<NewAccountForm />);
  fireEvent.change(screen.getByLabelText("First Name"), {
    target: { value: "Lucas" },
  });
  fireEvent.change(screen.getByLabelText("Last Name"), {
    target: { value: "Rinsler" },
  });
  fireEvent.change(screen.getByLabelText("Email"), {
    target: { value: "valid@boisestate.edu" },
  });
  fireEvent.change(screen.getByLabelText("Password"), {
    target: { value: "INVALIDPASS" },
  });
  fireEvent.change(screen.getByLabelText("Confirm Password"), {
    target: { value: "INVALIDPASS" },
  });
  fireEvent.click(screen.getByText("Create Account"));
  expect(screen.getByText("Passwords must be 6-20 characters, and contain 1 letter, 1 number, 1 special character (@$!%*#?&^_), and no spaces.")).toBeTruthy();
});

test("Passwords must be correct format (All Letters)", () => {
  render(<NewAccountForm />);
  fireEvent.change(screen.getByLabelText("First Name"), {
    target: { value: "Lucas" },
  });
  fireEvent.change(screen.getByLabelText("Last Name"), {
    target: { value: "Rinsler" },
  });
  fireEvent.change(screen.getByLabelText("Email"), {
    target: { value: "valid@boisestate.edu" },
  });
  fireEvent.change(screen.getByLabelText("Password"), {
    target: { value: "InvalidPass" },
  });
  fireEvent.change(screen.getByLabelText("Confirm Password"), {
    target: { value: "InvalidPass" },
  });
  fireEvent.click(screen.getByText("Create Account"));
  expect(screen.getByText("Passwords must be 6-20 characters, and contain 1 letter, 1 number, 1 special character (@$!%*#?&^_), and no spaces.")).toBeTruthy();
});

test("Passwords must be correct format (All Numbers)", () => {
  render(<NewAccountForm />);
  fireEvent.change(screen.getByLabelText("First Name"), {
    target: { value: "Lucas" },
  });
  fireEvent.change(screen.getByLabelText("Last Name"), {
    target: { value: "Rinsler" },
  });
  fireEvent.change(screen.getByLabelText("Email"), {
    target: { value: "valid@boisestate.edu" },
  });
  fireEvent.change(screen.getByLabelText("Password"), {
    target: { value: "012345678" },
  });
  fireEvent.change(screen.getByLabelText("Confirm Password"), {
    target: { value: "012345678" },
  });
  fireEvent.click(screen.getByText("Create Account"));
  expect(screen.getByText("Passwords must be 6-20 characters, and contain 1 letter, 1 number, 1 special character (@$!%*#?&^_), and no spaces.")).toBeTruthy();
});

test("Passwords must be correct format (All Symbols)", () => {
  render(<NewAccountForm />);
  fireEvent.change(screen.getByLabelText("First Name"), {
    target: { value: "Lucas" },
  });
  fireEvent.change(screen.getByLabelText("Last Name"), {
    target: { value: "Rinsler" },
  });
  fireEvent.change(screen.getByLabelText("Email"), {
    target: { value: "valid@boisestate.edu" },
  });
  fireEvent.change(screen.getByLabelText("Password"), {
    target: { value: "!@#$%^&" },
  });
  fireEvent.change(screen.getByLabelText("Confirm Password"), {
    target: { value: "!@#$%^&" },
  });
  fireEvent.click(screen.getByText("Create Account"));
  expect(screen.getByText("Passwords must be 6-20 characters, and contain 1 letter, 1 number, 1 special character (@$!%*#?&^_), and no spaces.")).toBeTruthy();
});

test("Passwords must be correct format (Includes Space)", () => {
  render(<NewAccountForm />);
  fireEvent.change(screen.getByLabelText("First Name"), {
    target: { value: "Lucas" },
  });
  fireEvent.change(screen.getByLabelText("Last Name"), {
    target: { value: "Rinsler" },
  });
  fireEvent.change(screen.getByLabelText("Email"), {
    target: { value: "valid@boisestate.edu" },
  });
  fireEvent.change(screen.getByLabelText("Password"), {
    target: { value: "12345 A$valid" },
  });
  fireEvent.change(screen.getByLabelText("Confirm Password"), {
    target: { value: "12345 A$valid" },
  });
  fireEvent.click(screen.getByText("Create Account"));
  expect(screen.getByText("Passwords must be 6-20 characters, and contain 1 letter, 1 number, 1 special character (@$!%*#?&^_), and no spaces.")).toBeTruthy();
});

test("Passwords must be correct format (Too Short)", () => {
  render(<NewAccountForm />);
  fireEvent.change(screen.getByLabelText("First Name"), {
    target: { value: "Lucas" },
  });
  fireEvent.change(screen.getByLabelText("Last Name"), {
    target: { value: "Rinsler" },
  });
  fireEvent.change(screen.getByLabelText("Email"), {
    target: { value: "valid@boisestate.edu" },
  });
  fireEvent.change(screen.getByLabelText("Password"), {
    target: { value: "1A$a" },
  });
  fireEvent.change(screen.getByLabelText("Confirm Password"), {
    target: { value: "1A$a" },
  });
  fireEvent.click(screen.getByText("Create Account"));
  expect(screen.getByText("Passwords must be 6-20 characters, and contain 1 letter, 1 number, 1 special character (@$!%*#?&^_), and no spaces.")).toBeTruthy();
});

test("Passwords must be correct format (Too Long)", () => {
  render(<NewAccountForm />);
  fireEvent.change(screen.getByLabelText("First Name"), {
    target: { value: "Lucas" },
  });
  fireEvent.change(screen.getByLabelText("Last Name"), {
    target: { value: "Rinsler" },
  });
  fireEvent.change(screen.getByLabelText("Email"), {
    target: { value: "valid@boisestate.edu" },
  });
  fireEvent.change(screen.getByLabelText("Password"), {
    target: { value: "1A$a123456789012345!a" },
  });
  fireEvent.change(screen.getByLabelText("Confirm Password"), {
    target: { value: "1A$a123456789012345!a" },
  });
  fireEvent.click(screen.getByText("Create Account"));
  expect(screen.getByText("Passwords must be 6-20 characters, and contain 1 letter, 1 number, 1 special character (@$!%*#?&^_), and no spaces.")).toBeTruthy();
});

//Passwords must match
test("Passwords must be correct format (Passwords Don't Match)", () => {
  render(<NewAccountForm />);
  fireEvent.change(screen.getByLabelText("First Name"), {
    target: { value: "Lucas" },
  });
  fireEvent.change(screen.getByLabelText("Last Name"), {
    target: { value: "Rinsler" },
  });
  fireEvent.change(screen.getByLabelText("Email"), {
    target: { value: "valid@boisestate.edu" },
  });
  fireEvent.change(screen.getByLabelText("Password"), {
    target: { value: "1A$a123456789012345!a" },
  });
  fireEvent.change(screen.getByLabelText("Confirm Password"), {
    target: { value: "a!543210987654321a$A1" },
  });
  fireEvent.click(screen.getByText("Create Account"));
  expect(screen.getByText("Your passwords must match.")).toBeTruthy();
});

//If account info is valid, go to landing page.
test("If new account validation succeeds, go to landing page", () => {
  const result = render(<NewAccountForm />);
  fireEvent.change(screen.getByLabelText("First Name"), {
    target: { value: "Lucas" },
  });
  fireEvent.change(screen.getByLabelText("Last Name"), {
    target: { value: "Rinsler" },
  });
  fireEvent.change(screen.getByLabelText("Email"), {
    target: { value: "valid@boisestate.edu" },
  });
  fireEvent.change(screen.getByLabelText("Password"), {
    target: { value: "1A$a12345_789012345!" },
  });
  fireEvent.change(screen.getByLabelText("Confirm Password"), {
    target: { value: "1A$a12345_789012345!" },
  });
  fireEvent.click(screen.getByText("Create Account"));
  expect(() => screen.getByRole("alert")).toThrow();
});

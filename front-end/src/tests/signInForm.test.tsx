import { render, fireEvent, screen, waitFor } from "@testing-library/react";
import { rest } from "msw";
import { setupServer, SetupServerApi } from "msw/lib/node";
import { SignInForm } from "../components/signInForm";

let serverResponse = 0; //setting up fake server to send requests to.
const server: SetupServerApi = setupServer(
  rest.post("/api/user/login", async (req, res, ctx) => {
    return res(ctx.status(serverResponse));
  })
);
const makeServerBeforeTest = (response: number) => {
  serverResponse = response;
  server.listen();
};

test("Email and password fields must be filled", () => {
  render(<SignInForm />);
  fireEvent.click(screen.getByText("Sign in"));
  expect(screen.getByText("Enter your email and password.")).toBeTruthy();
});

test("If error response, tell user credentials are invalid", async () => {
  makeServerBeforeTest(400)
  const result = render(<SignInForm />);
  fireEvent.change(screen.getByLabelText("Email"), {
    target: { value: "email" },
  });
  fireEvent.change(screen.getByLabelText("Password"), {
    target: { value: "password" },
  });
  fireEvent.click(screen.getByText("Sign in"));
  await waitFor(() =>
    expect(screen.getByText("Credentials are invalid. Try again.")).toBeTruthy()
  );
});

test("If input validation succeeds, go to landing page", async () => {
  makeServerBeforeTest(200)
  const result = render(<SignInForm />);
  fireEvent.change(screen.getByLabelText("Email"), {
    target: { value: "email" },
  });
  fireEvent.change(screen.getByLabelText("Password"), {
    target: { value: "password" },
  });
  fireEvent.click(screen.getByText("Sign in"));
  await waitFor(() =>
    expect(() => screen.getByRole("alert")).toThrow()
  );
});

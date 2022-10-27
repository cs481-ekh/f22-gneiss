import { render, fireEvent, screen, waitFor } from "@testing-library/react";
import { SignInForm } from "../components/signInForm";
import { rest } from "msw";
import { setupServer, SetupServerApi } from "msw/node";

let serverResponse = 0; //setting up fake server to send requests to.
const server: SetupServerApi = setupServer(
  rest.post("/api/user/create", async (req, res, ctx) => {
    console.log("string " + serverResponse);
    return res(ctx.status(serverResponse));
  })
);
const makeServerBeforeTest = (response: number) => {
  serverResponse = response;
  server.listen();
};
afterEach(() => server.resetHandlers());
afterAll(() => server.close);

test("Email and password fields must be filled", () => {
  render(<SignInForm />);
  fireEvent.click(screen.getByText("Sign in"));
  expect(screen.getByText("Enter your email and password.")).toBeTruthy();
});

test("If input validation succeeds, go to landing page", () => {
  makeServerBeforeTest(200); //Expect accepting (200) message from JTW check
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

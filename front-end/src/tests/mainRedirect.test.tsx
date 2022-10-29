import { render, fireEvent, screen, waitFor } from "@testing-library/react";
import { NewAccountForm } from "../components/newAccountForm";
import { rest } from "msw";
import { setupServer, SetupServerApi } from "msw/node";
import { SignInForm } from "../components/signInForm";


// let serverResponse = 0; //setting up fake server to send requests to, may or may not need this
// const server: SetupServerApi = setupServer(
//   rest.post("/api/user/create", async (req, res, ctx) => {
//     console.log("string " + serverResponse);
//     return res(ctx.status(serverResponse));
//   })
// );
// const makeServerBeforeTest = (response: number) => {
//   serverResponse = response;
//   server.listen();
// };
// afterEach(() => server.resetHandlers());
// afterAll(() => server.close);

test("No JWT redirects to Sign In", () => {
  render(<SignInForm />);
  let localJWT = null;
  fireEvent.change(screen.getByLabelText("Email"), {
    target: { value: "email" },
  });
  fireEvent.click(screen.getByText("Create Account"));
  expect(
    screen.getByText("Enter your name, email address, and password.")
  ).toBeTruthy();
});
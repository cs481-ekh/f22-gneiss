import { render, fireEvent, screen, waitFor } from "@testing-library/react";
import { createMemoryHistory } from "history";
import { rest } from "msw";
import { setupServer, SetupServerApi } from "msw/node";
import { Navigate } from "react-router-dom";
import { MainPage } from "../pages/mainPage";
//import { useHistory } from 'react-router-dom'; //Module '"react-router-dom"' has no exported member 'useHistory'.


let serverResponse = false; //setting up fake server to send requests to, may or may not need this
const server: SetupServerApi = setupServer(
  rest.post("/api/user/create", async (req, res, ctx) => {
    console.log("string " + serverResponse);
    return res(
        ctx.json({
          validJWT: true,
        }),
      )
  })
);
const makeServerBeforeTest = (response: boolean) => {
  serverResponse = response;
  server.listen();
};
afterEach(() => server.resetHandlers());
afterAll(() => server.close);

//If the JWT registers as valid, mainPage should history.push to itself.
test("Valid JWT pushes to Main Page", () => {
  makeServerBeforeTest(true);
  const history = createMemoryHistory();
  const result = render(<MainPage pageContent={<p>Welcome home :)</p>} />);
  expect(history.location.pathname).toBe("home");
});

//If the JWT does NOT register as valid, mainPage should history.push back to signIn "/".
test("No JWT redirects to Sign In", () => {
  makeServerBeforeTest(false);
  const history = createMemoryHistory();
  const result = render(<MainPage pageContent={<p>Welcome home :)</p>} />);
  expect(history.location.pathname).toBe("/");
});
import { render, fireEvent, screen, waitFor } from "@testing-library/react";
//import { NewAccountForm } from "../components/newAccountForm";
import { rest } from "msw";
import { setupServer, SetupServerApi } from "msw/node";
//import { SignInForm } from "../components/signInForm";
//import { useHistory } from 'react-router-dom'; //Module '"react-router-dom"' has no exported member 'useHistory'.


let serverResponse = 0; //setting up fake server to send requests to, may or may not need this
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

//If the JWT registers as valid, mainPage should history.push to itself.
test("Valid JWT pushes to Main Page", () => {
  makeServerBeforeTest(200);
  const Route = () => {
    //const history = useHistory();
    
  };
});

//If the JWT does NOT register as valid, mainPage should history.push back to signIn "/".
test("No JWT redirects to Sign In", () => {
  makeServerBeforeTest(200);
  const Route = () => {
    //const history = useHistory();
    
  };
});
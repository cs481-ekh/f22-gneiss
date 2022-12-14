import { render, fireEvent, screen, waitFor } from "@testing-library/react";
import { CsvStep } from "../components/createPacket/csvStep";
import { rest } from "msw";
import { setupServer, SetupServerApi } from "msw/node";
import userEvent from "@testing-library/user-event";

let serverResponse = 0;
const server: SetupServerApi = setupServer(
  rest.post("/api/csv", async (req, res, ctx) => {
    return res(ctx.status(serverResponse));
  })
);

const makeServerBeforeTest = (response: number) => {
  serverResponse = response;
  server.listen();
};

afterEach(() => server.resetHandlers());
afterAll(() => server.close);

test("File must be selected before clicking save", () => {
  let completed = false;
  render(
    <CsvStep
      completeStep={() => {
        completed = true;
      }}
    />
  );
  fireEvent.click(screen.getByText("Save & Continue"));
  expect(screen.getByText("You must select a valid CSV file")).toBeTruthy();
});

test("Selected file must be a CSV", () => {
  let completed = false;
  render(
    <CsvStep
      completeStep={() => {
        completed = true;
      }}
    />
  );
  let file = new File(["blob"], "coolImage.png", { type: "image/png" });
  userEvent.upload(screen.getByTestId("fileSelect"), file);
  expect(screen.getByText("File selected is not a CSV file")).toBeTruthy();
});

test("400 response creates alert", async () => {
  makeServerBeforeTest(400);
  let completed = false;
  render(
    <CsvStep
      completeStep={() => {
        completed = true;
      }}
    />
  );

  let file = new File(["blob"], "coolDocument.csv", {
    type: "text/csv",
  });
  userEvent.upload(screen.getByTestId("fileSelect"), file);
  fireEvent.click(screen.getByText("Save & Continue"));
  await waitFor(() =>
    expect(screen.getByText("Failed to save CSV")).toBeTruthy()
  );
});

test("Step is completed with 200 response", async () => {
  makeServerBeforeTest(200);
  let completed = false;
  render(
    <CsvStep
      completeStep={() => {
        completed = true;
      }}
    />
  );

  let file = new File(["blob"], "coolDocument.csv", {
    type: "text/csv",
  });
  userEvent.upload(screen.getByTestId("fileSelect"), file);
  fireEvent.click(screen.getByText("Save & Continue"));
  await waitFor(() => expect(completed).toBe(true));
});
